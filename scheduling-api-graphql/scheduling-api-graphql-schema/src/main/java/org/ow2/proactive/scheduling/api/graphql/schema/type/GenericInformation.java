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

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.KEY;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VALUE;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLObjectType;
import lombok.AllArgsConstructor;

import org.ow2.proactive.scheduling.api.graphql.common.Types;
import org.ow2.proactive.scheduling.api.graphql.schema.type.interfaces.KeyValue;


/**
 * @author ActiveEon Team
 */
@AllArgsConstructor
public class GenericInformation extends KeyValue {

    public final static TypeSingleton<GraphQLObjectType> TYPE = new TypeSingleton<GraphQLObjectType>() {
        @Override
        public GraphQLObjectType buildType(DataFetcher... dataFetchers) {
            return GraphQLObjectType.newObject()
                                    .name(Types.GENERIC_INFORMATION.getName())
                                    .description("Generic Information.")
                                    .withInterface(KeyValue.TYPE.getInstance())
                                    .field(newFieldDefinition().name(KEY.getName())
                                                               .description("Key as the key in a map.")
                                                               .type(GraphQLString))
                                    .field(newFieldDefinition().name(VALUE.getName())
                                                               .description("Value as the value in a map.")
                                                               .type(GraphQLString))
                                    .build();
        }
    };

}
