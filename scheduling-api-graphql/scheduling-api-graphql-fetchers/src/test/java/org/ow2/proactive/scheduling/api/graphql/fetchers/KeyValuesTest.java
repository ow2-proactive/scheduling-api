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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.ow2.proactive.scheduling.api.graphql.schema.type.GenericInformation;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.KeyValueInput;
import org.python.google.common.collect.ImmutableList;


public class KeyValuesTest {

    private Map<String, String> entries = new HashMap<>();

    private List<KeyValueInput> inputs;

    @Before
    public void setup() {
        KeyValueInput input1 = new KeyValueInput("START_AT", null);
        KeyValueInput input2 = new KeyValueInput("MASTER_ID", null);
        inputs = ImmutableList.of(input1, input2);
    }

    @Test
    public void filterKeyValue() throws Exception {
        entries.clear();
        entries.put("START_AT", "startat");
        entries.put("MASTER_ID", "masterid");

        List<GenericInformation> list = KeyValues.filterKeyValue(entries, inputs, GenericInformation::new);
        assertThat(list.size(), is(2));
    }

    @Test
    public void filterKeyValue2() throws Exception {
        entries.clear();
        entries.put("MASTER_ID", "startat");

        List<GenericInformation> list = KeyValues.filterKeyValue(entries, inputs, GenericInformation::new);
        assertThat(list.size(), is(1));
    }

    @Test
    public void filterKeyValue3() throws Exception {
        entries.clear();
        entries.put("MASTER", "startat");

        List<GenericInformation> list = KeyValues.filterKeyValue(entries, inputs, GenericInformation::new);
        assertThat(list.size(), is(0));
    }
}
