/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2015 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 *  * $$ACTIVEEON_INITIAL_DEV$$
 */
package org.ow2.proactive.scheduling.api.client;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.ow2.proactive.scheduling.api.client.bean.Query;
import org.ow2.proactive.scheduling.api.client.bean.QueryResponse;
import org.ow2.proactive.scheduling.api.client.exception.SchedulingApiException;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;


public class SchedulingApiClient {

    private final ResteasyClient client = new ResteasyClientBuilder().build();

    private final String url;

    public SchedulingApiClient(String url) {
        this.url = url;
    }

    public QueryResponse postQuery(Query query) throws SchedulingApiException {
        if(StringUtils.isBlank(url)) {
            throw new SchedulingApiException("API server URL is not initialized");
        }

        try {
            ResteasyWebTarget target = client.target(url);
            return target.request().post(Entity.entity(query.getQueryMap(), MediaType.APPLICATION_JSON_TYPE), QueryResponse.class);
        } catch(Exception e) {
            throw new SchedulingApiException("API server URL is not initialized");
        }
    }

}
