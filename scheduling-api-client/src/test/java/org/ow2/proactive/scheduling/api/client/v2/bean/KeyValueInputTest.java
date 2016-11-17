package org.ow2.proactive.scheduling.api.client.v2.bean;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class KeyValueInputTest {
    private static final String ALL = "{ key : \"key\" value : \"value\" }";

    private static final String PARTIAL = "{ key : \"key\" }";

    @Test
    public void getQueryString() throws Exception {
        KeyValueInput input = new KeyValueInput.Builder().key("key").value("value").build();
        assertThat(input.getQueryString(), is(ALL));
    }

    @Test
    public void getPartialQueryString() throws Exception {
        KeyValueInput input = new KeyValueInput.Builder().key("key").build();
        System.out.print(input.getQueryString());
        assertThat(input.getQueryString(), is(PARTIAL));
    }

}