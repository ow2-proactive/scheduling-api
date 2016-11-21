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

import java.util.List;
import java.util.Map;

import org.ow2.proactive.scheduling.api.fetchers.GenericInformationDataFetcher;
import org.ow2.proactive.scheduling.api.fetchers.TaskDataFetcher;
import org.ow2.proactive.scheduling.api.fetchers.VariablesDataFetcher;
import org.ow2.proactive.scheduling.api.schema.type.inputs.KeyValueInput;
import org.ow2.proactive.scheduling.api.schema.type.inputs.TaskInput;
import org.ow2.proactive.scheduling.api.schema.type.interfaces.JobTaskCommon;
import org.ow2.proactive.scheduling.api.services.ApplicationContextProvider;
import org.ow2.proactive.scheduling.api.util.Constants;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class Job extends JobTaskCommon {

    private DataManagement dataManagement;

    private int numberOfFailedTasks;

    private int numberOfFaultyTasks;

    private int numberOfFinishedTasks;

    private int numberOfInErrorTasks;

    private int numberOfPendingTasks;

    private int numberOfRunningTasks;

    private String owner;

    private String priority;

    private String projectName;

    private long removedTime;

    private String status;

    private long submittedTime;

    private List<Task> tasks;

    private int totalNumberOfTasks;

    public final static GraphQLEnumType JOB_PRIORITY_ENUM = newEnum().name("JobPriority")
            .description("Available job's priorities")
            .value("HIGH", "HIGH", "High priority")
            .value("HIGHEST", "HIGHEST", "Highest priority")
            .value("IDLE", "IDLE", "Idle")
            .value("LOW", "LOW", "Low priority")
            .value("LOWEST", "LOWEST", "Lowest priority")
            .value("NORMAL", "NORMAL", "Normal priority")
            .build();

    public final static GraphQLEnumType JOB_STATUS_ENUM = newEnum().name("JobStatus")
            .description("Available job's statuses")
            .value("CANCELED", "CANCELED", "The job has been canceled due to user exceptions and order. " +
                    "This status runs when a user exceptions occurs in a task and when the user has asked " +
                    "to cancel On exceptions.")
            .value("FAILED", "FAILED",
                    "The job has failed. One or more tasks have failed (due to resources " +
                            "failure). There is no more executionOnFailure left for a task.")
            .value("FINISHED", "FINISHED", "The job is finished. Every tasks are finished.")
            .value("IN_ERROR", "IN_ERROR", "The job has at least one in-error task and in-error tasks are " +
                    "the last, among others which have changed their state (i.e. Job status is depicted " +
                    "by the last action).")
            .value("KILLED", "KILLED", "The job has been killed by a user. Nothing can be done anymore on " +
                    "this job except reading execution information such as output, time, etc.")
            .value("PAUSED", "PAUSED", "The job is paused waiting for user to resume it.")
            .value("PENDING", "PENDING", "The job is waiting to be scheduled.")
            .value("RUNNING", "RUNNING", "The job is running. Actually at least one of its task " +
                    "has been scheduled.")
            .value("STALLED", "STALLED", "The job has been launched but no task are currently running")
            .build();

    public final static GraphQLObjectType TYPE = GraphQLObjectType.newObject()
            .name("Job")
            .description("Job managed by a ProActive Scheduler instance. A Job is made of one or more Tasks.")
            .withInterface(JobTaskCommon.TYPE)
            .field(newFieldDefinition().name("dataManagement")
                    .description("User configuration for data spaces.")
                    .type(DataManagement.TYPE))
            .field(newFieldDefinition().name("description")
                    .description("The description of the job.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("finishedTime")
                    .description("The timestamp at which the Job has finished its execution.")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("genericInformation")
                    .description("Generic information list, empty if there is none.")
                    .type(new GraphQLList(GenericInformation.TYPE))
                    .argument(newArgument().name("input")
                            .description("Generic information input filter.")
                            .type(new GraphQLList(KeyValueInput.TYPE))
                            .build())
                    .dataFetcher(new GenericInformationDataFetcher()))
            .field(newFieldDefinition().name("id")
                    .description("The unique identifier of the Job.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("inErrorTime")
                    .description("A timestamp that depicts the time at which the Job was marked in-error for the last time.")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("maxNumberOfExecution")
                    .description(
                            "The maximum number of execution attempts for the Tasks associated to this Job." +
                                    " The value may be redefined at the Task level.")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("name")
                    .description("Job's name.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("numberOfFailedTasks")
                    .description("Number of failed Tasks contained in the Job.")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("numberOfFaultyTasks")
                    .description("Number of faulty Tasks contained in Job.")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("numberOfFinishedTasks")
                    .description("Number of finished Tasks contained in Job.")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("numberOfInErrorTasks")
                    .description("Number of in-error Tasks contained in the Job.")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("numberOfPendingTasks")
                    .description("Number of pending Tasks contained in the Job.")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("numberOfRunningTasks")
                    .description("Number of running Tasks contained in the Job.")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("onTaskError")
                    .description("The behaviour applied on Tasks when an error occurs.")
                    .type(ON_TASK_ERROR))
            .field(newFieldDefinition().name("owner")
                    .description("Job's owner.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("priority")
                    .description("Job priority.")
                    .type(JOB_PRIORITY_ENUM))
            .field(newFieldDefinition().name("projectName")
                    .description("Project name which the job belongs to.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("removedTime")
                    .description("Job removed time.")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("startTime")
                    .description("Start time.")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("status")
                    .description("Scheduling status of a job.")
                    .type(JOB_STATUS_ENUM))
            .field(newFieldDefinition().name("submittedTime")
                    .description("Job submitted time.")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("tasks")
                    .description("Task list of the job, empty if there is none.")
                    .type(TasksConnection.TYPE)
                    .argument(newArgument().name("input")
                            .description("Tasks input filter.")
                            .type(new GraphQLList(TaskInput.TYPE))
                            .build())
                    .argument(TasksConnection.getConnectionFieldArguments())
                    .argument(newArgument()
                            .name("first")
                            .type(GraphQLInt).defaultValue(Constants.PAGINATION_DEFAULT_SIZE)
                            .build())
                    .dataFetcher(new TaskDataFetcher()))
            .field(newFieldDefinition().name("totalNumberOfTasks")
                    .description("Total number of Tasks of the Job.")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("variables")
                    .description("Variable list, empty if there is none.")
                    .type(new GraphQLList(Variable.TYPE))
                    .argument(newArgument().name("input")
                            .description("Variables input filter.")
                            .type(new GraphQLList(KeyValueInput.TYPE))
                            .build())
                    .dataFetcher(new VariablesDataFetcher()))
            .build();

    @Builder
    public Job(DataManagement dataManagement, String description, long finishedTime,
            Map<String, String> genericInformation, long id, long inErrorTime, int maxNumberOfExecution,
            String name, int numberOfFailedTasks, int numberOfFaultyTasks, int numberOfFinishedTasks,
            int numberOfInErrorTasks, int numberOfPendingTasks, int numberOfRunningTasks, String onTaskError,
            String owner, String priority, String projectName, long removedTime, long startTime,
            String status, long submittedTime, List<Task> tasks, int totalNumberOfTasks,
            Map<String, String> variables) {

        super(description, finishedTime, genericInformation,
                id, inErrorTime, maxNumberOfExecution, name, onTaskError, startTime, variables);

        this.dataManagement = dataManagement;
        this.numberOfFailedTasks = numberOfFailedTasks;
        this.numberOfFaultyTasks = numberOfFaultyTasks;
        this.numberOfFinishedTasks = numberOfFinishedTasks;
        this.numberOfInErrorTasks = numberOfInErrorTasks;
        this.numberOfPendingTasks = numberOfPendingTasks;
        this.numberOfRunningTasks = numberOfRunningTasks;
        this.owner = owner;
        this.priority = priority;
        this.projectName = projectName;
        this.removedTime = removedTime;
        this.status = status;
        this.submittedTime = submittedTime;
        this.tasks = tasks;
        this.totalNumberOfTasks = totalNumberOfTasks;
    }

}
