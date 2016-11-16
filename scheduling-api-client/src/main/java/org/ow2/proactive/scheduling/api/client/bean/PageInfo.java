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

import static org.ow2.proactive.scheduling.api.client.bean.Constants.RETURN;

@Data
public class PageInfo implements ApiType {
    private final String queryString;

    private PageInfo(String queryString) {
        this.queryString = queryString;
    }

    public static class Builder {

        private boolean hasNextPage = true;
        private boolean hasPreviousPage = true;
        private boolean startCursor = true;
        private boolean endCursor = true;

        private StringBuilder sb = new StringBuilder();

        public PageInfo.Builder excludeHasNextPage() {
            this.hasNextPage = false;
            return this;
        }

        public PageInfo.Builder excludeHasPreviousPage() {
            this.hasPreviousPage = false;
            return this;
        }

        public PageInfo.Builder excludeStartCursor() {
            this.startCursor = false;
            return this;
        }

        public PageInfo.Builder excludeEndCursor() {
            this.endCursor = false;
            return this;
        }

        public PageInfo build() {
            sb.append("pageInfo");
            sb.append("{").append(RETURN);
            if(hasNextPage) {
                sb.append("hasNextPage").append(RETURN);
            }
            if(hasPreviousPage) {
                sb.append("hasPreviousPage").append(RETURN);
            }
            if(startCursor) {
                sb.append("startCursor").append(RETURN);
            }
            if(endCursor) {
                sb.append("endCursor").append(RETURN);
            }
            sb.append("}").append(RETURN);
            return new PageInfo(sb.toString());
        }
    }
}
