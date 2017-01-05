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
package org.ow2.proactive.scheduling.api.graphql.client.beans;

import org.ow2.proactive.scheduling.api.graphql.common.Fields;

import lombok.Data;


/**
 * @author ActiveEon Team
 */
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
            sb.append(Fields.PAGE_INFO.getName());
            sb.append("{").append(Constants.RETURN);
            if (hasNextPage) {
                sb.append(Fields.HAS_NEXT_PAGE.getName()).append(Constants.RETURN);
            }
            if (hasPreviousPage) {
                sb.append(Fields.HAS_PREVIOUS_PAGE.getName()).append(Constants.RETURN);
            }
            if (startCursor) {
                sb.append(Fields.START_CURSOR.getName()).append(Constants.RETURN);
            }
            if (endCursor) {
                sb.append(Fields.END_CURSOR.getName()).append(Constants.RETURN);
            }
            sb.append("}").append(Constants.RETURN);
            return new PageInfo(sb.toString());
        }

    }
}
