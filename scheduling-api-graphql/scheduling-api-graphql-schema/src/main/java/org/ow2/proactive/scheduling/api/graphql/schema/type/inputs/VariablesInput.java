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
package org.ow2.proactive.scheduling.api.graphql.schema.type.inputs;

import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VARIABLES;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ow2.proactive.scheduling.api.graphql.schema.type.TypeSingleton;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLList;
import lombok.Data;
import lombok.Getter;


/**
 * @author ActiveEon team
 */
@Data
@Getter
public class VariablesInput {

    public final static TypeSingleton<GraphQLInputType> TYPE = new TypeSingleton<GraphQLInputType>() {
        @Override
        public GraphQLInputType buildType(DataFetcher... dataFetchers) {
            return newInputObject().name(VARIABLES.getName())
                                   .description("KeyValue input filter.")
                                   .field(newInputObjectField().name(VARIABLES.getName())
                                                               .description("Key as the key value of a map.")
                                                               .type(new GraphQLList(KeyValueInput.TYPE.getInstance()))
                                                               .build())
                                   .build();
        }
    };

    private final List<Map<String, String>> variables;

    public VariablesInput(List<Map<String, Object>> input) {
        if (input != null) {
            variables = input.stream().map(x -> {
                HashMap<String, String> result = new HashMap<String, String>();
                x.keySet().forEach(key -> result.put(key, (String) x.get(key)));
                return result;
            }).collect(Collectors.toList());
        } else {
            variables = null;
        }
    }

}
