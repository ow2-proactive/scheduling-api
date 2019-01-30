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

import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.ID;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.NAME;

import java.util.Map;

import org.ow2.proactive.scheduling.api.graphql.common.Fields;
import org.ow2.proactive.scheduling.api.graphql.common.Inputs;
import org.ow2.proactive.scheduling.api.graphql.common.Types;
import org.ow2.proactive.scheduling.api.graphql.schema.type.TaskStatus;
import org.ow2.proactive.scheduling.api.graphql.schema.type.TypeSingleton;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLList;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author ActiveEon team
 */
@AllArgsConstructor
@Getter
public class TaskInput extends JobTaskCommonAbstractInput {

    public final static TypeSingleton<GraphQLInputType> TYPE = new TypeSingleton<GraphQLInputType>() {
        @Override
        public GraphQLInputType buildType(DataFetcher... dataFetchers) {
            return newInputObject().name(Types.TASK_INPUT.getName())
                                   .description("Task filter input")
                                   .field(newInputObjectField().name(ID.getName())
                                                               .description("Task identifier.")
                                                               .type(GraphQLLong)
                                                               .build())
                                   .field(newInputObjectField().name(Fields.STATUS.getName())
                                                               .description("Task status.")
                                                               .type(new GraphQLList(TaskStatus.TYPE.getInstance()))
                                                               .build())
                                   .field(newInputObjectField().name(NAME.getName())
                                                               .description("Task name.")
                                                               .type(GraphQLString)
                                                               .build())
                                   .build();
        }
    };

    private final String taskName;

    private final TaskStatusInput taskStatus;

    public TaskInput(Map<String, Object> input) {
        super(input);
        if (input != null) {
            taskName = Inputs.getValue(input, NAME.getName(), null);
            taskStatus = new TaskStatusInput(Inputs.getValue(input, Fields.STATUS.getName(), null));
        } else {
            taskName = null;
            taskStatus = null;
        }
    }

}
