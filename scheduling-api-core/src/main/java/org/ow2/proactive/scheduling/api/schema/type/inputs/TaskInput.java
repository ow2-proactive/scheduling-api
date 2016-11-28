/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2015 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 *  * $$ACTIVEEON_INITIAL_DEV$$
 */
package org.ow2.proactive.scheduling.api.schema.type.inputs;

import graphql.schema.GraphQLInputType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ow2.proactive.scheduling.api.schema.type.Task;
import org.ow2.proactive.scheduling.api.util.Inputs;

import java.util.Map;

import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;


/**
 * @author ActiveEon team
 */
@AllArgsConstructor
@Getter
public class TaskInput extends JobTaskCommonAbstractInput {

    private final String taskName;

    public TaskInput(Map<String, Object> input) {
        super(input);
        if (input != null) {
            taskName = Inputs.getValue(input, InputFieldNameEnum.NAME.value(), null);
        } else {
            taskName = null;
        }
    }

    public final static GraphQLInputType TYPE = newInputObject().name("TaskInput")
            .description("Task filter input")
            .field(newInputObjectField().name(InputFieldNameEnum.ID.value())
                    .description("Task identifier")
                    .type(GraphQLLong)
                    .build())
            .field(newInputObjectField().name(InputFieldNameEnum.STATUS.value())
                    .description("Task status")
                    .type(Task.TASK_STATUS_ENUM)
                    .build())
            .field(newInputObjectField().name(InputFieldNameEnum.NAME.value())
                    .description("Task name")
                    .type(GraphQLString)
                    .build())
            .build();

}
