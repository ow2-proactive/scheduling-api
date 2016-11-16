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
package org.ow2.proactive.scheduling.api.schema.type;

import java.util.Map;

import org.ow2.proactive.scheduling.api.fetchers.GenericInformationDataFetcher;
import org.ow2.proactive.scheduling.api.fetchers.VariablesDataFetcher;
import org.ow2.proactive.scheduling.api.schema.type.inputs.KeyValueInput;
import org.ow2.proactive.scheduling.api.schema.type.interfaces.JobTaskCommon;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLEnumType.newEnum;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;


/**
 * @author ActiveEon Team
 */
@Getter
@ToString
public class Task extends JobTaskCommon {

    private long executionDuration = -1;

    private String executionHostName;

    private long jobId;

    private int numberOfExecutionLeft = 1;

    private int numberOfExecutionOnFailureLeft = 1;

    private int progress;

    private long scheduledTime = -1;

    private String status;

    private String tag;

    public final static GraphQLEnumType TASK_STATUS_ENUM = newEnum().name("TaskStatus")
            .description("Task status list")
            .value("ABORTED",
                    "ABORTED",
                    "The task has been aborted by an exception on an other task while the task is running (job has cancelOnError=true). Can be also in this status if the job is killed while the concerned task was running")
            .value("FAILED",
                    "FAILED",
                    "The task is failed (only if max execution time has been reached and the node on which it was started is down)")
            .value("FAULTY",
                    "FAULTY",
                    "The task has finished execution with error code (!=0) or exception")
            .value("FINISHED",
                    "FINISHED",
                    "The task has finished execution")
            .value("IN_ERROR",
                    "IN_ERROR",
                    "The task is suspended after first error and is waiting for a manual restart action")
            .value("NOT_RESTARTED",
                    "NOT_RESTARTED",
                    "The task could not be restarted. It means that the task could not be restarted after an error during the previous execution")
            .value("NOT_STARTED",
                    "NOT_STARTED",
                    "The task could not be started. It means that the task could not be started due to one ore more dependency failure")
            .value("PAUSED", "PAUSED", "The task is paused")
            .value("PENDING",
                    "PENDING",
                    "The task is in the scheduler pending queue")
            .value("RUNNING",
                    "RUNNING",
                    "The task is executing")
            .value("SKIPPED",
                    "SKIPPED",
                    "The task was not executed: it was the non-selected branch of an IF/ELSE control flow action")
            .value("SUBMITTED",
                    "SUBMITTED",
                    "The task has just been submitted by the user")
            .value("WAITING_ON_ERROR",
                    "WAITING_ON_ERROR",
                    "The task is waiting for restart after an error (i.e. native code != 0 or exception)")
            .value("WAITING_ON_FAILURE",
                    "WAITING_ON_FAILURE",
                    "The task is waiting for restart after a failure (i.e. node down)")
            .build();

    public final static GraphQLObjectType TYPE = GraphQLObjectType.newObject()
            .name("Task")
            .description("Task of a scheduler job")
            .withInterface(JobTaskCommon.TYPE)
            .field(newFieldDefinition().name("description")
                    .description("description")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("executionDuration")
                    .description("Execution duration of the task")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("executionHostName")
                    .description("Execution host name on which the task is running")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("finishedTime")
                    .description("Finished time")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("genericInformation")
                    .description("Generic information list, empty if there is none")
                    .type(new GraphQLList(GenericInformation.TYPE))
                    .argument(newArgument().name("input")
                            .description("Generic information input filter")
                            .type(new GraphQLList(KeyValueInput.TYPE))
                            .build())
                    .dataFetcher(new GenericInformationDataFetcher()))
            .field(newFieldDefinition().name("id")
                    .description("Unique identifier")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("inErrorTime")
                    .description("In error time")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("jobId")
                    .description("ID of the job which the task belongs to")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("name")
                    .description("Name")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("numberOfExecutionLeft")
                    .description("Number of execution left")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("numberOfExecutionOnFailureLeft")
                    .description("Number of execution on failure left")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("onTaskError")
                    .description("The behaviour applied on Tasks when an error occurs")
                    .type(ON_TASK_ERROR))
            .field(newFieldDefinition().name("progress")
                    .description("Progress status of the task")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("scheduledTime")
                    .description("Scheduled time for running the task")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("startTime")
                    .description("Start time")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("status")
                    .description("Task status")
                    .type(TASK_STATUS_ENUM))
            .field(newFieldDefinition().name("tag")
                    .description("Task tag")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("variables")
                    .description("Variable list, empty if there is none")
                    .type(new GraphQLList(Variable.TYPE))
                    .argument(newArgument().name("input")
                            .description("Variables input filter")
                            .type(new GraphQLList(KeyValueInput.TYPE))
                            .build())
                    .dataFetcher(new VariablesDataFetcher()))
            .build();

    @Builder
    public Task(String description, long executionDuration, String executionHostName, long finishedTime,
            long id, long inErrorTime, Map<String, String> genericInformation, long jobId, String name,
            int numberOfExecutionLeft, int numberOfExecutionOnFailureLeft, String onTaskError, int progress,
            long scheduledTime,
            long startTime, String status, String tag, Map<String, String> variables) {
        super(description, finishedTime, genericInformation, id, inErrorTime, name, onTaskError, startTime,
                variables);

        this.executionDuration = executionDuration;
        this.executionHostName = executionHostName;
        this.jobId = jobId;
        this.numberOfExecutionLeft = numberOfExecutionLeft;
        this.numberOfExecutionOnFailureLeft = numberOfExecutionOnFailureLeft;
        this.progress = progress;
        this.scheduledTime = scheduledTime;
        this.status = status;
        this.tag = tag;
    }

}
