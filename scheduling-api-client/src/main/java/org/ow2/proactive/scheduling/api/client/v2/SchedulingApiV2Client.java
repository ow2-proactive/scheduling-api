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
package org.ow2.proactive.scheduling.api.client.v2;

import java.util.Map;

import org.ow2.proactive.scheduling.api.client.v2.bean.Query;
import org.ow2.proactive.scheduling.api.client.v2.exception.SchedulingApiException;
import com.google.common.base.Strings;
import org.springframework.web.client.RestTemplate;


public class SchedulingApiV2Client {

    private final RestTemplate client = new RestTemplate();

    private final String url;

    public SchedulingApiV2Client(String url) {
        this.url = url;
    }

    public Query postQuery(Query query) throws SchedulingApiException {
        if (Strings.isNullOrEmpty(url)) {
            throw new SchedulingApiException("API server URL is not initialized");
        }

        try {
            Map<String, Object> result = client.postForObject(url, query.getQueryMap(), Map.class);
            query.setQueryResponse(result);
            return query;
        } catch (Exception e) {
            throw new SchedulingApiException("Exception", e);
        }
    }

}
