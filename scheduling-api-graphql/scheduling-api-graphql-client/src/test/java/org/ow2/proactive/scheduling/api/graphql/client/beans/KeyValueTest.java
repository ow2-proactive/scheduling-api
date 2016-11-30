package org.ow2.proactive.scheduling.api.graphql.client.beans;

import org.ow2.proactive.scheduling.api.graphql.common.Fields;
import com.google.common.truth.Truth;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.GENERIC_INFORMATION;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.KEY;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VALUE;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VARIABLES;


public class KeyValueTest {

    private static final String GENERIC_INFO = GENERIC_INFORMATION.getName() + "{\n" + KEY.getName() + "\n" + VALUE.getName() + "\n}\n";

    private static final String GENERIC_INFO_PARTIAL = GENERIC_INFORMATION.getName() + "{\n" + KEY.getName() + "\n}\n";

    private static final String VAR = VARIABLES.getName() + "{\n" + KEY.getName() + "\n" + VALUE.getName() + "\n}\n";

    private static final String VAR_PARTIAL = VARIABLES.getName() + "{\n" + KEY.getName() + "\n}\n";

    @Test
    public void getGenericInformationQueryString() throws Exception {
        GenericInformation genericInformation = new GenericInformation.Builder().build();
        assertThat(genericInformation.getQueryString()).isEqualTo(GENERIC_INFO);
    }

    @Test
    public void getGenericInformationPartialQueryString() throws Exception {
        GenericInformation genericInformation = new GenericInformation.Builder().excludeValue().build();
        assertThat(genericInformation.getQueryString()).isEqualTo(GENERIC_INFO_PARTIAL);
    }

    @Test
    public void getVariablesQueryString() throws Exception {
        Variables variables = new Variables.Builder().build();
        assertThat(variables.getQueryString()).isEqualTo(VAR);
    }

    @Test
    public void getVariablesPartialQueryString() throws Exception {
        Variables variables = new Variables.Builder().excludeValue().build();
        assertThat(variables.getQueryString()).isEqualTo(VAR_PARTIAL);
    }

}