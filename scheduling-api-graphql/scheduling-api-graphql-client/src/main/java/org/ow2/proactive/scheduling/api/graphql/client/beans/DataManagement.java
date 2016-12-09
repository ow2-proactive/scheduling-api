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

import lombok.Data;

import org.ow2.proactive.scheduling.api.graphql.common.Fields;


/**
 * @author ActiveEon Team
 */
@Data
public class DataManagement implements ApiType {

    private final String queryString;

    private DataManagement(String queryString) {
        this.queryString = queryString;
    }

    public static class Builder {

        private boolean globalSpaceUrl = true;

        private boolean inputSpaceUrl = true;

        private boolean outputSpaceUrl = true;

        private boolean userSpaceUrl = true;

        private StringBuilder sb = new StringBuilder();

        public Builder excludeGlobalSpaceUrl() {
            this.globalSpaceUrl = false;
            return this;
        }

        public Builder excludeInputSpaceUrl() {
            this.inputSpaceUrl = false;
            return this;
        }

        public Builder excludeOutputSpaceUrl() {
            this.outputSpaceUrl = false;
            return this;
        }

        public Builder excludeUserSpaceUrl() {
            this.userSpaceUrl = false;
            return this;
        }

        public DataManagement build() {
            sb.append(Fields.DATA_MANAGEMENT.getName());
            sb.append("{").append(Constants.RETURN);
            if (globalSpaceUrl) {
                sb.append(Fields.GLOBAL_SPACE_URL.getName()).append(Constants.RETURN);
            }
            if (inputSpaceUrl) {
                sb.append(Fields.INPUT_SPACE_URL.getName()).append(Constants.RETURN);
            }
            if (outputSpaceUrl) {
                sb.append(Fields.OUTPUT_SPACE_URL.getName()).append(Constants.RETURN);
            }
            if (userSpaceUrl) {
                sb.append(Fields.USER_SPACE_URL.getName()).append(Constants.RETURN);
            }
            sb.append("}").append(Constants.RETURN);
            return new DataManagement(sb.toString());
        }
    }

}
