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

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.ow2.proactive.scheduler.common.task.OnTaskError;
import org.ow2.proactive.scheduler.common.task.RestartMode;
import org.ow2.proactive.scheduler.common.task.TaskStatus;
import org.ow2.proactive.scheduler.common.task.flow.FlowBlock;
import org.ow2.proactive.scheduler.core.db.EnvironmentModifierData;
import org.ow2.proactive.scheduler.core.db.JobData;
import org.ow2.proactive.scheduler.core.db.ScriptData;
import org.ow2.proactive.scheduler.core.db.SelectorData;
import org.ow2.proactive.scheduler.core.db.TaskData;
import org.ow2.proactive.scheduler.core.db.TaskDataVariable;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Task;
import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.mockito.Mockito;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author ActiveEon Team
 */
public class TaskDataFetcherTest {

    @Test
    public void testDataMapping() throws Exception {

        TaskData.DBTaskId dbTaskId = new TaskData.DBTaskId();
        dbTaskId.setJobId(1);
        dbTaskId.setTaskId(2);

        TaskData taskData = new TaskData();
        taskData.setAdditionalClasspath(ImmutableList.of("classpath1", "classpath2", "classpath3"));
        taskData.setCleanScript(new ScriptData()); // currently, not in GraphQL data model
        taskData.setDataspaceSelectors(
                ImmutableList.of(new SelectorData())); // currently, not in GraphQL data model
        taskData.setDependentTasks(null); // currently, not in GraphQL data model
        taskData.setDescription("description");
        taskData.setEnvModifiers(
                ImmutableList.of(new EnvironmentModifierData())); // currently, not in GraphQL data model
        taskData.setEnvScript(new ScriptData()); // currently, not in GraphQL data model
        taskData.setExecutionDuration(3);
        taskData.setExecutionHostName("hostname");
        taskData.setFinishedTime(4);
        taskData.setFlowBlock(FlowBlock.parse(FlowBlock.START.name())); // currently not in GraphQL data model
        taskData.setFlowScript(new ScriptData()); // currently not in GraphQL data model
        taskData.setGenericInformation(ImmutableMap.of("gk1", "gv1"));
        taskData.setId(dbTaskId);
        taskData.setIfBranch(new TaskData()); // currently, not in GraphQL data model
        taskData.setInErrorTime(5);
        taskData.setIteration(6); // currently, not in GraphQL data model
        taskData.setJavaHome("javaHome");
        taskData.setJobData(new JobData()); // currently, not in GraphQL data model
        taskData.setJoinedBranches(
                ImmutableList.of(new TaskData.DBTaskId())); // currently, not in GraphQL data model
        taskData.setJvmArguments(ImmutableList.of("arg1", "arg2"));
        taskData.setMatchingBlock("matchingBlock"); // currently, not in GraphQL data model
        taskData.setMaxNumberOfExecution(7);
        taskData.setNumberOfExecutionLeft(8);
        taskData.setNumberOfExecutionOnFailureLeft(9);
        taskData.setOnTaskErrorString(OnTaskError.NONE);
        taskData.setParallelEnvNodesNumber(10); // currently, not in GraphQL data model
        taskData.setPostScript(new ScriptData()); // currently, not in GraphQL data model
        taskData.setPreciousLogs(true);
        taskData.setPreciousResult(false);
        taskData.setPreScript(new ScriptData()); // currently, not in GraphQL data model
        taskData.setReplication(8); // currently, not in GraphQL data model
        taskData.setRestartModeId(RestartMode.ANYWHERE.getIndex());
        taskData.setResultPreview("resultPreview");
        taskData.setRunAsMe(true);
        taskData.setScheduledTime(9);
        taskData.setScript(new ScriptData()); // currently, not in GraphQL data model
        taskData.setSelectionScripts(ImmutableList.of()); // currently, not in GraphQL data model
        taskData.setStartTime(10);
        taskData.setTag("tag");
        taskData.setTaskName("taskName");
        taskData.setTaskStatus(TaskStatus.SUBMITTED);
        taskData.setTaskType("taskType"); // currently, not in GraphQL data model
        taskData.setTopologyDescriptor("topologyDescriptor"); // currently, not in GraphQL data model
        taskData.setTopologyDescriptorThreshold(11); // currently, not in GraphQL data model

        TaskDataVariable taskDataVariable = new TaskDataVariable();
        taskDataVariable.setId(13);
        taskDataVariable.setJobInherited(true);
        taskDataVariable.setName("vk1");
        taskDataVariable.setValue("vv1");
        taskData.setVariables(ImmutableMap.of("vk1", taskDataVariable));

        taskData.setWallTime(12);
        taskData.setWorkingDir("workingDir");

        Stream<Task> taskStream =
                new TaskDataFetcher(() -> Mockito.mock(EntityManager.class))
                        .dataMapping(ImmutableList.of(taskData).stream());

        Optional<Task> firstElement = taskStream.findFirst();

        assertThat(firstElement.isPresent()).isTrue();

        Task task = firstElement.get();

        assertThat(taskData.getAdditionalClasspath()).isEqualTo(task.getAdditionalClasspath());
        assertThat(task.getDescription()).isEqualTo(taskData.getDescription());
        assertThat(task.getExecutionDuration()).isEqualTo(taskData.getExecutionDuration());
        assertThat(task.getExecutionHostname()).isEqualTo(taskData.getExecutionHostName());
        assertThat(task.getFinishedTime()).isEqualTo(taskData.getFinishedTime());
        assertThat(task.getGenericInformation()).isEqualTo(taskData.getGenericInformation());

        TaskData.DBTaskId taskDataId = taskData.getId();
        assertThat(task.getId()).isEqualTo(taskDataId.getTaskId());
        assertThat(task.getJobId()).isEqualTo(taskDataId.getJobId());

        assertThat(task.getInErrorTime()).isEqualTo(taskData.getInErrorTime());
        assertThat(task.getJavaHome()).isEqualTo(taskData.getJavaHome());
        assertThat(task.getJvmArguments()).isEqualTo(taskData.getJvmArguments());
        assertThat(task.getMaxNumberOfExecution()).isEqualTo(taskData.getMaxNumberOfExecution());
        assertThat(task.getNumberOfExecutionLeft()).isEqualTo(taskData.getNumberOfExecutionLeft());
        assertThat(task.getNumberOfExecutionOnFailureLeft()).isEqualTo(
                taskData.getNumberOfExecutionOnFailureLeft());
        assertThat(task.getOnTaskError()).isEqualTo(
                CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, taskData.getOnTaskErrorString()));
        assertThat(task.isPreciousLogs()).isEqualTo(taskData.isPreciousLogs());
        assertThat(task.isPreciousResult()).isEqualTo(taskData.isPreciousResult());
        assertThat(task.getRestartMode()).isEqualTo(
                RestartMode.getMode(taskData.getRestartModeId()).getDescription().toUpperCase());
        assertThat(task.getResultPreview()).isEqualTo(taskData.getResultPreview());
        assertThat(task.isRunAsMe()).isEqualTo(taskData.isRunAsMe());
        assertThat(task.getScheduledTime()).isEqualTo(taskData.getScheduledTime());
        assertThat(task.getStartTime()).isEqualTo(taskData.getStartTime());
        assertThat(task.getTag()).isEqualTo(taskData.getTag());
        assertThat(task.getName()).isEqualTo(taskData.getTaskName());
        assertThat(task.getStatus()).isEqualTo(taskData.getTaskStatus().name());

        Map<String, TaskDataVariable> expectedVariables = taskData.getVariables();
        assertThat(task.getVariables()).hasSize(expectedVariables.size());
        assertThat(task.getVariables()).hasSize(1);

        Map.Entry<String, String> entry = task.getVariables().entrySet().iterator().next();
        Map.Entry<String, TaskDataVariable> expectedEntry = expectedVariables.entrySet().iterator().next();
        TaskDataVariable expectedEntryValue = expectedEntry.getValue();

        assertThat(entry.getKey()).isEqualTo(expectedEntry.getKey());
        assertThat(entry.getKey()).isEqualTo(expectedEntryValue.getName());
        assertThat(entry.getValue()).isEqualTo(expectedEntryValue.getValue());

        assertThat(task.getWalltime()).isEqualTo(taskData.getWallTime());
        assertThat(task.getWorkingDir()).isEqualTo(taskData.getWorkingDir());
    }

}