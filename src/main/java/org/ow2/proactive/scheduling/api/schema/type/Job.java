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

    private long submittedTime;

    private List<Task> tasks;

    private int totalNumberOfTasks;

    public final static GraphQLEnumType JOB_PRIORITY_ENUM = newEnum().name("JobPriority")
            .description("Job's priority list")
            .value("HIGH", "HIGH", "High priority")
            .value("HIGHEST", "HIGHEST", "Highest priority")
            .value("IDLE", "IDLE", "Idle")
            .value("LOW", "LOW", "Low priority")
            .value("LOWEST", "LOWEST", "Lowest priority")
            .value("NORMAL", "NORMAL", "Normal priority.")
            .build();

    public final static GraphQLObjectType TYPE = GraphQLObjectType.newObject()
            .name("Job")
            .description("Scheduler job")
            .withInterface(JobTaskCommon.TYPE)
            .field(newFieldDefinition().name("description")
                    .description("description")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("finishedTime")
                    .description("Finished time")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("genericInformation")
                    .description("Generic information list, empty if there is none")
                    .type(new GraphQLList(GenericInformation.TYPE))
                    .argument(newArgument().name("input")
                            .description("Generic information input filter")
                            .type(KeyValueInput.TYPE)
                            .build())
                    .dataFetcher(new GenericInformationDataFetcher()))
            .field(newFieldDefinition().name("id")
                    .description("Unique identifier")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("inErrorTime")
                    .description("In error time")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("name")
                    .description("Name")
                    .type(GraphQLString))

            .field(newFieldDefinition().name("numberOfFailedTasks")
                    .description("Number of the failed tasks of the job")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("numberOfFaultyTasks")
                    .description("Number of faulty tasks of the job")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("numberOfFinishedTasks")
                    .description("Number of the finished tasks of the job")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("numberOfInErrorTasks")
                    .description("Number of the in error tasks of the job")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("numberOfPendingTasks")
                    .description("Number of the pending tasks of the job")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("numberOfRunningTasks")
                    .description("Number of the running tasks of the job")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("owner")
                    .description("Job's owner")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("priority")
                    .description("Job priority")
                    .type(JOB_PRIORITY_ENUM))
            .field(newFieldDefinition().name("projectName")
                    .description("Project name which the job belongs to")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("removedTime")
                    .description("Job removed time")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("startTime")
                    .description("Start time")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("submittedTime")
                    .description("Job submitted time")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("tasks")
                    .description("Task list of the job, empty if there is none")
                    .type(TasksConnection.TYPE)
                    .argument(newArgument().name("input")
                            .description("task filter input")
                            .type(TaskInput.TYPE)
                            .build())
                    .argument(TasksConnection.getConnectionFieldArguments())
                    .dataFetcher(new TaskDataFetcher()))
            .field(newFieldDefinition().name("totalNumberOfTasks")
                    .description("Total number of tasks of the job")
                    .type(GraphQLInt))
            .field(newFieldDefinition().name("variables")
                    .description("Variable list, empty if there is none")
                    .type(new GraphQLList(Variable.TYPE))
                    .argument(newArgument().name("input")
                            .description("Variables input filter")
                            .type(KeyValueInput.TYPE)
                            .build())
                    .dataFetcher(new VariablesDataFetcher()))
            .build();

    @Builder
    public Job(String description, long finishedTime, Map<String, String> genericInformation, long id,
            long inErrorTime,
            String name, int numberOfFailedTasks, int numberOfFaultyTasks, int numberOfFinishedTasks,
            int numberOfInErrorTasks, int numberOfPendingTasks, int numberOfRunningTasks, String owner,
            String priority, String projectName, long removedTime, long submittedTime, List<Task> tasks,
            int totalNumberOfTasks, long startTime, Map<String, String> variables) {

        super(description, finishedTime, genericInformation, id, inErrorTime, name, startTime, variables);

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
        this.submittedTime = submittedTime;
        this.tasks = tasks;
        this.totalNumberOfTasks = totalNumberOfTasks;

    }

}
