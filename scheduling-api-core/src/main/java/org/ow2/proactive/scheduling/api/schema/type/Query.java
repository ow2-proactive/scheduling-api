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
import org.ow2.proactive.scheduling.api.fetchers.UserDataFetcher;
import org.ow2.proactive.scheduling.api.schema.type.inputs.JobInput;
import org.ow2.proactive.scheduling.api.util.Constants;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import graphql.schema.StaticDataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;


/**
 * @author ActiveEon Team
 */
@Component
public final class Query {

    @Autowired
    private UserDataFetcher userDataFetcher;

    public GraphQLObjectType buildType() {
        return GraphQLObjectType.newObject()
                .name("Query")
                .field(newFieldDefinition().name("jobs")
                        .description("Jobs list, it will be empty if there is none")
                        .type(JobsConnection.TYPE)
                        .argument(newArgument().name(Constants.ARGUMENT_NAME_FILTER)
                                .description("Jobs input filter")
                                .type(new GraphQLList(JobInput.TYPE))
                                .build())
                        .argument(JobsConnection.getConnectionFieldArguments())
                        .argument(newArgument()
                                .name("first")
                                .type(GraphQLInt).defaultValue(Constants.PAGINATION_DEFAULT_SIZE)
                                .build())
                        .dataFetcher(new JobDataFetcher()))
                .field(newFieldDefinition().name("version")
                        .description("Query schema version")
                        .type(GraphQLString)
                        .dataFetcher(new StaticDataFetcher(Constants.VERSION_API)))
                .field(newFieldDefinition().name("viewer")
                        .description("Viewer of the query")
                        .type(User.TYPE)
                        .dataFetcher(userDataFetcher))
                .build();
    }

}
