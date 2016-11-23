package org.ow2.proactive.scheduling.api.client.v2.beans;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class KeyValueTest {

    private static final String GENERIC_INFO = "genericInformation{\nkey\nvalue\n}\n";

    private static final String GENERIC_INFO_PARTIAL = "genericInformation{\nkey\n}\n";

    private static final String VAR = "variables{\nkey\nvalue\n}\n";

    private static final String VAR_PARTIAL = "variables{\nkey\n}\n";

    @Test
    public void getGenericInformationQueryString() throws Exception {
        GenericInformation genericInformation = new GenericInformation.Builder().build();
        assertThat(genericInformation.getQueryString(), is(GENERIC_INFO));
    }

    @Test
    public void getGenericInformationPartialQueryString() throws Exception {
        GenericInformation genericInformation = new GenericInformation.Builder().excludeValue().build();
        assertThat(genericInformation.getQueryString(), is(GENERIC_INFO_PARTIAL));
    }

    @Test
    public void getVariablesQueryString() throws Exception {
        Variables variables = new Variables.Builder().build();
        assertThat(variables.getQueryString(), is(VAR));
    }

    @Test
    public void getVariablesPartialQueryString() throws Exception {
        Variables variables = new Variables.Builder().excludeValue().build();
        assertThat(variables.getQueryString(), is(VAR_PARTIAL));
    }

}