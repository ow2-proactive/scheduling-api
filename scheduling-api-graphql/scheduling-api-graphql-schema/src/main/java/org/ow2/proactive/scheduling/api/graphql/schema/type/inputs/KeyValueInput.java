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
package org.ow2.proactive.scheduling.api.graphql.schema.type.inputs;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.KEY;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.VALUE;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLInputType;

import java.util.LinkedHashMap;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.ow2.proactive.scheduling.api.graphql.common.Fields;
import org.ow2.proactive.scheduling.api.graphql.common.Types;
import org.ow2.proactive.scheduling.api.graphql.schema.type.TypeSingleton;


/**
 * @author ActiveEon team
 */
@AllArgsConstructor
@Getter
public class KeyValueInput {

    public final static TypeSingleton<GraphQLInputType> TYPE = new TypeSingleton<GraphQLInputType>() {
        @Override
        public GraphQLInputType buildType(DataFetcher... dataFetchers) {
            return newInputObject().name(Types.KEY_VALUE_INPUT.getName())
                                   .description("KeyValue input filter.")
                                   .field(newInputObjectField().name(Fields.KEY.getName())
                                                               .description("Key as the key value of a map.")
                                                               .type(GraphQLString)
                                                               .build())
                                   .field(newInputObjectField().name(Fields.VALUE.getName())
                                                               .description("Value as the value of a map.")
                                                               .type(GraphQLString)
                                                               .build())
                                   .build();
        }
    };

    private final String key;

    private final String value;

    public KeyValueInput(LinkedHashMap<String, String> input) {
        if (input != null) {
            key = input.get(KEY.getName());
            value = input.get(VALUE.getName());
        } else {
            key = null;
            value = null;
        }
    }

}
