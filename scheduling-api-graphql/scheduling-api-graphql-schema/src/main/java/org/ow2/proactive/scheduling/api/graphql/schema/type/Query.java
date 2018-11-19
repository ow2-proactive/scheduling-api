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

import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.JOBS;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VERSION;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VIEWER;

import org.ow2.proactive.scheduling.api.graphql.common.Arguments;
import org.ow2.proactive.scheduling.api.graphql.common.DefaultValues;
import org.ow2.proactive.scheduling.api.graphql.common.Types;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.JobInput;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import graphql.schema.StaticDataFetcher;


/**
 * @author ActiveEon Team
 */
public final class Query {

    public static final TypeSingleton<GraphQLObjectType> TYPE = new TypeSingleton<GraphQLObjectType>() {
        @Override
        public GraphQLObjectType buildType(DataFetcher... dataFetchers) {

            DataFetcher genericInformationDataFetcher = dataFetchers[0];
            DataFetcher jobDataFetcher = dataFetchers[1];
            DataFetcher taskDataFetcher = dataFetchers[2];
            DataFetcher userDataFetcher = dataFetchers[3];
            DataFetcher variableDataFetcher = dataFetchers[4];
            DataFetcher resultMapDataFetcher = dataFetchers[5];

            return GraphQLObjectType.newObject()
                                    .name(Types.QUERY.getName())
                                    .field(newFieldDefinition().name(JOBS.getName())
                                                               .description("Jobs list, it will be empty if there is none.")
                                                               .type(JobConnection.TYPE.getInstance(genericInformationDataFetcher,
                                                                                                    taskDataFetcher,
                                                                                                    variableDataFetcher,
                                                                                                    resultMapDataFetcher))
                                                               .argument(newArgument().name(Arguments.FILTER.getName())
                                                                                      .description("Jobs input filter.")
                                                                                      .type(new GraphQLList(JobInput.TYPE.getInstance(genericInformationDataFetcher,
                                                                                                                                      taskDataFetcher,
                                                                                                                                      variableDataFetcher,
                                                                                                                                      resultMapDataFetcher)))
                                                                                      .build())
                                                               .argument(JobConnection.getConnectionFieldArguments())
                                                               .argument(newArgument().name(Arguments.FIRST.getName())
                                                                                      .type(GraphQLInt)
                                                                                      .defaultValue(DefaultValues.PAGE_SIZE)
                                                                                      .build())
                                                               .dataFetcher(jobDataFetcher))
                                    .field(newFieldDefinition().name(VERSION.getName())
                                                               .description("GraphQL API version.")
                                                               .type(GraphQLString)
                                                               .dataFetcher(new StaticDataFetcher(VERSION_API)))
                                    .field(newFieldDefinition().name(VIEWER.getName())
                                                               .description("Viewer of the query.")
                                                               .type(User.TYPE.getInstance(genericInformationDataFetcher,
                                                                                           jobDataFetcher,
                                                                                           taskDataFetcher,
                                                                                           variableDataFetcher,
                                                                                           resultMapDataFetcher))
                                                               .dataFetcher(userDataFetcher))
                                    .build();
        }
    };

    public static final String VERSION_API = "1.0.0-alpha.1";

    private Query() {
    }

}
