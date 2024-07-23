/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
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

import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.*;

import java.util.Map;

import org.ow2.proactive.scheduling.api.graphql.common.Inputs;
import org.ow2.proactive.scheduling.api.graphql.common.NullStatus;

import graphql.schema.GraphQLEnumType;
import lombok.Data;


/**
 * @author ActiveEon Team
 * @since 15/12/16
 */
@Data
public class ComparableInput<T> {

    public final static GraphQLEnumType NullStatusEnum = GraphQLEnumType.newEnum()
                                                                        .name(NULL_STATUS.getName())
                                                                        .description("Check if the value is null or not null")
                                                                        .value("NULL")
                                                                        .value("NOT_NULL")
                                                                        .value("ANY")
                                                                        .build();

    protected final T before;

    protected final T after;

    protected final NullStatus nullStatus;

    public ComparableInput(Map<String, Object> input, T defaultValue) {
        if (input != null) {
            before = Inputs.getValue(input, BEFORE.getName(), defaultValue);
            after = Inputs.getValue(input, AFTER.getName(), defaultValue);
            nullStatus = Inputs.getEnumValue(input, NULL_STATUS.getName(), NullStatus.ANY);
        } else {
            before = defaultValue;
            after = defaultValue;
            nullStatus = Inputs.getEnumValue(input, NULL_STATUS.getName(), NullStatus.ANY);
        }
    }
}
