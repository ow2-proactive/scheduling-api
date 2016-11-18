/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2015 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 *  * $$ACTIVEEON_INITIAL_DEV$$
 */
package org.ow2.proactive.scheduling.api.fetchers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ow2.proactive.scheduler.common.job.JobPriority;
import org.ow2.proactive.scheduler.common.job.JobStatus;
import org.ow2.proactive.scheduler.core.db.JobData;
import org.ow2.proactive.scheduling.api.fetchers.cursor.JobCursorMapper;
import org.ow2.proactive.scheduling.api.schema.type.DataManagement;
import org.ow2.proactive.scheduling.api.schema.type.Job;
import org.ow2.proactive.scheduling.api.schema.type.User;
import org.ow2.proactive.scheduling.api.schema.type.inputs.AbstractInput;
import org.ow2.proactive.scheduling.api.schema.type.inputs.JobInput;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import graphql.schema.DataFetchingEnvironment;


public class JobDataFetcher extends DatabaseConnectionFetcher<JobData, Job> {

    @Override
    public Object get(DataFetchingEnvironment environment) {

        Function<Root<JobData>, Path<? extends Number>> entityId = root -> root.get("id");

        BiFunction<CriteriaBuilder, Root<JobData>, List<Predicate[]>> criteria = (criteriaBuilder, root) -> {

            List<JobInput> input = new ArrayList<>();

            // use the {@code user} parent field to create a new input filter on jobs connection,
            // so that the job list contains jobs belonging to the user only
            if (User.TYPE.equals(environment.getParentType())) {
                filterOnUser(environment, input);
            }

            if (environment.getArgument("input") != null) {
                List<LinkedHashMap<String, Object>> args = environment.getArgument("input");
                input.addAll(args.stream().map(JobInput::new).collect(Collectors.toList()));
            }

            List<Predicate[]> filters = new ArrayList<>();

            if (!input.isEmpty()) {
                filters = input.stream().map(i -> {
                    List<Predicate> predicates = new ArrayList<>();

                    long jobId = i.getId();
                    String jobName = i.getJobName();
                    String owner = i.getOwner();
                    String priority = i.getPriority();
                    String projectName = i.getProjectName();
                    String status = i.getStatus();

                    if (jobId != -1L) {
                        predicates.add(criteriaBuilder.equal(root.get("id"), jobId));
                    }
                    if (!Strings.isNullOrEmpty(jobName)) {
                        predicates.add(criteriaBuilder.equal(root.get("jobName"), jobName));
                    }
                    if (!Strings.isNullOrEmpty(owner)) {
                        predicates.add(criteriaBuilder.equal(root.get("owner"), owner));
                    }
                    if (!Strings.isNullOrEmpty(priority)) {
                        predicates.add(criteriaBuilder.equal(root.get("priority"),
                                JobPriority.valueOf(priority)));
                    }
                    if (!Strings.isNullOrEmpty(projectName)) {
                        predicates.add(criteriaBuilder.equal(root.get("projectName"), projectName));
                    }
                    if (!Strings.isNullOrEmpty(status)) {
                        predicates.add(
                                criteriaBuilder.equal(root.get("status"), JobStatus.valueOf(status)));
                    }

                    return predicates.toArray(new Predicate[predicates.size()]);

                }).filter(array -> array.length > 0).collect(Collectors.toList());

            }

            return filters;
        };

        return createPaginatedConnection(environment,
                JobData.class,
                entityId,
                (t1, t2) -> Long.compare(t1.getId(), t2.getId()),
                criteria,
                new JobCursorMapper());
    }

    private void filterOnUser(DataFetchingEnvironment environment, List<JobInput> input) {
        User user = (User) environment.getSource();
        LinkedHashMap<String, Object> ownerInput = new LinkedHashMap<>();
        if(!Strings.isNullOrEmpty(user.getLogin())) {
            ownerInput.put(AbstractInput.InputFieldNameEnum.OWNER.value(), user.getLogin());
        } else {
            throw new IllegalStateException("Missing login name");
        }
        input.add(new JobInput(ownerInput));
    }

    @Override
    protected Stream<Job> dataMapping(Stream<JobData> taskStream) {
        return taskStream.parallel()
                .map(jobData -> Job.builder()
                        .dataManagement(
                                DataManagement.builder()
                                        .globalSpaceUrl(jobData.getGlobalSpace())
                                        .inputSpaceUrl(jobData.getInputSpace())
                                        .outputSpaceUrl(jobData.getOutputSpace())
                                        .userSpaceUrl(jobData.getUserSpace()).build()
                        )
                        .description(jobData.getDescription())
                        .finishedTime(jobData.getFinishedTime())
                        .genericInformation(jobData.getGenericInformation())
                        .id(jobData.getId())
                        .inErrorTime(jobData.getInErrorTime())
                        .maxNumberOfExecution(jobData.getMaxNumberOfExecution())
                        .name(jobData.getJobName())
                        .numberOfFailedTasks(jobData.getNumberOfFailedTasks())
                        .numberOfFaultyTasks(jobData.getNumberOfFaultyTasks())
                        .numberOfFinishedTasks(jobData.getNumberOfFinishedTasks())
                        .numberOfInErrorTasks(jobData.getNumberOfInErrorTasks())
                        .numberOfPendingTasks(jobData.getNumberOfPendingTasks())
                        .numberOfRunningTasks(jobData.getNumberOfRunningTasks())
                        .onTaskError(
                                CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE,
                                        jobData.getOnTaskErrorString()))
                        .owner(jobData.getOwner())
                        .priority(jobData.getPriority().name())
                        .projectName(jobData.getProjectName())
                        .removedTime(jobData.getRemovedTime())
                        .status(jobData.getStatus().name())
                        .startTime(jobData.getStartTime())
                        .submittedTime(jobData.getSubmittedTime())
                        .totalNumberOfTasks(jobData.getTotalNumberOfTasks())
                        .variables(jobData.getVariables())
                        .build());
    }

}
