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

import java.util.HashMap;

import org.ow2.proactive.scheduling.api.schema.type.Task;
import graphql.schema.GraphQLInputType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static org.ow2.proactive.scheduling.api.util.Inputs.STRING2LONG;
import static org.ow2.proactive.scheduling.api.util.Inputs.STRING2STRING;
import static org.ow2.proactive.scheduling.api.util.Inputs.getValue;


/**
 * @author ActiveEon team
 */
@AllArgsConstructor
@Getter
public class TaskInput {

    private static final String ID_FIELD_NAME = "id";

    private static final String JOB_ID_FIELD_NAME = "jobId";

    private static final String TASK_NAME_FIELD_NAME = "name";

    private static final String TASK_STATUS_FIELD_NAME = "status";

    private final long id;

    private final long jobId;

    private final String status;

    private final String taskName;

    public TaskInput(HashMap<String, String> input) {
        if (input != null) {
            id = getValue(input, ID_FIELD_NAME, STRING2LONG, -1l);
            jobId = getValue(input, JOB_ID_FIELD_NAME, STRING2LONG, -1l);
            status = getValue(input, TASK_STATUS_FIELD_NAME, STRING2STRING, null);
            taskName = getValue(input, TASK_NAME_FIELD_NAME, STRING2STRING, null);
        } else {
            id = -1;
            jobId = -1;
            status = null;
            taskName = null;
        }
    }

    public final static GraphQLInputType TYPE = newInputObject().name("TaskInput")
            .description("Task filter input")
            .field(newInputObjectField().name("id")
                    .description("Task identifier")
                    .type(GraphQLLong)
                    .build())
            .field(newInputObjectField().name("jobId")
                    .description("Identifier of the Job which the task belongs to")
                    .type(GraphQLLong)
                    .build())
            .field(newInputObjectField().name("status")
                    .description("Task status")
                    .type(Task.TASK_STATUS_ENUM)
                    .build())
            .field(newInputObjectField().name("taskName")
                    .description("Task name")
                    .type(GraphQLString)
                    .build())
            .build();

}
