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
package org.ow2.proactive.scheduling.api.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.ow2.proactive.scheduling.api.schema.type.inputs.KeyValueInput;
import org.ow2.proactive.scheduling.api.schema.type.interfaces.JobTaskCommon;
import org.ow2.proactive.scheduling.api.schema.type.interfaces.KeyValue;

import graphql.schema.DataFetchingEnvironment;


/**
 * @author ActiveEon team
 */
public final class KeyValues {

    private KeyValues() {
    }

    public static <T extends KeyValue, I extends List<KeyValueInput>> List<T> filterKeyValue(
            DataFetchingEnvironment environment, Function<JobTaskCommon, Map<String, String>> function,
            Supplier<T> keyValueSupplier) {

        JobTaskCommon object = (JobTaskCommon) environment.getSource();

        List<KeyValueInput> input = null;

        if(environment.getArgument("input") != null) {
            List<LinkedHashMap<String, String>> args = environment.getArgument("input");
            input = args.stream().map(arg -> new KeyValueInput(arg)).collect(Collectors.toList());
        }

        return filterKeyValue(function.apply(object), input, keyValueSupplier);
    }

    public static <T extends KeyValue> List<T> filterKeyValue(Map<String, String> keyValueEntries,
            List<KeyValueInput> input, Supplier<T> keyValueSupplier) {

        final Function<Map.Entry<String, String>, T> mapper = entry -> {
            T keyValue = keyValueSupplier.get();
            keyValue.setKey(entry.getKey());
            keyValue.setValue(entry.getValue());
            return keyValue;
        };

        if (input == null || input.isEmpty()) {
            return keyValueEntries.entrySet().parallelStream().map(mapper).collect(Collectors.toList());
        }

        // for each entry set compare it with the input criteria list
        return keyValueEntries.entrySet().parallelStream().filter(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();

            for (KeyValueInput i : input) {
                if (i.getKey() != null && i.getValue() != null) {
                    return key.equals(i.getKey()) && value.equals(i.getValue());
                } else if (i.getKey() != null && i.getValue() == null) {
                    return key.equals(i.getKey());
                } else if (i.getKey() == null && i.getValue() != null) {
                    return value.equals(i.getValue());
                }
            }

            return false;

        }).map(mapper).collect(Collectors.toList());

    }

}
