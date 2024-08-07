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
package org.ow2.proactive.scheduling.api.graphql.common;

import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * @author ActiveEon Team
 */
public final class Inputs {

    public static <T> T getValue(Map<String, Object> input, String fieldName, T defaultValue) {

        Object fieldValue = input.get(fieldName);

        if (fieldValue != null) {
            return (T) fieldValue;
        } else {
            return defaultValue;
        }
    }

    public static <T extends Enum> T getEnumValue(Map<String, Object> input, String fieldName, T defaultValue) {

        Object fieldValue = input.get(fieldName);

        if (fieldValue != null) {
            if (fieldValue instanceof String) {
                return (T) T.valueOf(defaultValue.getClass(), (String) fieldValue);
            } else {
                return (T) fieldValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static <T> T getObject(Map<String, Object> input, String fieldName, Function<Map<String, Object>, T> mapper,
            T defaultValue) {

        Object fieldValue = input.get(fieldName);

        if (fieldValue != null) {
            return mapper.apply((Map<String, Object>) fieldValue);
        } else {
            return defaultValue;
        }
    }

    public static <T> T getListOfObjects(Map<String, Object> input, String fieldName,
            Function<List<Map<String, Object>>, T> mapper, T defaultValue) {

        Object fieldValue = input.get(fieldName);

        if (fieldValue != null) {
            return mapper.apply((List<Map<String, Object>>) fieldValue);
        } else {
            return defaultValue;
        }
    }

}
