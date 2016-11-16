package org.ow2.proactive.scheduling.api.client.bean;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;


public class DataManagementTest {

    private static final String all = "dataManagement{\ngloablSpaceUrl\ninputSpaceUrl\noutputSpaceUrl\nuserSpaceUrl\n}";

    private static final String partial = "dataManagement{\noutputSpaceUrl\nuserSpaceUrl\n}";

    @Test
    public void getQueryString() throws Exception {
        DataManagement dataMgmt = new DataManagement.Builder().build();

        assertThat(all.equals(dataMgmt.getQueryString()));
    }

    @Test
    public void getPartialQueryString() throws Exception {
        DataManagement dataMgmt = new DataManagement.Builder().excludeGlobalSpaceUrl().excludeInputSpaceUrl().build();

        assertThat(partial.equals(dataMgmt.getQueryString()));
    }

}