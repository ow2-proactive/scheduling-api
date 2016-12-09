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

import static graphql.Scalars.GraphQLBoolean;
import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.EXCLUDE_REMOVED;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.ID;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.NAME;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.OWNER;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.PRIORITY;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.PROJECT_NAME;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.STATUS;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.SUBMITTED_TIME;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLInputType;

import java.util.Map;

import lombok.Getter;

import org.ow2.proactive.scheduling.api.graphql.common.Inputs;
import org.ow2.proactive.scheduling.api.graphql.common.Types;
import org.ow2.proactive.scheduling.api.graphql.schema.type.JobPriority;
import org.ow2.proactive.scheduling.api.graphql.schema.type.JobStatus;
import org.ow2.proactive.scheduling.api.graphql.schema.type.TypeSingleton;


/**
 * @author ActiveEon team
 */
@Getter
public class JobInput extends JobTaskCommonAbstractInput {

    public final static TypeSingleton<GraphQLInputType> TYPE = new TypeSingleton<GraphQLInputType>() {
        @Override
        public GraphQLInputType buildType(DataFetcher... dataFetchers) {
            return newInputObject().name(Types.JOB_INPUT.getName())
                                   .description("Job input filter.")
                                   .field(newInputObjectField().name(EXCLUDE_REMOVED.getName())
                                                               .description("Exclude removed job, true by default.")
                                                               .type(GraphQLBoolean)
                                                               .build())
                                   .field(newInputObjectField().name(ID.getName())
                                                               .description("Job identifier.")
                                                               .type(GraphQLLong)
                                                               .build())
                                   .field(newInputObjectField().name(NAME.getName())
                                                               .description("Job name.")
                                                               .type(GraphQLString)
                                                               .build())
                                   .field(newInputObjectField().name(OWNER.getName())
                                                               .description("Job owner (i.e. the one who submitted the Job).")
                                                               .type(GraphQLString)
                                                               .build())
                                   .field(newInputObjectField().name(PRIORITY.getName())
                                                               .description("Job priority.")
                                                               .type(JobPriority.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(PROJECT_NAME.getName())
                                                               .description("Project name which the job belongs to.")
                                                               .type(GraphQLString)
                                                               .build())
                                   .field(newInputObjectField().name(STATUS.getName())
                                                               .description("Job status.")
                                                               .type(JobStatus.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(SUBMITTED_TIME.getName())
                                                               .description("Job submitted time.")
                                                               .type(SubmittedTimeInput.TYPE.getInstance())
                                                               .build())
                                   .build();
        }
    };

    private boolean excludeRemoved;

    private String jobName;

    private String owner;

    private String priority;

    private String projectName;

    private SubmittedTimeInput submittedTime;

    public JobInput(Map<String, Object> input) {
        super(input);
        if (input != null) {
            excludeRemoved = Inputs.getValue(input, EXCLUDE_REMOVED.getName(), true);
            jobName = Inputs.getValue(input, NAME.getName(), null);
            owner = Inputs.getValue(input, OWNER.getName(), null);
            priority = Inputs.getValue(input, PRIORITY.getName(), null);
            projectName = Inputs.getValue(input, PROJECT_NAME.getName(), null);
            submittedTime = Inputs.getObject(input, SUBMITTED_TIME.getName(), SubmittedTimeInput::new, null);
        }
    }

}
