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
package org.ow2.proactive.scheduling.api.graphql.schema.type;

import static graphql.Scalars.GraphQLBoolean;
import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.ADDITIONAL_CLASSPATH;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.DESCRIPTION;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.EXECUTION_DURATION;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.EXECUTION_HOST_NAME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.FINISHED_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.FORK;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.GENERIC_INFORMATION;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.ID;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.IN_ERROR_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.JAVA_HOME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.JOB_ID;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.JVM_ARGUMENTS;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.MAX_NUMBER_OF_EXECUTION;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.NAME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.NUMBER_OF_EXECUTION_LEFT;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.NUMBER_OF_EXECUTION_ON_FAILURE_LEFT;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.ON_TASK_ERROR;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.PRECIOUS_LOGS;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.PRECIOUS_RESULT;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.RESTART_MODE;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.RESULT_PREVIEW;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.RUN_AS_ME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.SCHEDULED_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.START_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.STATUS;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.TAG;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.TASK_RETRY_DELAY;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VARIABLES;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.WALLTIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.WORKING_DIR;

import java.util.List;
import java.util.Map;

import org.ow2.proactive.scheduling.api.graphql.common.Arguments;
import org.ow2.proactive.scheduling.api.graphql.common.Types;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.KeyValueInput;
import org.ow2.proactive.scheduling.api.graphql.schema.type.interfaces.JobTaskCommon;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


/**
 * @author ActiveEon Team
 */
@Getter
@ToString
public class Task extends JobTaskCommon {

    public final static TypeSingleton<GraphQLObjectType> TYPE = new TypeSingleton<GraphQLObjectType>() {
        @Override
        public GraphQLObjectType buildType(DataFetcher... dataFetchers) {

            DataFetcher genericInformationDataFetcher = dataFetchers[0];
            DataFetcher variableDataFetcher = dataFetchers[1];

            return GraphQLObjectType.newObject()
                                    .name(Types.TASK.getName())
                                    .description("Task managed by a ProActive Scheduler instance. " +
                                                 "A task is a unit of work that is executed on a ProActive node.")
                                    .withInterface(JobTaskCommon.TYPE.getInstance(genericInformationDataFetcher,
                                                                                  variableDataFetcher))
                                    .field(newFieldDefinition().name(ADDITIONAL_CLASSPATH.getName())
                                                               .description("The list of \"pathElement\" representing " +
                                                                            "the classpath to be added when starting the new JVM.")
                                                               .type(new GraphQLList(GraphQLString)))
                                    .field(newFieldDefinition().name(DESCRIPTION.getName())
                                                               .description("A sentence that describes the purpose of the Task.")
                                                               .type(GraphQLString))
                                    .field(newFieldDefinition().name(EXECUTION_DURATION.getName())
                                                               .description("Execution duration of the Task in milliseconds.")
                                                               .type(GraphQLLong))
                                    .field(newFieldDefinition().name(EXECUTION_HOST_NAME.getName())
                                                               .description("Execution host name on which the Task is running.")
                                                               .type(GraphQLString))
                                    .field(newFieldDefinition().name(FINISHED_TIME.getName())
                                                               .description("The time at which the Task has finished (timestamp).")
                                                               .type(GraphQLLong))
                                    .field(newFieldDefinition().name(GENERIC_INFORMATION.getName())
                                                               .description("Generic information list, empty if there is none.")
                                                               .type(new GraphQLList(GenericInformation.TYPE.getInstance()))
                                                               .argument(newArgument().name(Arguments.FILTER.getName())
                                                                                      .description("Generic information input filter")
                                                                                      .type(new GraphQLList(KeyValueInput.TYPE.getInstance()))
                                                                                      .build())
                                                               .dataFetcher(genericInformationDataFetcher))
                                    .field(newFieldDefinition().name(ID.getName())
                                                               .description("The unique identifier of the Task for a given Job.")
                                                               .type(GraphQLString))
                                    .field(newFieldDefinition().name(IN_ERROR_TIME.getName())
                                                               .description("The timestamp that depicts the time at which the Task was maked in-error for the last time.")
                                                               .type(GraphQLLong))
                                    .field(newFieldDefinition().name(JAVA_HOME.getName())
                                                               .description("Java installation directory on the node side.")
                                                               .type(GraphQLString))
                                    .field(newFieldDefinition().name(JOB_ID.getName())
                                                               .description("The identifier the Job which the Task belongs to.")
                                                               .type(GraphQLLong))
                                    .field(newFieldDefinition().name(JVM_ARGUMENTS.getName())
                                                               .description("JVM arguments")
                                                               .type(new GraphQLList(GraphQLString)))
                                    .field(newFieldDefinition().name(MAX_NUMBER_OF_EXECUTION.getName())
                                                               .description("The maximum number of execution attempts for the Task.")
                                                               .type(GraphQLInt))
                                    .field(newFieldDefinition().name(NAME.getName())
                                                               .description("The name of the Task. Please note that Task names are unique per Job.")
                                                               .type(GraphQLString))
                                    .field(newFieldDefinition().name(NUMBER_OF_EXECUTION_LEFT.getName())
                                                               .description("Number of execution left.")
                                                               .type(GraphQLInt))
                                    .field(newFieldDefinition().name(NUMBER_OF_EXECUTION_ON_FAILURE_LEFT.getName())
                                                               .description("Number of execution on failure left.")
                                                               .type(GraphQLInt))
                                    .field(newFieldDefinition().name(ON_TASK_ERROR.getName())
                                                               .description("The behaviour applied on Tasks when an error occurs.")
                                                               .type(OnTaskError.TYPE.getInstance()))
                                    .field(newFieldDefinition().name(TASK_RETRY_DELAY.getName())
                                                               .description("The time (in milliseconds) to wait before restarting the task if an error occurred.")
                                                               .type(GraphQLLong))
                                    .field(newFieldDefinition().name(PRECIOUS_LOGS.getName())
                                                               .description("If the value is `true`, then it means that full task logs are kept.")
                                                               .type(GraphQLString))
                                    .field(newFieldDefinition().name(PRECIOUS_RESULT.getName())
                                                               .description("If the value is `true`, then it means the result of the Task " +
                                                                            "is saved in the Job result.")
                                                               .type(GraphQLString))
                                    .field(newFieldDefinition().name(RESTART_MODE.getName())
                                                               .type(RestartMode.TYPE.getInstance()))
                                    .field(newFieldDefinition().name(RESULT_PREVIEW.getName()).type(GraphQLString))
                                    .field(newFieldDefinition().name(FORK.getName())
                                                               .description("If `true`, it means the task is executed in a forked JVM; if `false`, it means the task is executed in the node's JVM")
                                                               .type(GraphQLBoolean))
                                    .field(newFieldDefinition().name(RUN_AS_ME.getName())
                                                               .description("If `true`, it means that the task script or command is executed with " +
                                                                            "the system account associated to the current Job owner value.")
                                                               .type(GraphQLBoolean))
                                    .field(newFieldDefinition().name(SCHEDULED_TIME.getName())
                                                               .description("Scheduled time for running the task.")
                                                               .type(GraphQLLong))
                                    .field(newFieldDefinition().name(START_TIME.getName())
                                                               .description("Start time.")
                                                               .type(GraphQLLong))
                                    .field(newFieldDefinition().name(STATUS.getName())
                                                               .description("Task status.")
                                                               .type(TaskStatus.TYPE.getInstance()))
                                    .field(newFieldDefinition().name(TAG.getName())
                                                               .description("Task tag.")
                                                               .type(GraphQLString))
                                    .field(newFieldDefinition().name(VARIABLES.getName())
                                                               .description("Variable list, empty if there is none.")
                                                               .type(new GraphQLList(Variable.TYPE.getInstance()))
                                                               .argument(newArgument().name(Arguments.FILTER.getName())
                                                                                      .description("Variables input filter.")
                                                                                      .type(new GraphQLList(KeyValueInput.TYPE.getInstance()))
                                                                                      .build())
                                                               .dataFetcher(variableDataFetcher))
                                    .field(newFieldDefinition().name(WALLTIME.getName())
                                                               .description("Maximum execution time allowed for the Task (in milliseconds).")
                                                               .type(GraphQLLong))
                                    .field(newFieldDefinition().name(WORKING_DIR.getName())
                                                               .description("Working folder of the Task that is executed on a ProActive node.")
                                                               .type(GraphQLString))
                                    .build();
        }
    };

    private List<String> additionalClasspath;

    private long executionDuration = -1;

    private String executionHostname;

    private String javaHome;

    private long jobId;

    private List<String> jvmArguments;

    private int numberOfExecutionLeft = 1;

    private int numberOfExecutionOnFailureLeft = 1;

    private boolean preciousLogs;

    private boolean preciousResult;

    private String restartMode;

    private String resultPreview;

    private boolean fork;

    private boolean runAsMe;

    private long scheduledTime = -1;

    private String status;

    private String tag;

    private long walltime = -1;

    private String workingDir;

    @Builder
    public Task(List<String> additionalClasspath, String description, long executionDuration, String executionHostname,
            long finishedTime, long id, long inErrorTime, Map<String, String> genericInformation, String javaHome,
            long jobId, List<String> jvmArguments, int maxNumberOfExecution, String name, int numberOfExecutionLeft,
            int numberOfExecutionOnFailureLeft, String onTaskError, Long taskRetryDelay, boolean preciousLogs,
            boolean preciousResult, String restartMode, String resultPreview, boolean fork, boolean runAsMe,
            long scheduledTime, long startTime, String status, String tag, Map<String, String> variables, long walltime,
            String workingDir) {

        super(description,
              finishedTime,
              genericInformation,
              id,
              inErrorTime,
              maxNumberOfExecution,
              name,
              onTaskError,
              taskRetryDelay,
              startTime,
              variables);

        this.additionalClasspath = additionalClasspath;
        this.executionDuration = executionDuration;
        this.executionHostname = executionHostname;
        this.javaHome = javaHome;
        this.jobId = jobId;
        this.jvmArguments = jvmArguments;
        this.numberOfExecutionLeft = numberOfExecutionLeft;
        this.numberOfExecutionOnFailureLeft = numberOfExecutionOnFailureLeft;
        this.preciousLogs = preciousLogs;
        this.preciousResult = preciousResult;
        this.restartMode = restartMode;
        this.resultPreview = resultPreview;
        this.fork = fork;
        this.runAsMe = runAsMe;
        this.scheduledTime = scheduledTime;
        this.status = status;
        this.tag = tag;
        this.walltime = walltime;
        this.workingDir = workingDir;
    }

}
