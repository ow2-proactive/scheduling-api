package org.ow2.proactive.scheduling.api.client.v2;

import org.junit.Ignore;
import org.junit.Test;
import org.ow2.proactive.scheduling.api.beans.v2.SchedulingApiResponse;
import org.ow2.proactive.scheduling.api.client.v2.beans.Jobs;
import org.ow2.proactive.scheduling.api.client.v2.beans.Query;

@Ignore
public class SchedulingApiClientTest {

    private static final String url = "http://localhost:9999/v2/graphql";

    private static final String CONTEXT_LOGIN = "bobot";

    private static final String CONTEXT_SESSION_ID = "sessionId";

    @Test
    public void testExecute() {
        SchedulingApiClient client = new SchedulingApiClient(url, "");
        Jobs jobs = new Jobs.Builder().build();
        Query query = new Query.Builder().query(jobs.getQueryString()).build();
        SchedulingApiResponse result = client.execute(query);
        System.out.println(result);
    }

}