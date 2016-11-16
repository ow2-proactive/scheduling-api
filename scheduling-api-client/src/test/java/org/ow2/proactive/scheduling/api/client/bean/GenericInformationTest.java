package org.ow2.proactive.scheduling.api.client.bean;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class GenericInformationTest {
    private static final String ALL = "genericInformation(input : [{ key : \"START_AT\" },{ key : \"blabla\" value : \"value\" }]){\nkey\nvalue\n}\n";

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