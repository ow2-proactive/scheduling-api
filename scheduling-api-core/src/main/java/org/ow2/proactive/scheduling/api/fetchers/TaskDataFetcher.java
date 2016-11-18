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
import java.util.Collections;
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

import org.ow2.proactive.scheduler.common.task.RestartMode;
import org.ow2.proactive.scheduler.common.task.TaskStatus;
import org.ow2.proactive.scheduler.core.db.TaskData;
import org.ow2.proactive.scheduling.api.fetchers.cursor.TaskCursorMapper;
import org.ow2.proactive.scheduling.api.schema.type.Job;
import org.ow2.proactive.scheduling.api.schema.type.Task;
import org.ow2.proactive.scheduling.api.schema.type.inputs.TaskInput;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import graphql.schema.DataFetchingEnvironment;


public class TaskDataFetcher extends DatabaseConnectionFetcher<TaskData, Task> {

    @Override
    public Object get(DataFetchingEnvironment environment) {
        Job job = (Job) environment.getSource();

        Function<Root<TaskData>, Path<? extends Number>> entityId = root -> root.get("id").get("taskId");

        BiFunction<CriteriaBuilder, Root<TaskData>, List<Predicate[]>> criteria = (criteriaBuilder, root) -> {

            List<TaskInput> input = ImmutableList.of();

            if (environment.getArgument("input") != null) {
                List<LinkedHashMap<String, Object>> args = environment.getArgument("input");
                input = args.stream().map(arg -> new TaskInput(arg)).collect(Collectors.toList());
            }

            List<Predicate[]> filters = ImmutableList.of();

            if (!input.isEmpty()) {
                filters = input.stream().map(i -> {
                    List<Predicate> predicates = new ArrayList<>();

                    long taskId = i.getId();
                    String status = i.getStatus();
                    String taskName = i.getTaskName();

                    predicates.add(criteriaBuilder.equal(root.get("id").get("jobId"), job.getId()));

                    if (taskId != -1l) {
                        predicates.add(criteriaBuilder.equal(root.get("id").get("taskId"), taskId));
                    }
                    if (!Strings.isNullOrEmpty(status)) {
                        predicates.add(
                                criteriaBuilder.equal(root.get("taskStatus"), TaskStatus.valueOf(status)));
                    }
                    if (!Strings.isNullOrEmpty(taskName)) {
                        predicates.add(criteriaBuilder.equal(root.get("taskName"), taskName));
                    }

                    return predicates.toArray(new Predicate[predicates.size()]);

                }).filter(array -> array.length > 1).collect(Collectors.toList());

            }

            if (filters.isEmpty()) {
                filters = Collections.singletonList(
                        new Predicate[] { criteriaBuilder.equal(root.get("id").get("jobId"), job.getId()) });
            }

            return filters;
        };

        return createPaginatedConnection(environment,
                TaskData.class,
                entityId,
                (t1, t2) -> Long.compare(t1.getId().getTaskId(), t2.getId().getTaskId()),
                criteria,
                new TaskCursorMapper());
    }

    @Override
    protected Stream<Task> dataMapping(Stream<TaskData> taskStream) {
        // TODO Task progress not accessible from DB. It implies to establish a connection with
        // the SchedulerFrontend active object to get the value that is in the Scheduler memory

        return taskStream.map(taskData -> {
            TaskData.DBTaskId id = taskData.getId();

            return Task.builder()
                    .additionalClasspath(taskData.getAdditionalClasspath())
                    .description(taskData.getDescription())
                    .executionDuration(taskData.getExecutionDuration())
                    .executionHostname(taskData.getExecutionHostName())
                    .finishedTime(taskData.getFinishedTime())
                    .genericInformation(taskData.getGenericInformation())
                    .id(id.getTaskId())
                    .inErrorTime(taskData.getInErrorTime())
                    .javaHome(taskData.getJavaHome())
                    .jobId(id.getJobId())
                    .jvmArguments(taskData.getJvmArguments())
                    .maxNumberOfExecution(taskData.getMaxNumberOfExecution())
                    .name(taskData.getTaskName())
                    .numberOfExecutionLeft(taskData.getNumberOfExecutionLeft())
                    .numberOfExecutionOnFailureLeft(taskData.getNumberOfExecutionOnFailureLeft())
                    .onTaskError(
                            CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE,
                                    taskData.getOnTaskErrorString())
                    )
                    .preciousLogs(taskData.isPreciousLogs())
                    .preciousResult(taskData.isPreciousResult())
                    .restartMode(
                            RestartMode.getMode(taskData.getRestartModeId()).getDescription().toUpperCase())
                    .resultPreview(taskData.getResultPreview())
                    .runAsMe(taskData.isRunAsMe())
                    .scheduledTime(taskData.getScheduledTime())
                    .startTime(taskData.getStartTime())
                    .status(taskData.getTaskStatus().name())
                    .tag(taskData.getTag())
                    .variables(taskData.getVariables().values().stream().map(
                            taskDataVariable -> Maps.immutableEntry(taskDataVariable.getName(),
                                    taskDataVariable.getValue())).collect(
                            Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue())))
                    .workingDir(taskData.getWorkingDir())
                    .walltime(taskData.getWallTime())
                    .build();
        });
    }

}
