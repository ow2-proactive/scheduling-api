/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
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
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.ComparableLongInput;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.JobInput;

import com.google.common.base.Strings;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLType;


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

            if (excludeRemoved) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("removedTime"), 0L));
            }
            if (jobId != -1L) {
                predicates.add(criteriaBuilder.equal(root.get("id"), jobId));
            }

            comparableLongPredicated(i.getComparableId(), "id", root, criteriaBuilder, predicates);

            if (!Strings.isNullOrEmpty(jobName)) {
                Predicate jobNamePredicate = WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                 root,
                                                                                 "jobName",
                                                                                 jobName);
                predicates.add(jobNamePredicate);
            }

            comparableLongPredicated(i.getLastUpdatedTime(), "lastUpdatedTime", root, criteriaBuilder, predicates);

            if (!Strings.isNullOrEmpty(owner)) {
                Predicate ownerPredicate = WildCardInputPredicateBuilder.build(criteriaBuilder, root, "owner", owner);
                predicates.add(ownerPredicate);
            }
            if (!Strings.isNullOrEmpty(priority)) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), JobPriority.valueOf(priority)));
            }
            if (!Strings.isNullOrEmpty(projectName)) {
                Predicate projectNamePredicate = WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                     root,
                                                                                     "projectName",
                                                                                     projectName);
                predicates.add(projectNamePredicate);
            }
            if (!Strings.isNullOrEmpty(status)) {
                predicates.add(criteriaBuilder.equal(root.get("status"), JobStatus.valueOf(status)));
            }

            comparableLongPredicated(i.getSubmittedTime(), "submittedTime", root, criteriaBuilder, predicates);

            return predicates.toArray(new Predicate[predicates.size()]);

        }).filter(array -> array.length > 0).collect(Collectors.toList());
    }

    private void comparableLongPredicated(ComparableLongInput input, String name, Root<JobData> root,
            CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        long before = -1;
        long after = -1;
        if (input != null) {
            before = input.getBefore();
            after = input.getAfter();
        }
        if (before != -1L) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(name), before));
        }
        if (after != -1L) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(name), after));
        }
    }

    @Override
    protected Function<Map<String, Object>, JobInput> mapFunction() {
        return JobInput::new;
    }

}
