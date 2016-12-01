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
package org.ow2.proactive.scheduling.api.graphql.schema.type;

import org.ow2.proactive.scheduling.api.graphql.common.Types;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLObjectType;
import lombok.Builder;
import lombok.Getter;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.GLOBAL_SPACE_URL;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.INPUT_SPACE_URL;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.OUTPUT_SPACE_URL;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.USER_SPACE_URL;

/**
 * @author ActiveEon Team
 */
@Builder
@Getter
public class DataManagement {

    public static final TypeSingleton<GraphQLObjectType> TYPE = new TypeSingleton<GraphQLObjectType>() {
        @Override
        public GraphQLObjectType buildType(DataFetcher... dataFetchers) {
            return GraphQLObjectType.newObject()
                    .name(Types.DATA_MANAGEMENT.getName())
                    .description(
                            "Represents a Job configuration for managing data. It contains user defined data space " +
                                    "URLs. Data spaces are used to fetch and push files between the Scheduler and Tasks. " +
                                    "A data space URL set to `null` means that the space " +
                                    "started with the Scheduler instance is used.")
                    .field(newFieldDefinition().name(GLOBAL_SPACE_URL.getName())
                            .description(
                                    "The URL of the global space that is shared between all ProActive users.")
                            .type(GraphQLString))
                    .field(newFieldDefinition().name(INPUT_SPACE_URL.getName())
                            .description(
                                    "The URL of a read-only space that is personal to the ProActive user running the Job.")
                            .type(GraphQLString))
                    .field(newFieldDefinition().name(OUTPUT_SPACE_URL.getName())
                            .description(
                                    "The URL of a private space that is personal to the ProActive user running the Job and used to push results.")
                            .type(GraphQLString))
                    .field(newFieldDefinition().name(USER_SPACE_URL.getName())
                            .description(
                                    "The URL of the user space that is personal to the ProActive user running the Job.")
                            .type(GraphQLString))
                    .build();
        }
    };

    private final String globalSpaceUrl;

    private final String inputSpaceUrl;

    private final String outputSpaceUrl;

    private final String userSpaceUrl;

}
