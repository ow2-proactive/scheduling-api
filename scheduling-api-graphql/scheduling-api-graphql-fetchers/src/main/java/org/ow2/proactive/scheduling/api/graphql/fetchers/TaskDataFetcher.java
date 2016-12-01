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
package org.ow2.proactive.scheduling.api.graphql.fetchers;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ow2.proactive.scheduler.common.task.RestartMode;
import org.ow2.proactive.scheduler.core.db.TaskData;
import org.ow2.proactive.scheduling.api.graphql.fetchers.converter.JobTaskFilterInputBiFunction;
import org.ow2.proactive.scheduling.api.graphql.fetchers.converter.TaskInputConverter;
import org.ow2.proactive.scheduling.api.graphql.fetchers.cursor.TaskCursorMapper;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Task;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;
import graphql.schema.DataFetchingEnvironment;

/**
 * @author ActiveEon Team
 */
public class TaskDataFetcher extends DatabaseConnectionFetcher<TaskData, Task> {

    public TaskDataFetcher(Supplier<EntityManager> entityManager) {
        super(entityManager);
    }

    @Override
    public Object get(DataFetchingEnvironment environment) {

        Function<Root<TaskData>, Path<? extends Number>> entityId = root -> root.get("id").get("taskId");

        BiFunction<CriteriaBuilder, Root<TaskData>, List<Predicate[]>> criteria =
                new JobTaskFilterInputBiFunction(environment, new TaskInputConverter());

        return createPaginatedConnection(environment,
                TaskData.class,
                entityId,
                Comparator.comparingLong(t -> t.getId().getTaskId()),
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
                            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                    .workingDir(taskData.getWorkingDir())
                    .walltime(taskData.getWallTime())
                    .build();
        });
    }

}
