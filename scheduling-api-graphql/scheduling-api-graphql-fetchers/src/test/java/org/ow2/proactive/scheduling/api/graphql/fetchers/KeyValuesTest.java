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