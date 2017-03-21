/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package org.ow2.proactive.scheduling.api.graphql.client;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.ow2.proactive.scheduling.api.graphql.beans.input.Jobs;
import org.ow2.proactive.scheduling.api.graphql.beans.input.Query;


@Ignore
public class SchedulingApiClientGwtTest {

    private static final String url = "http://localhost:9999/v1/graphql";

    private static final String CONTEXT_LOGIN = "bobot";

    private static final String CONTEXT_SESSION_ID = "sessionId";

    private static final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(1);

    @Test
    public void testExecute() {
        SchedulingApiClientGwt client = new SchedulingApiClientGwt(url, httpClient, threadPool);
        Jobs jobs = new Jobs.Builder().build();
        Query query = new Query.Builder().query(jobs.getQueryString()).build();
        Map<String, Object> result = client.execute(CONTEXT_SESSION_ID, query);
        System.out.println(result);
    }

}
