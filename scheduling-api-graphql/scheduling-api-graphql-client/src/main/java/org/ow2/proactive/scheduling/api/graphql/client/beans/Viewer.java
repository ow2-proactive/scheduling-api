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
package org.ow2.proactive.scheduling.api.graphql.client.beans;

import org.ow2.proactive.scheduling.api.graphql.common.Fields;
import lombok.Data;

/**
 * @author ActiveEon Team
 */
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

        public Viewer build() {
            sb.append(Fields.VIEWER).append(" {").append(
                    Constants.RETURN);
            if (login) {
                sb.append(Fields.LOGIN.getName()).append(Constants.RETURN);
            }
            if (sessionId) {
                sb.append(Fields.SESSION_ID.getName()).append(Constants.RETURN);
            }
            if (jobs != null) {
                sb.append(jobs.getQueryString()).append(Constants.RETURN);
            }
            sb.append("}");

            return new Viewer(sb.toString());
        }
    }
}
