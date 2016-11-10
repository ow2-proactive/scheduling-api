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

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ow2.proactive.scheduler.core.db.TaskData;
import org.ow2.proactive.scheduling.api.fetchers.cursor.TaskCursorMapper;
import org.ow2.proactive.scheduling.api.schema.type.Job;
import org.ow2.proactive.scheduling.api.schema.type.Task;
import com.google.common.collect.Maps;
import graphql.schema.DataFetchingEnvironment;


public class TaskDataFetcher extends DatabaseConnectionFetcher<TaskData, Task> {

    @Override
    public Object get(DataFetchingEnvironment environment) {
        Job job = (Job) environment.getSource();

        Function<Root<TaskData>, Path<? extends Number>> entityId = root -> root.get("id").get("taskId");

        BiFunction<CriteriaBuilder, Root<TaskData>, Predicate[]> criteria = (criteriaBuilder,
                root) -> new Predicate[] { criteriaBuilder.equal(root.get("id").get("jobId"), job.getId()) };

        return createPaginatedConnection(environment,
                TaskData.class,
                entityId,
                (t1, t2) -> Long.compare(t1.getId().getTaskId(), t2.getId().getTaskId()),
                criteria,
                new TaskCursorMapper());
    }

    @Override
    protected Stream<Task> dataMapping(Stream<TaskData> taskStream) {
        // TODO Task progress not accessible from DB, needs to establish connection with
        // SchedulerFrontend
        // --> Active Object to get the value that is in Scheduler memory
        // TODO Variables for tasks
        return taskStream.map(taskData -> Task.builder()
                .id(taskData.getId().getTaskId())
                .description(taskData.getDescription())
                .executionDuration(taskData.getExecutionDuration())
                .executionHostName(taskData.getExecutionHostName())
                .finishedTime(taskData.getFinishedTime())
                .genericInformation(taskData.getGenericInformation())
                .inErrorTime(taskData.getInErrorTime())
                .jobId(taskData.getId().getJobId())
                .name(taskData.getTaskName())
                .numberOfExecutionLeft(taskData.getNumberOfExecutionLeft())
                .numberOfExecutionOnFailureLeft(taskData.getNumberOfExecutionOnFailureLeft())
                .scheduledTime(taskData.getScheduledTime())
                .startTime(taskData.getStartTime())
                .progress(-1)
                .status(taskData.getTaskStatus().name())
                .tag(taskData.getTag())
                .variables(Maps.newHashMap())
                .build());
    }

}
