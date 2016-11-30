package org.ow2.proactive.scheduling.api.graphql.client.beans;

import java.util.ArrayList;
import java.util.List;

import org.ow2.proactive.scheduling.api.graphql.common.Arguments;
import org.ow2.proactive.scheduling.api.graphql.common.Fields;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.FILTER;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.GENERIC_INFORMATION;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.KEY;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VALUE;

public class GenericInformationTest {

    private static final String ALL = String.format(
            "%s( %s : [{ %s : \"START_AT\" },{ %s : \"blabla\" %s : \"value\" }] ){\n%s\n%s\n}\n",
            GENERIC_INFORMATION.getName(), FILTER.getName(), KEY.getName(), KEY.getName(), VALUE.getName(),
            KEY.getName(), VALUE.getName());

    @Test
    public void getQueryString() throws Exception {
        List<KeyValueInput> input = new ArrayList<>();
        input.add(new KeyValueInput.Builder().key("START_AT").build());
        input.add(new KeyValueInput.Builder().key("blabla").value("value").build());
        GenericInformation genericInformation = new GenericInformation.Builder().input(input).build();
        System.out.print(genericInformation.getQueryString());
        assertThat(genericInformation.getQueryString(), is(ALL));
    }

}