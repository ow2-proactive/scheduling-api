package org.ow2.proactive.scheduling.api.graphql.client.beans;

import org.ow2.proactive.scheduling.api.graphql.common.Fields;
import com.google.common.truth.Truth;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.KEY;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VALUE;


public class KeyValueInputTest {

    private static final String ALL = "{ " + KEY.getName() + " : \"key\" " + VALUE.getName() + " : \"value\" }";

    private static final String PARTIAL = "{ " + KEY.getName() + " : \"key\" }";

    @Test
    public void getQueryString() throws Exception {
        KeyValueInput input = new KeyValueInput.Builder().key("key").value("value").build();
        assertThat(input.getQueryString()).isEqualTo(ALL);
    }

    @Test
    public void getPartialQueryString() throws Exception {
        KeyValueInput input = new KeyValueInput.Builder().key("key").build();
        System.out.print(input.getQueryString());
        assertThat(input.getQueryString()).isEqualTo(PARTIAL);
    }

}