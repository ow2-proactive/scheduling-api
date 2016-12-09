/*
 * ProActive Parallel Suite(TM):
 * The Java(TM) library for Parallel, Distributed,
 * Multi-Core Computing for Enterprise Grids & Clouds
 *
 * Copyright (c) 2016 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package org.ow2.proactive.scheduling.api.graphql.fetchers.converter;

import com.google.common.base.Strings;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ow2.proactive.scheduler.common.job.JobPriority;
import org.ow2.proactive.scheduler.common.job.JobStatus;
import org.ow2.proactive.scheduler.core.db.JobData;
import org.ow2.proactive.scheduling.api.graphql.common.InputFields;
import org.ow2.proactive.scheduling.api.graphql.common.Types;
import org.ow2.proactive.scheduling.api.graphql.schema.type.User;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.JobInput;


/**
 * @author ActiveEon Team
 */
public class JobInputConverter extends AbstractJobTaskInputConverter<JobData, JobInput> {

    @Override
    protected LinkedHashMap<String, Object> extraInputCheck(DataFetchingEnvironment environment) {
        // use the {@code user} parent field to create a new input filter on jobs connection,
        // so that the job list contains jobs belonging to the user only
        GraphQLType parentType = environment.getParentType();
        LinkedHashMap<String, Object> ownerInput = new LinkedHashMap<>();

        if (parentType != null && Types.USER.getName().equals(parentType.getName())) {
            User user = (User) environment.getSource();

            if (!Strings.isNullOrEmpty(user.getLogin())) {
                ownerInput.put(InputFields.OWNER.getName(), user.getLogin());
            } else {
                throw new IllegalStateException("Missing login name");
            }
        }
        return ownerInput;
    }

    @Override
    public List<Predicate[]> inputToPredicates(DataFetchingEnvironment environment, CriteriaBuilder criteriaBuilder,
            Root<JobData> root, List<JobInput> input) {

        return input.stream().map(i -> {
            List<Predicate> predicates = new ArrayList<>();

            boolean excludeRemoved = i.isExcludeRemoved();
            long jobId = i.getId();
            String jobName = i.getJobName();
            String owner = i.getOwner();
            String priority = i.getPriority();
            String projectName = i.getProjectName();
            String status = i.getStatus();
            long beforeSubmittedTime = -1;
            long afterSubmittedTime = -1;

            if (excludeRemoved) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("removedTime"), 0L));
            }
            if (jobId != -1L) {
                predicates.add(criteriaBuilder.equal(root.get("id"), jobId));
            }
            if (!Strings.isNullOrEmpty(jobName)) {
                predicates.add(criteriaBuilder.like(root.get("jobName"), "%" + jobName + "%"));
            }
            if (!Strings.isNullOrEmpty(owner)) {
                predicates.add(criteriaBuilder.equal(root.get("owner"), owner));
            }
            if (!Strings.isNullOrEmpty(priority)) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), JobPriority.valueOf(priority)));
            }
            if (!Strings.isNullOrEmpty(projectName)) {
                predicates.add(criteriaBuilder.equal(root.get("projectName"), projectName));
            }
            if (!Strings.isNullOrEmpty(status)) {
                predicates.add(criteriaBuilder.equal(root.get("status"), JobStatus.valueOf(status)));
            }
            if (i.getSubmittedTime() != null) {
                beforeSubmittedTime = i.getSubmittedTime().getBefore();
                afterSubmittedTime = i.getSubmittedTime().getAfter();
            }
            if (beforeSubmittedTime != -1L) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("submittedTime"), beforeSubmittedTime));
            }
            if (afterSubmittedTime != -1L) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("submittedTime"), afterSubmittedTime));
            }

            return predicates.toArray(new Predicate[predicates.size()]);

        }).filter(array -> array.length > 0).collect(Collectors.toList());
    }

    @Override
    protected Function<Map<String, Object>, JobInput> mapFunction() {
        return JobInput::new;
    }

}
