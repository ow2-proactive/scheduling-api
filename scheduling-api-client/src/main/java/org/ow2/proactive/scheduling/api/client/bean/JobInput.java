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
package org.ow2.proactive.scheduling.api.client.bean;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import static org.ow2.proactive.scheduling.api.client.bean.Constants.QUOTE;

@Data
public class JobInput implements ApiType {
    private final String queryString;

    private JobInput(String queryString) {
        this.queryString = queryString;
    }

    public static class Builder {
        private String id;
        private String jobName;
        private String owner;
        private String priority;
        private String projectName;
        private String status;

        private StringBuilder sb = new StringBuilder();

        public JobInput.Builder id(String id) {
            this.id = id;
            return this;
        }

        public JobInput.Builder jobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public JobInput.Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public JobInput.Builder priority(String priority) {
            this.priority = priority;
            return this;
        }

        public JobInput.Builder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public JobInput.Builder status(String status) {
            this.status = status;
            return this;
        }

        public JobInput build() {
            sb.append("{");
            if (StringUtils.isNotBlank(this.id)) {
                sb.append(" id : ");
                sb.append(this.id);
            }
            if (StringUtils.isNotBlank(this.jobName)) {
                sb.append(" jobName : ").append(QUOTE);
                sb.append(this.jobName).append(QUOTE);
            }
            if (StringUtils.isNotBlank(this.owner)) {
                sb.append(" owner : ").append(QUOTE);
                sb.append(this.owner).append(QUOTE);
            }
            if (StringUtils.isNotBlank(this.priority)) {
                sb.append(" priority : ").append(QUOTE);
                sb.append(this.priority).append(QUOTE);
            }
            if (StringUtils.isNotBlank(this.projectName)) {
                sb.append(" projectName : ").append(QUOTE);
                sb.append(this.projectName).append(QUOTE);
            }
            if (StringUtils.isNotBlank(this.status)) {
                sb.append(" status : ").append(QUOTE);
                sb.append(this.status).append(QUOTE);
            }
            sb.append(" }");
            return new JobInput(sb.toString());
        }
    }
}
