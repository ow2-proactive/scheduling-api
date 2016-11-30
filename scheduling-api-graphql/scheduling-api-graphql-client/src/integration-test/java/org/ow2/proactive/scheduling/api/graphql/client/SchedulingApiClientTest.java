package org.ow2.proactive.scheduling.api.graphql.client;

import org.ow2.proactive.scheduling.api.graphql.beans.SchedulingApiResponse;
import org.ow2.proactive.scheduling.api.graphql.client.beans.Jobs;
import org.ow2.proactive.scheduling.api.graphql.client.beans.Query;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SchedulingApiClientTest {

    private static final String url = "http://localhost:9999/v1/graphql";

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