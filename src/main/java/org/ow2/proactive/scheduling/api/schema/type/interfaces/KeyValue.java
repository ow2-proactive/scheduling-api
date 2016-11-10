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
package org.ow2.proactive.scheduling.api.schema.type.interfaces;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;

import org.ow2.proactive.scheduling.api.schema.type.GenericInformation;
import org.ow2.proactive.scheduling.api.schema.type.Variable;

import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author ActiveEon team
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class KeyValue {

    protected String key;

    protected String value;

    public static final GraphQLInterfaceType TYPE = GraphQLInterfaceType.newInterface()
            .name("KeyValue")
            .description("Key value type as a map")
            .field(newFieldDefinition().name("key")
                    .description("Key as the key in a map")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("value")
                    .description("Value as the value in a map")
                    .type(GraphQLString))
            .typeResolver(new TypeResolver() {

                @Override
                public GraphQLObjectType getType(
                        Object object) {
                    if (object instanceof GenericInformation) {
                        return GenericInformation.TYPE;
                    }
                    return Variable.TYPE;
                }

            })
            .build();

}
