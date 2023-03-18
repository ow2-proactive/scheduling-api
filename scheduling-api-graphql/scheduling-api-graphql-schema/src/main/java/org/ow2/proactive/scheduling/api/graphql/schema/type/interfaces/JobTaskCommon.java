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
package org.ow2.proactive.scheduling.api.graphql.schema.type.interfaces;

import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.DESCRIPTION;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.FINISHED_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.GENERIC_INFORMATION;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.ID;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.IN_ERROR_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.MAX_NUMBER_OF_EXECUTION;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.NAME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.ON_TASK_ERROR;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.START_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.TASK_RETRY_DELAY;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VARIABLES;

import java.util.Map;

import org.ow2.proactive.scheduling.api.graphql.common.Arguments;
import org.ow2.proactive.scheduling.api.graphql.common.Types;
import org.ow2.proactive.scheduling.api.graphql.schema.type.GenericInformation;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Job;
import org.ow2.proactive.scheduling.api.graphql.schema.type.OnTaskError;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Task;
import org.ow2.proactive.scheduling.api.graphql.schema.type.TypeSingleton;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Variable;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.KeyValueInput;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLList;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author ActiveEon team
 */
@NoArgsConstructor
@Data
public abstract class JobTaskCommon {

    public static final TypeSingleton<GraphQLInterfaceType> TYPE = new TypeSingleton<GraphQLInterfaceType>() {
        @Override
        public GraphQLInterfaceType buildType(DataFetcher... dataFetchers) {

            DataFetcher genericInformationDataFetcher = dataFetchers[0];
            DataFetcher variableDataFetcher = dataFetchers[1];

            return GraphQLInterfaceType.newInterface()
                                       .name(Types.JOB_TASK_COMMON.getName())
                                       .field(newFieldDefinition().name(DESCRIPTION.getName())
                                                                  .description("Description.")
                                                                  .type(GraphQLString))
                                       .field(newFieldDefinition().name(FINISHED_TIME.getName())
                                                                  .description("Finished time.")
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
                                                                  .description("Unique identifier.")
                                                                  .type(GraphQLString))
                                       .field(newFieldDefinition().name(IN_ERROR_TIME.getName())
                                                                  .description("In error time.")
                                                                  .type(GraphQLLong))
                                       .field(newFieldDefinition().name(MAX_NUMBER_OF_EXECUTION.getName())
                                                                  .description("The maximum number of execution attempts for the Task(s).")
                                                                  .type(GraphQLInt))
                                       .field(newFieldDefinition().name(NAME.getName())
                                                                  .description("Name.")
                                                                  .type(GraphQLString))
                                       .field(newFieldDefinition().name(ON_TASK_ERROR.getName())
                                                                  .description("The behaviour applied on Tasks when an error occurs.")
                                                                  .type(OnTaskError.TYPE.getInstance()))
                                       .field(newFieldDefinition().name(TASK_RETRY_DELAY.getName())
                                                                  .description("The time (in milliseconds) to wait before restarting the task if an error occurred.")
                                                                  .type(GraphQLLong))
                                       .field(newFieldDefinition().name(START_TIME.getName())
                                                                  .description("Start time.")
                                                                  .type(GraphQLLong))
                                       .field(newFieldDefinition().name(VARIABLES.getName())
                                                                  .description("Variable list, empty if there is none.")
                                                                  .type(new GraphQLList(Variable.TYPE.getInstance()))
                                                                  .argument(newArgument().name(Arguments.FILTER.getName())
                                                                                         .description("Variables input filter.")
                                                                                         .type(new GraphQLList(KeyValueInput.TYPE.getInstance()))
                                                                                         .build())
                                                                  .dataFetcher(variableDataFetcher))
                                       .typeResolver(env -> {
                                           if (env.getObject() instanceof Job) {
                                               return Job.TYPE.getInstance(dataFetchers);
                                           }
                                           return Task.TYPE.getInstance(dataFetchers);
                                       })
                                       .build();
        }
    };

    private String description;

    private long finishedTime = -1;

    private Map<String, String> genericInformation;

    private long id;

    private long inErrorTime = -1;

    private int maxNumberOfExecution = -1;

    private String name;

    private String onTaskError;

    private Long taskRetryDelay;

    private long startTime = -1;

    private Map<String, String> variables;

    public JobTaskCommon(String description, long finishedTime, Map<String, String> genericInformation, long id,
            long inErrorTime, int maxNumberOfExecution, String name, String onTaskError, Long taskRetryDelay,
            long startTime, Map<String, String> variables) {

        this.description = description;
        this.finishedTime = finishedTime;
        this.genericInformation = genericInformation;
        this.id = id;
        this.inErrorTime = inErrorTime;
        this.maxNumberOfExecution = maxNumberOfExecution;
        this.name = name;
        this.onTaskError = onTaskError;
        this.taskRetryDelay = taskRetryDelay;
        this.startTime = startTime;
        this.variables = variables;
    }

}
