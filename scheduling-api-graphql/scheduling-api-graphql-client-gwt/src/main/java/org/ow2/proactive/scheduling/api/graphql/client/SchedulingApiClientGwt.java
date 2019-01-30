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

import javax.ws.rs.WebApplicationException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.ow2.proactive.scheduling.api.graphql.beans.input.Query;
import org.ow2.proactive.scheduling.api.graphql.client.exception.SchedulingApiGwtException;


public class SchedulingApiClientGwt {

    private final GraphQLClient graphQLClientProxy;

    public SchedulingApiClientGwt(String url, CloseableHttpClient httpClient, ExecutorService threadPool) {
        ResteasyClient client = new ResteasyClientBuilder().asyncExecutor(threadPool)
                                                           .httpEngine(new ApacheHttpClient4Engine(httpClient))
                                                           .build();
        ResteasyWebTarget target = client.target(url);
        this.graphQLClientProxy = target.proxy(GraphQLClient.class);
    }

    public Map<String, Object> execute(String sessionId, Query query, String variables)
            throws SchedulingApiGwtException {

        if (query == null)
            return null;

        try {
            Map<String, Object> queryResult = graphQLClientProxy.graphqlQuery(sessionId, query.getQuery(), variables);

            return queryResult;
        } catch (WebApplicationException e) {
            throw new SchedulingApiGwtException(e);
        }
    }

    public Map<String, Object> execute(String sessionId, Query query) throws SchedulingApiGwtException {
        return execute(sessionId, query, null);
    }

}
