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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.ow2.proactive.scheduling.api.graphql.beans.input.JobInput;
import org.ow2.proactive.scheduling.api.graphql.beans.input.Jobs;
import org.ow2.proactive.scheduling.api.graphql.beans.input.KeyValueInput;
import org.ow2.proactive.scheduling.api.graphql.beans.input.Query;
import org.ow2.proactive.scheduling.api.graphql.beans.input.Variables;
import org.ow2.proactive.scheduling.api.graphql.beans.output.SchedulingApiResponse;


@Ignore
public class SchedulingApiClientTest {

    private static final String url = "http://localhost:8080/scheduling-api/v1/graphql";

    private static final String CONTEXT_LOGIN = "admin";

    private static final String CONTEXT_SESSION_ID = "sessionId";

    @Test
    public void testExecute() {
        SchedulingApiClient client = new SchedulingApiClient(url, CONTEXT_SESSION_ID);
        List<JobInput> inputs = new ArrayList<>();
        inputs.add(new JobInput.Builder().status(Arrays.asList("PENDING", "KILLED", "FINISHED")).build());
        Jobs jobs = new Jobs.Builder().input(inputs).build();
        Query query = new Query.Builder().query(jobs.getQueryString()).build();
        SchedulingApiResponse result = client.execute(query);
        System.out.println(result);
    }

}
