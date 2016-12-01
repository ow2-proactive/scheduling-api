/*
 * ProActive Parallel Suite(TM):
 * The Java(TM) library for Parallel, Distributed,
 * Multi-Core Computing for Enterprise Grids & Clouds
 *
 * Copyright (c) 2016 ActiveEon
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