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

import graphql.schema.GraphQLObjectType;
import lombok.Builder;
import lombok.Getter;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;

@Builder
@Getter
public class DataManagement {

    private final String globalSpaceUrl;

    private final String inputSpaceUrl;

    private final String outputSpaceUrl;

    private final String userSpaceUrl;

    public static final GraphQLObjectType TYPE = GraphQLObjectType.newObject()
            .name("DataManagement")
            .description(
                    "Represents a Job configuration for managing data. It contains user defined data space " +
                            "URLs. Data spaces are used to fetch and push files between the Scheduler and Tasks. " +
                            "A data space URL set to `null` means that the space " +
                            "started with the Scheduler instance is used.")
            .field(newFieldDefinition().name("globalSpaceUrl")
                    .description(
                            "The URL of the global space that is shared between all ProActive users.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("inputSpaceUrl")
                    .description(
                            "The URL of a read-only space that is personal to the ProActive user running the Job.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("outputSpaceUrl")
                    .description(
                            "The URL of a private space that is personal to the ProActive user running the Job and used to push results.")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("userSpaceUrl")
                    .description(
                            "The URL of the user space that is personal to the ProActive user running the Job.")
                    .type(GraphQLString))
            .build();

}
