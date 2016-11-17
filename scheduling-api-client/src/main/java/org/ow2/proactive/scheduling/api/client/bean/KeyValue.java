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

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import static org.ow2.proactive.scheduling.api.client.bean.ApiTypeKeyEnum.KEY;
import static org.ow2.proactive.scheduling.api.client.bean.ApiTypeKeyEnum.VALUE;

@Data
public abstract class KeyValue implements ApiType {

    protected final String queryString;

    protected KeyValue(String queryString) {
        this.queryString = queryString;
    }

    public static abstract class Builder {

        private boolean key = true;
        private boolean value = true;

        private List<KeyValueInput> input = new ArrayList<>();

        protected StringBuilder sb = new StringBuilder();

        public KeyValue.Builder excludeKey() {
            this.key = false;
            return this;
        }

        public KeyValue.Builder excludeValue() {
            this.value = false;
            return this;
        }

        public KeyValue.Builder input(List<KeyValueInput> input) {
            this.input = input;
            return this;
        }

        protected abstract String getKeyValueBeanName();

        protected String buildQueryString() {
            sb.append(getKeyValueBeanName());
            sb.append(Inputs.buildQueryString(input));
            sb.append("{").append(Constants.RETURN);
            if (key) {
                sb.append(KEY.getKey()).append(Constants.RETURN);
            }
            if (value) {
                sb.append(VALUE.getKey()).append(Constants.RETURN);
            }
            sb.append("}").append(Constants.RETURN);
            return sb.toString();
        }

        public abstract <T extends KeyValue> T build();
    }
}
