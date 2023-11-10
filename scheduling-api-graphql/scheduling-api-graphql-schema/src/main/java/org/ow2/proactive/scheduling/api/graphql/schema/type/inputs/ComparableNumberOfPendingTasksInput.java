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

import static graphql.Scalars.GraphQLInt;
import static graphql.scalars.java.JavaPrimitives.GraphQLLong;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.AFTER;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.BEFORE;

import java.util.Map;

import org.ow2.proactive.scheduling.api.graphql.common.Types;
import org.ow2.proactive.scheduling.api.graphql.schema.type.TypeSingleton;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLInputType;
import lombok.Data;


/**
 * @author ActiveEon Team
 */
@Data
public class ComparableNumberOfPendingTasksInput extends ComparableIntegerInput {

    public final static TypeSingleton<GraphQLInputType> TYPE = new TypeSingleton<GraphQLInputType>() {
        @Override
        public GraphQLInputType buildType(DataFetcher... dataFetchers) {
            return newInputObject().name(Types.COMPARABLE_NUMBER_OF_PENDING_TASKS_INPUT.getName())
                                   .description("Number of pending tasks input.")
                                   .field(newInputObjectField().name(BEFORE.getName())
                                                               .description("Jobs having a number of pending tasks lower than this value.")
                                                               .type(GraphQLInt)
                                                               .build())
                                   .field(newInputObjectField().name(AFTER.getName())
                                                               .description("Jobs having a number of pending tasks greater than this value.")
                                                               .type(GraphQLInt)
                                                               .build())
                                   .build();
        }
    };

    public ComparableNumberOfPendingTasksInput(Map<String, Object> input) {
        super(input);
    }

}
