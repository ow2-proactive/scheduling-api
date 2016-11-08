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

import java.util.List;
import java.util.stream.Collectors;

import org.ow2.proactive.scheduler.core.db.TaskData;
import org.ow2.proactive.scheduling.api.repository.TaskRepository;
import org.ow2.proactive.scheduling.api.schema.type.Job;
import org.ow2.proactive.scheduling.api.schema.type.Task;
import org.ow2.proactive.scheduling.api.schema.type.enums.TaskStatus;
import org.ow2.proactive.scheduling.api.service.ApplicationContextProvider;

import com.google.common.collect.Maps;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;


public class TaskDataFetcher implements DataFetcher {

    private TaskRepository taskRepository;

    private TaskRepository getTaskRepository() {
        if (taskRepository == null) {
            this.taskRepository = ApplicationContextProvider.getApplicationContext().getBean(TaskRepository.class);
        }
        return taskRepository;
    }

    @Override
    public Object get(DataFetchingEnvironment environment) {
        Job job = (Job) environment.getSource();
        List<TaskData> tasks = getTaskRepository().findByIdJobId(job.getId());

        // TODO Task progress not accessible from DB
        // TODO Variables for tasks

        return tasks.stream()
                    .parallel()
                    .map(taskData -> Task.builder()
                                         .id(taskData.getId().getTaskId())
                                         .jobId(taskData.getId().getJobId())
                                         .name(taskData.getTaskName())
                                         .status(TaskStatus.valueOf(taskData.getTaskStatus().name()))
                                         .genericInformation(taskData.getGenericInformation())
                                         .executionDuration(taskData.getExecutionDuration())
                                         .executionHostName(taskData.getExecutionHostName())
                                         .finishedTime(taskData.getFinishedTime())
                                         .inErrorTime(taskData.getInErrorTime())
                                         .numberOfExecutionLeft(taskData.getNumberOfExecutionLeft())
                                         .numberOfExecutionOnFailureLeft(taskData.getNumberOfExecutionOnFailureLeft())
                                         .scheduledTime(taskData.getScheduledTime())
                                         .startTime(taskData.getStartTime())
                                         .progress(-1)
                                         .variables(Maps.newHashMap())
                                         .build())
                    .collect(Collectors.toList());

    }

}
