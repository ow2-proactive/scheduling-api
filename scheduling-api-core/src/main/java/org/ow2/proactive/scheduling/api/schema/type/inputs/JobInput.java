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

import java.util.Map;

import org.ow2.proactive.scheduling.api.schema.type.Job;
import org.ow2.proactive.scheduling.api.util.Inputs;
import graphql.schema.GraphQLInputType;
import lombok.Getter;

import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static org.ow2.proactive.scheduling.api.schema.type.inputs.InputFieldNameEnum.ID;
import static org.ow2.proactive.scheduling.api.schema.type.inputs.InputFieldNameEnum.NAME;
import static org.ow2.proactive.scheduling.api.schema.type.inputs.InputFieldNameEnum.OWNER;
import static org.ow2.proactive.scheduling.api.schema.type.inputs.InputFieldNameEnum.PRIORITY;
import static org.ow2.proactive.scheduling.api.schema.type.inputs.InputFieldNameEnum.PROJECT_NAME;
import static org.ow2.proactive.scheduling.api.schema.type.inputs.InputFieldNameEnum.STATUS;
import static org.ow2.proactive.scheduling.api.schema.type.inputs.InputFieldNameEnum.SUBMITTED_TIME;


/**
 * @author ActiveEon team
 */
@Getter
public class JobInput extends JobTaskCommonAbstractInput {

    private String jobName;

    private String owner;

    private String priority;

    private String projectName;

    private SubmittedTimeInput submittedTime;

    public JobInput(Map<String, Object> input) {
        super(input);
        if (input != null) {
            jobName = Inputs.getValue(input, NAME.value(), null);
            owner = Inputs.getValue(input, OWNER.value(), null);
            priority = Inputs.getValue(input, PRIORITY.value(), null);
            projectName = Inputs.getValue(input, PROJECT_NAME.value(), null);
            submittedTime = Inputs.getObject(input, SUBMITTED_TIME.value(), SubmittedTimeInput::new, null);
        }
    }

    public final static GraphQLInputType TYPE = newInputObject().name("JobInput")
            .description("Job filter input")
            .field(newInputObjectField().name(ID.value())
                    .description("Job ID")
                    .type(GraphQLLong)
                    .build())
            .field(newInputObjectField().name(NAME.value())
                    .description("Job name")
                    .type(GraphQLString)
                    .build())
            .field(newInputObjectField().name(OWNER.value())
                    .description("Job owner")
                    .type(GraphQLString)
                    .build())
            .field(newInputObjectField().name(PRIORITY.value())
                    .description("Job priority")
                    .type(Job.JOB_PRIORITY_ENUM)
                    .build())
            .field(newInputObjectField().name(PROJECT_NAME.value())
                    .description("Project name which the job belongs to")
                    .type(GraphQLString)
                    .build())
            .field(newInputObjectField().name(STATUS.value())
                    .description("Job status")
                    .type(Job.JOB_STATUS_ENUM)
                    .build())
            .field(newInputObjectField().name(SUBMITTED_TIME.value())
                    .description("Job submitted time")
                    .type(SubmittedTimeInput.TYPE)
                    .build())
            .build();

}
