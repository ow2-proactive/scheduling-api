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

import org.ow2.proactive.scheduling.api.schema.type.Job;
import graphql.schema.GraphQLInputType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static java.util.function.Function.identity;
import static org.ow2.proactive.scheduling.api.util.Inputs.getValue;


/**
 * @author ActiveEon team
 */
@AllArgsConstructor
@Getter
public class JobInput {

    private static final String ID_FIELD_NAME = "id";

    private static final String JOB_NAME_FIELD_NAME = "name";

    private static final String OWNER_FIELD_NAME = "owner";

    private static final String PRIORITY_FIELD_NAME = "priority";

    private static final String PROJ_NAME_FIELD_NAME = "projectName";

    private long id;

    private String jobName;

    private String owner;

    private String priority;

    private String projectName;

    public JobInput(HashMap<String, String> input) {
        if (input != null) {
            id = getValue(input, ID_FIELD_NAME, Long::valueOf, -1l);
            jobName = getValue(input, JOB_NAME_FIELD_NAME, identity(), null);
            owner = getValue(input, OWNER_FIELD_NAME, identity(), null);
            priority = getValue(input, PRIORITY_FIELD_NAME, identity(), null);
            projectName = getValue(input, PROJ_NAME_FIELD_NAME, identity(), null);
        }
    }

    public final static GraphQLInputType TYPE = newInputObject().name("JobInput")
            .description("Job filter input")
            .field(newInputObjectField().name("id")
                    .description("Job ID")
                    .type(GraphQLLong)
                    .build())
            .field(newInputObjectField().name("jobName")
                    .description("Job name")
                    .type(GraphQLString)
                    .build())
            .field(newInputObjectField().name("owner")
                    .description("Job owner")
                    .type(GraphQLString)
                    .build())
            .field(newInputObjectField().name("priority")
                    .description("Job priority")
                    .type(Job.JOB_PRIORITY_ENUM)
                    .build())
            .field(newInputObjectField().name("projectName")
                    .description("Project name which the job belongs to")
                    .type(GraphQLString)
                    .build())
            .build();

}
