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
package org.ow2.proactive.scheduling.api.graphql.schema.type;

import java.util.List;
import java.util.Map;

import org.ow2.proactive.scheduling.api.graphql.common.Arguments;
import org.ow2.proactive.scheduling.api.graphql.common.DefaultValues;
import org.ow2.proactive.scheduling.api.graphql.common.Types;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.KeyValueInput;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.TaskInput;
import org.ow2.proactive.scheduling.api.graphql.schema.type.interfaces.JobTaskCommon;
import graphql.schema.DataFetcher;
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
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.FILTER;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.*;


/**
 * @author ActiveEon Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class Job extends JobTaskCommon {

    public final static TypeSingleton<GraphQLObjectType> TYPE = new TypeSingleton<GraphQLObjectType>() {
        @Override
        public GraphQLObjectType buildType(DataFetcher... dataFetchers) {

            DataFetcher genericInformationDataFetcher = dataFetchers[0];
            DataFetcher taskDataFetcher = dataFetchers[1];
            DataFetcher variableDataFetcher = dataFetchers[2];

            return GraphQLObjectType.newObject()
                    .name(Types.JOB.getName())
                    .description(
                            "Job managed by a ProActive Scheduler instance. A Job is made of one or more Tasks.")
                    .withInterface(JobTaskCommon.TYPE.getInstance(genericInformationDataFetcher,
                            variableDataFetcher))
                    .field(newFieldDefinition().name(DATA_MANAGEMENT.getName())
                            .description("User configuration for data spaces.")
                            .type(DataManagement.TYPE.getInstance()))
                    .field(newFieldDefinition().name(DESCRIPTION.getName())
                            .description("The description of the job.")
                            .type(GraphQLString))
                    .field(newFieldDefinition().name(FINISHED_TIME.getName())
                            .description("The timestamp at which the Job has finished its execution.")
                            .type(GraphQLLong))
                    .field(newFieldDefinition().name(GENERIC_INFORMATION.getName())
                            .description("Generic information list, empty if there is none.")
                            .type(new GraphQLList(GenericInformation.TYPE.getInstance(dataFetchers)))
                            .argument(newArgument().name(FILTER.getName())
                                    .description("Generic information input filter.")
                                    .type(new GraphQLList(KeyValueInput.TYPE.getInstance()))
                                    .build())
                            .dataFetcher(genericInformationDataFetcher))
                    .field(newFieldDefinition().name(ID.getName())
                            .description("The unique identifier of the Job.")
                            .type(GraphQLString))
                    .field(newFieldDefinition().name(IN_ERROR_TIME.getName())
                            .description(
                                    "A timestamp that depicts the time at which the Job was marked in-error for the last time.")
                            .type(GraphQLLong))
                    .field(newFieldDefinition().name(MAX_NUMBER_OF_EXECUTION.getName())
                            .description(
                                    "The maximum number of execution attempts for the Tasks associated to this Job." +
                                            " The value may be redefined at the Task level.")
                            .type(GraphQLInt))
                    .field(newFieldDefinition().name(NAME.getName())
                            .description("Job's name.")
                            .type(GraphQLString))
                    .field(newFieldDefinition().name(NUMBER_OF_FAILED_TASKS.getName())
                            .description("Number of failed Tasks contained in the Job.")
                            .type(GraphQLInt))
                    .field(newFieldDefinition().name(NUMBER_OF_FAULTY_TASKS.getName())
                            .description("Number of faulty Tasks contained in Job.")
                            .type(GraphQLInt))
                    .field(newFieldDefinition().name(NUMBER_OF_FINISHED_TASKS.getName())
                            .description("Number of finished Tasks contained in Job.")
                            .type(GraphQLInt))
                    .field(newFieldDefinition().name(NUMBER_OF_IN_ERROR_TASKS.getName())
                            .description("Number of in-error Tasks contained in the Job.")
                            .type(GraphQLInt))
                    .field(newFieldDefinition().name(NUMBER_OF_PENDING_TASKS.getName())
                            .description("Number of pending Tasks contained in the Job.")
                            .type(GraphQLInt))
                    .field(newFieldDefinition().name(NUMBER_OF_RUNNING_TASKS.getName())
                            .description("Number of running Tasks contained in the Job.")
                            .type(GraphQLInt))
                    .field(newFieldDefinition().name(ON_TASK_ERROR.getName())
                            .description("The behaviour applied on Tasks when an error occurs.")
                            .type(OnTaskError.TYPE.getInstance()))
                    .field(newFieldDefinition().name(OWNER.getName())
                            .description("Job's owner.")
                            .type(GraphQLString))
                    .field(newFieldDefinition().name(PRIORITY.getName())
                            .description("Job priority.")
                            .type(JobPriority.TYPE.getInstance()))
                    .field(newFieldDefinition().name(PROJECT_NAME.getName())
                            .description("Project name which the job belongs to.")
                            .type(GraphQLString))
                    .field(newFieldDefinition().name(REMOVED_TIME.getName())
                            .description("Job removed time.")
                            .type(GraphQLLong))
                    .field(newFieldDefinition().name(START_TIME.getName())
                            .description("Start time.")
                            .type(GraphQLLong))
                    .field(newFieldDefinition().name(STATUS.getName())
                            .description("Scheduling status of a job.")
                            .type(JobStatus.TYPE.getInstance()))
                    .field(newFieldDefinition().name(SUBMITTED_TIME.getName())
                            .description("Job submitted time.")
                            .type(GraphQLLong))
                    .field(newFieldDefinition().name(TASKS.getName())
                            .description("Task list of the job, empty if there is none.")
                            .type(TaskConnection.TYPE.getInstance(
                                    genericInformationDataFetcher, variableDataFetcher))
                            .argument(newArgument().name(Arguments.FILTER.getName())
                                    .description("Tasks input filter.")
                                    .type(new GraphQLList(TaskInput.TYPE.getInstance()))
                                    .build())
                            .argument(TaskConnection.getConnectionFieldArguments())
                            .argument(newArgument()
                                    .name(Arguments.FIRST.getName())
                                    .type(GraphQLInt).defaultValue(DefaultValues.PAGE_SIZE)
                                    .build())
                            .dataFetcher(taskDataFetcher))
                    .field(newFieldDefinition().name(TOTAL_NUMBER_OF_TASKS.getName())
                            .description("Total number of Tasks of the Job.")
                            .type(GraphQLInt))
                    .field(newFieldDefinition().name(VARIABLES.getName())
                            .description("Variable list, empty if there is none.")
                            .type(new GraphQLList(Variable.TYPE.getInstance()))
                            .argument(newArgument().name(Arguments.FILTER.getName())
                                    .description("Variables input filter.")
                                    .type(new GraphQLList(KeyValueInput.TYPE.getInstance()))
                                    .build())
                            .dataFetcher(variableDataFetcher))
                    .build();
        }
    };

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
