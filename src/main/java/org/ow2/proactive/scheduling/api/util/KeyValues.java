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

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.ow2.proactive.scheduling.api.schema.type.inputs.KeyValueInput;
import org.ow2.proactive.scheduling.api.schema.type.interfaces.JobTaskCommon;
import org.ow2.proactive.scheduling.api.schema.type.interfaces.KeyValue;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

import graphql.schema.DataFetchingEnvironment;


/**
 * @author ActiveEon team
 */
public final class KeyValues {

    private KeyValues() {
    }

    @SuppressWarnings("unchecked")
    public static <T extends KeyValue, I extends KeyValueInput> List<T> filterKeyValue(
            DataFetchingEnvironment environment, Function<JobTaskCommon, Map<String, String>> function,
            Supplier<T> keyValueSupplier) {

        JobTaskCommon object = (JobTaskCommon) environment.getSource();
        I input = (I) environment.getArgument("input");

        return filterKeyValue(function.apply(object), input, keyValueSupplier);
    }

    public static <T extends KeyValue, I extends KeyValueInput> List<T> filterKeyValue(
            Map<String, String> keyValueEntries, I input, Supplier<T> keyValueSupplier) {

        if (input == null) {
            return ImmutableList.of();
        }

        String key = input.getKey();
        Object value = input.getValue();

        if (key == null && value == null) {
            return filterBy(keyValueEntries, entry -> true, keyValueSupplier);
        } else if (key == null && value != null) {
            return filterBy(keyValueEntries, entry -> value.equals(entry.getValue()), keyValueSupplier);
        } else if (key != null && value == null) {
            return filterBy(keyValueEntries, entry -> key.equals(entry.getKey()), keyValueSupplier);
        } else {
            return filterBy(keyValueEntries,
                    entry -> value.equals(entry.getValue()) && key.equals(entry.getKey()),
                    keyValueSupplier);
        }
    }

    @VisibleForTesting
    protected static <T extends KeyValue> List<T> filterBy(Map<String, String> keyValueEntries,
            Predicate<Map.Entry<String, String>> predicate, Supplier<T> keyValueSupplier) {

        return keyValueEntries.entrySet().stream().filter(predicate).map(entry -> {
            T keyValue = keyValueSupplier.get();
            keyValue.setKey(entry.getKey());
            keyValue.setValue(entry.getValue());
            return keyValue;
        }).collect(Collectors.toList());

    }

}
