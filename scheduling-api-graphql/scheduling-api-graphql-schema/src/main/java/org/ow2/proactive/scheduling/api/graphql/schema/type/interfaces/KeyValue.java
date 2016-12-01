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
package org.ow2.proactive.scheduling.api.graphql.schema.type.interfaces;

import org.ow2.proactive.scheduling.api.graphql.common.Types;
import org.ow2.proactive.scheduling.api.graphql.schema.type.GenericInformation;
import org.ow2.proactive.scheduling.api.graphql.schema.type.TypeSingleton;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Variable;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLInterfaceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.KEY;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VALUE;


/**
 * @author ActiveEon team
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class KeyValue {

    public static final TypeSingleton<GraphQLInterfaceType> TYPE = new TypeSingleton<GraphQLInterfaceType>() {
        @Override
        public GraphQLInterfaceType buildType(DataFetcher... dataFetchers) {
            return GraphQLInterfaceType.newInterface()
                    .name(Types.KEY_VALUE.getName())
                    .description("Key value type as a map.")
                    .field(newFieldDefinition().name(KEY.getName())
                            .description("Key as the key in a map.")
                            .type(GraphQLString))
                    .field(newFieldDefinition().name(VALUE.getName())
                            .description("Value as the value in a map.")
                            .type(GraphQLString))
                    .typeResolver(object -> {
                        if (object instanceof GenericInformation) {
                            return GenericInformation.TYPE.getInstance();
                        }
                        return Variable.TYPE.getInstance();
                    })
                    .build();
        }
    };

    protected String key;

    protected String value;

}
