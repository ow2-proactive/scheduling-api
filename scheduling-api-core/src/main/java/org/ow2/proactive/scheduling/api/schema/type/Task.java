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
import org.ow2.proactive.scheduling.api.fetchers.VariablesDataFetcher;
import org.ow2.proactive.scheduling.api.schema.type.inputs.KeyValueInput;
import org.ow2.proactive.scheduling.api.schema.type.interfaces.JobTaskCommon;
import org.ow2.proactive.scheduling.api.util.Constants;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static graphql.Scalars.GraphQLBoolean;
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

    private boolean runAsMe;

    private long scheduledTime = -1;

    private String status;

    private String tag;

    private long walltime = -1;

    private String workingDir;

    public final static GraphQLEnumType TASK_RESTART_MODE_ENUM = newEnum().name("RestartMode")
            .description("The restart mode configured for the Task if an error occurs during its execution.")
            .value("ANYWHERE",
                    "ANYWHERE",
                    "The task is restarted on any available node " +
                            "(possibly the same as the one where the error occurred).")
            .value("ELSEWHERE",
                    "ELSEWHERE",
                    "The task is restarted on a node that is different " +
                            "from the node where the error occurred.").build();

    public final static GraphQLEnumType TASK_STATUS_ENUM = newEnum().name("TaskStatus")
            .description("Task status list")
            .value("ABORTED",
                    "ABORTED",
                    "The task has been aborted by an exceptions on an other task while the task is running (job has cancelOnError=true). Can be also in this status if the job is killed while the concerned task was running.")
            .value("FAILED",
                    "FAILED",
                    "The task is failed (only if max execution time has been reached and the node on which it was started is down).")
            .value("FAULTY",
                    "FAULTY",
                    "The task has finished execution with error code (!=0) or exceptions.")
            .value("FINISHED",
                    "FINISHED",
                    "The task has finished execution.")
            .value("IN_ERROR",
                    "IN_ERROR",
                    "The task is suspended after first error and is waiting for a manual restart action.")
            .value("NOT_RESTARTED",
                    "NOT_RESTARTED",
                    "The task could not be restarted. It means that the task could not be restarted after an error during the previous execution.")
            .value("NOT_STARTED",
                    "NOT_STARTED",
                    "The task could not be started. It means that the task could not be started due to one ore more dependency failure.")
            .value("PAUSED", "PAUSED", "The task is paused")
            .value("PENDING",
                    "PENDING",
                    "The task is in the scheduler pending queue.")
            .value("RUNNING",
                    "RUNNING",
                    "The task is executing.")
            .value("SKIPPED",
                    "SKIPPED",
                    "The task was not executed: it was the non-selected branch of an IF/ELSE control flow action.")
            .value("SUBMITTED",
                    "SUBMITTED",
                    "The task has just been submitted by the user.")
            .value("WAITING_ON_ERROR",
                    "WAITING_ON_ERROR",
                    "The task is waiting for restart after an error (i.e. native code != 0 or exceptions).")
            .value("WAITING_ON_FAILURE",
                    "WAITING_ON_FAILURE",
                    "The task is waiting for restart after a failure (i.e. node down).")
            .build();

    public final static GraphQLObjectType TYPE = GraphQLObjectType.newObject()
            .name("Task")
            .description("Task managed by a ProActive Scheduler instance. " +
                    "A task is a unit of work that is executed on a ProActive node.")
            .withInterface(JobTaskCommon.TYPE)
            .field(newFieldDefinition().name("additionalClasspath")
                    .description("The list of \"pathElement\" representing " +
                            "the classpath to be added when starting the new JVM.")
                    .type(new GraphQLList(GraphQLString)))
            .field(newFieldDefinition().name("description")
                    .description("A sentence that describes the purpose of the Task.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("executionDuration")
                    .description("Execution duration of the Task in milliseconds.")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("executionHostName")
                    .description("Execution host name on which the Task is running.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("finishedTime")
                    .description("The time at which the Task has finished (timestamp).")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("genericInformation")
                    .description("Generic information list, empty if there is none.")
                    .type(new GraphQLList(GenericInformation.TYPE))
                    .argument(newArgument().name(Constants.ARGUMENT_NAME_FILTER)
                            .description("Generic information input filter")
                            .type(new GraphQLList(KeyValueInput.TYPE))
                            .build())
                    .dataFetcher(new GenericInformationDataFetcher()))
            .field(newFieldDefinition().name("id")
                    .description("The unique identifier of the Task for a given Job.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("inErrorTime")
                    .description(
                            "The timestamp that depicts the time at which the Task was maked in-error for the last time.")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("javaHome")
                    .description("Java installation directory on the node side.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("jobId")
                    .description("The identifier the Job which the Task belongs to.")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("jvmArguments")
                    .description("JVM arguments")
                    .type(new GraphQLList(GraphQLString)))
            .field(newFieldDefinition().name("maxNumberOfExecution")
                    .description(
                            "The maximum number of execution attempts for the Task.")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("name")
                    .description("The name of the Task. Please note that Task names are unique per Job.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("numberOfExecutionLeft")
                    .description("Number of execution left.")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("numberOfExecutionOnFailureLeft")
                    .description("Number of execution on failure left.")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("onTaskError")
                    .description("The behaviour applied on Tasks when an error occurs.")
                    .type(ON_TASK_ERROR))
            .field(newFieldDefinition().name("preciousLogs")
                    .description(
                            "If the value is `true`, then it means that full task logs are kept.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("preciousResult")
                    .description("If the value is `true`, then it means the result of the Task " +
                            "is saved in the Job result.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("restartMode")
                    .type(TASK_RESTART_MODE_ENUM))
            .field(newFieldDefinition().name("resultPreview")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("runAsMe")
                    .description(
                            "If `true`, if means that the Task script or command is executed with " +
                                    "the system account associated to the current Job owner value.")
                    .type(GraphQLBoolean))
            .field(newFieldDefinition().name("scheduledTime")
                    .description("Scheduled time for running the task.")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("startTime")
                    .description("Start time.")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("status")
                    .description("Task status.")
                    .type(TASK_STATUS_ENUM))
            .field(newFieldDefinition().name("tag")
                    .description("Task tag.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("variables")
                    .description("Variable list, empty if there is none.")
                    .type(new GraphQLList(Variable.TYPE))
                    .argument(newArgument().name(Constants.ARGUMENT_NAME_FILTER)
                            .description("Variables input filter.")
                            .type(new GraphQLList(KeyValueInput.TYPE))
                            .build())
                    .dataFetcher(new VariablesDataFetcher()))
            .field(newFieldDefinition().name("walltime")
                    .description("Maximum execution time allowed for the Task (in milliseconds).")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("workingDir")
                    .description("Working folder of the Task that is executed on a ProActive node.")
                    .type(GraphQLString))
            .build();

    @Builder
    public Task(List<String> additionalClasspath, String description, long executionDuration,
            String executionHostname, long finishedTime, long id, long inErrorTime,
            Map<String, String> genericInformation, String javaHome, long jobId, List<String> jvmArguments,
            int maxNumberOfExecution, String name, int numberOfExecutionLeft,
            int numberOfExecutionOnFailureLeft, String onTaskError, boolean preciousLogs,
            boolean preciousResult, String restartMode, String resultPreview, boolean runAsMe,
            long scheduledTime, long startTime, String status, String tag, Map<String, String> variables,
            long walltime, String workingDir) {

        super(description, finishedTime, genericInformation, id, inErrorTime, maxNumberOfExecution, name,
                onTaskError, startTime, variables);

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
        this.runAsMe = runAsMe;
        this.scheduledTime = scheduledTime;
        this.status = status;
        this.tag = tag;
        this.walltime = walltime;
        this.workingDir = workingDir;
    }

}
