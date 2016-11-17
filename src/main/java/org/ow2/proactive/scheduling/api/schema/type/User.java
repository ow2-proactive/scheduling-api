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
package org.ow2.proactive.scheduling.api.schema.type;

import org.ow2.proactive.scheduling.api.fetchers.JobDataFetcher;
import org.ow2.proactive.scheduling.api.schema.type.inputs.JobInput;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import lombok.Builder;
import lombok.Getter;

import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static org.ow2.proactive.scheduling.api.util.Constants.DEFAULT_FIRST_ELEMENTS;


/**
 * @author ActiveEon Team
 */
@Builder
@Getter
public class User {

    private final String login;

    private final String sessionId;

    public static final GraphQLObjectType TYPE = GraphQLObjectType.newObject()
            .name("User")
            .field(newFieldDefinition().name("login")
                    .description("Login name of the viewer")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("sessionId")
                    .description("Session ID of the viewer")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("jobs")
                    .description("Jobs list, it will be empty if there is none")
                    .type(JobsConnection.TYPE)
                    .argument(newArgument().name("input")
                            .description("Jobs filter input")
                            .type(new GraphQLList(JobInput.TYPE))
                            .build())
                    .argument(JobsConnection.getConnectionFieldArguments())
                    .argument(newArgument()
                            .name("first")
                            .type(GraphQLInt).defaultValue(DEFAULT_FIRST_ELEMENTS)
                            .build())
                    .dataFetcher(new JobDataFetcher()))
            .build();

}
