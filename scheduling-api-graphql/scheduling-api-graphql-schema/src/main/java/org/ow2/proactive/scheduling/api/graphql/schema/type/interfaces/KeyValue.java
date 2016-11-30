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
package org.ow2.proactive.scheduling.api.graphql.schema.type.interfaces;

import org.ow2.proactive.scheduling.api.graphql.common.Types;
import org.ow2.proactive.scheduling.api.graphql.schema.type.GenericInformation;
import org.ow2.proactive.scheduling.api.graphql.schema.type.TypeSingleton;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Variable;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLInterfaceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.KEY;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VALUE;


/**
 * @author ActiveEon team
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class KeyValue {

    public static final TypeSingleton<GraphQLInterfaceType> TYPE = new TypeSingleton<GraphQLInterfaceType>() {
        @Override
        public GraphQLInterfaceType buildType(DataFetcher... dataFetchers) {
            return GraphQLInterfaceType.newInterface()
                    .name(Types.KEY_VALUE.getName())
                    .description("Key value type as a map.")
                    .field(newFieldDefinition().name(KEY.getName())
                            .description("Key as the key in a map.")
                            .type(GraphQLString))
                    .field(newFieldDefinition().name(VALUE.getName())
                            .description("Value as the value in a map.")
                            .type(GraphQLString))
                    .typeResolver(object -> {
                        if (object instanceof GenericInformation) {
                            return GenericInformation.TYPE.getInstance();
                        }
                        return Variable.TYPE.getInstance();
                    })
                    .build();
        }
    };

    protected String key;

    protected String value;

}
