package org.ow2.proactive.scheduling.api.graphql.client.beans;

import org.ow2.proactive.scheduling.api.graphql.common.Fields;
import com.google.common.truth.Truth;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.DATA_MANAGEMENT;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.GLOBAL_SPACE_URL;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.INPUT_SPACE_URL;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.OUTPUT_SPACE_URL;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.USER_SPACE_URL;


public class DataManagementTest {

    private static final String ALL = String.format(
            "%s{\n%s\n%s\n%s\n%s\n}\n",
            DATA_MANAGEMENT.getName(), GLOBAL_SPACE_URL.getName(),
            INPUT_SPACE_URL.getName(), OUTPUT_SPACE_URL.getName(),
            USER_SPACE_URL.getName());

    private static final String PARTIAL = String.format("%s{\n%s\n%s\n}\n", DATA_MANAGEMENT.getName(),
            OUTPUT_SPACE_URL.getName(), USER_SPACE_URL.getName());

    @Test
    public void getQueryString() throws Exception {
        DataManagement dataMgmt = new DataManagement.Builder().build();

        assertThat(dataMgmt.getQueryString()).isEqualTo(ALL);
    }

    @Test
    public void getPartialQueryString() throws Exception {
        DataManagement dataMgmt = new DataManagement.Builder().excludeGlobalSpaceUrl().excludeInputSpaceUrl().build();

        assertThat(dataMgmt.getQueryString()).isEqualTo(PARTIAL);
    }

}