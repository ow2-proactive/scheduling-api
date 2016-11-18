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

import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import graphql.schema.GraphQLInputType;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author ActiveEon team
 */
@AllArgsConstructor
@Getter
public class KeyValueInput {

    private static final String KEY_FIELD_NAME = "key";

    private static final String VALUE_FIELD_NAME = "value";

    private final String key;

    private final String value;

    public KeyValueInput(LinkedHashMap<String, String> input) {
        if (input != null) {
            key = input.get(KEY_FIELD_NAME);
            value = input.get(VALUE_FIELD_NAME);
        } else {
            key = null;
            value = null;
        }

    }

    public final static GraphQLInputType TYPE = newInputObject().name("KeyValueInput")
            .description("KeyValue filter input")
            .field(newInputObjectField().name("key")
                    .description("Key as the key value of a map")
                    .type(GraphQLString)
                    .build())
            .field(newInputObjectField().name("value")
                    .description("Value as the value of a map")
                    .type(GraphQLString)
                    .build())
            .build();

}
