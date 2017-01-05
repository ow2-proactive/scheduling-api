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
package org.ow2.proactive.scheduling.api.graphql.fetchers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.ow2.proactive.scheduling.api.graphql.common.Arguments;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.KeyValueInput;
import org.ow2.proactive.scheduling.api.graphql.schema.type.interfaces.JobTaskCommon;
import org.ow2.proactive.scheduling.api.graphql.schema.type.interfaces.KeyValue;

import com.google.common.base.Strings;

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

        Object filterArgument = environment.getArgument(Arguments.FILTER.getName());
        if (filterArgument != null) {
            List<LinkedHashMap<String, String>> args = (List<LinkedHashMap<String, String>>) filterArgument;
            input = args.stream().map(KeyValueInput::new).collect(Collectors.toList());
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
                if (!Strings.isNullOrEmpty(i.getKey()) && !Strings.isNullOrEmpty(i.getValue())) {
                    if (key.equals(i.getKey()) && value.equals(i.getValue())) {
                        return true;
                    } else {
                        continue;
                    }
                } else if (!Strings.isNullOrEmpty(i.getKey()) && Strings.isNullOrEmpty(i.getValue())) {
                    if (key.equals(i.getKey())) {
                        return true;
                    } else {
                        continue;
                    }
                } else if (Strings.isNullOrEmpty(i.getKey()) && !Strings.isNullOrEmpty(i.getValue())) {
                    if (value.equals(i.getValue())) {
                        return true;
                    } else {
                        continue;
                    }
                }
            }

            return false;

        }).map(mapper).collect(Collectors.toList());

    }

}
