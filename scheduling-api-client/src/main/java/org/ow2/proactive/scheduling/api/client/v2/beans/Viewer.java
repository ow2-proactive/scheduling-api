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
package org.ow2.proactive.scheduling.api.client.v2.beans;

import lombok.Data;

import static org.ow2.proactive.scheduling.api.client.v2.beans.ApiTypeKeyEnum.LOGIN;
import static org.ow2.proactive.scheduling.api.client.v2.beans.ApiTypeKeyEnum.SESSION_ID;
import static org.ow2.proactive.scheduling.api.client.v2.beans.Constants.QUOTE;
import static org.ow2.proactive.scheduling.api.client.v2.beans.Constants.RETURN;

@Data
public class Viewer implements ApiType {
    private final String queryString;

    public Viewer(String queryString) {
        this.queryString = queryString;
    }

    public static class Builder {
        private Jobs jobs = null;
        private boolean login = true;
        private boolean sessionId = true;
        private String sessionIdInput;

        private StringBuilder sb = new StringBuilder();

        public Builder jobs(Jobs jobs) {
            this.jobs = jobs;
            return this;
        }

        public Builder excludeLogin() {
            this.login = false;
            return this;
        }

        public Builder excludeSessionId() {
            this.sessionId = false;
            return this;
        }

        public Builder sessionId(String sessionId) {
            this.sessionIdInput = sessionId;
            return this;
        }

        public Viewer build() {
            sb.append("{").append(RETURN).append("viewer (sessionId : ").append(QUOTE);
            sb.append(sessionIdInput).append(QUOTE).append("{").append(RETURN);
            if (login) {
                sb.append(LOGIN.getKey()).append(RETURN);
            }
            if (sessionId) {
                sb.append(SESSION_ID.getKey()).append(RETURN);
            }
            if (jobs != null) {
                sb.append(jobs.getQueryString());
            }
            sb.append("}").append(RETURN).append("}");

            return new Viewer(sb.toString());
        }
    }
}
