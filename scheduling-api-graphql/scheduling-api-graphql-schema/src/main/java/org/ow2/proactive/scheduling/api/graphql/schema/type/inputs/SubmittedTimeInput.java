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

import java.util.Map;

import org.ow2.proactive.scheduling.api.graphql.common.Inputs;
import org.ow2.proactive.scheduling.api.graphql.common.Types;
import org.ow2.proactive.scheduling.api.graphql.schema.type.TypeSingleton;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLInputType;
import lombok.Getter;

import static graphql.Scalars.GraphQLLong;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.AFTER;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.BEFORE;

/**
 * @author ActiveEon Team
 */
@Getter
public class SubmittedTimeInput {

    public final static TypeSingleton<GraphQLInputType> TYPE = new TypeSingleton<GraphQLInputType>() {
        @Override
        public GraphQLInputType buildType(DataFetcher... dataFetchers) {
            return newInputObject().name(Types.SUBMITTED_TIME_INPUT.getName())
                    .description("Submitted time filter input.")
                    .field(newInputObjectField().name(BEFORE.getName())
                            .description("Jobs having its submitted time before this value.")
                            .type(GraphQLLong)
                            .build())
                    .field(newInputObjectField().name(AFTER.getName())
                            .description("Jobs having its submitted time after this value.")
                            .type(GraphQLLong)
                            .build())
                    .build();
        }
    };

    private final long before;

    private final long after;

    public SubmittedTimeInput(Map<String, Object> input) {
        if (input != null) {
            before = Inputs.getValue(input, BEFORE.getName(), -1L);
            after = Inputs.getValue(input, AFTER.getName(), -1L);
        } else {
            before = -1L;
            after = -1L;
        }
    }

}
