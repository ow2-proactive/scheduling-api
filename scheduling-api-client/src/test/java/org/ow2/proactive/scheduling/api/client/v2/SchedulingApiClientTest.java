package org.ow2.proactive.scheduling.api.client.v2;

import org.ow2.proactive.scheduling.api.beans.v2.SchedulerApiResponse;
import org.ow2.proactive.scheduling.api.client.v2.bean.Jobs;
import org.ow2.proactive.scheduling.api.client.v2.bean.Query;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SchedulingApiClientTest {

    private static final String url = "http://localhost:9999/v2/graphql";

    @Test
    public void testExecute() {
        SchedulingApiClient client = new SchedulingApiClient(url, "");
        Jobs jobs = new Jobs.Builder().build();
        Query query = new Query.Builder().query(jobs.getQueryString()).build();
        SchedulerApiResponse result = client.execute(query);
        System.out.println(result);
    }

}