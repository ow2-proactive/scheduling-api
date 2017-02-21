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
package org.ow2.proactive.scheduling.api.graphql.beans.input;

import java.util.ArrayList;
import java.util.List;

import org.ow2.proactive.scheduling.api.graphql.common.Fields;


/**
 * @author ActiveEon Team
 */
public abstract class KeyValue extends AbstractApiType {

    KeyValue(String queryString) {
        super(queryString);
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    public static abstract class Builder {

        private boolean key = true;

        private boolean value = true;

        private List<KeyValueInput> input = new ArrayList<>();

        protected StringBuilder sb = new StringBuilder();

        public Builder excludeKey() {
            this.key = false;
            return this;
        }

        public Builder excludeValue() {
            this.value = false;
            return this;
        }

        public Builder input(List<KeyValueInput> input) {
            this.input = input;
            return this;
        }

        protected abstract String getKeyValueBeanName();

        protected String buildQueryString() {
            sb.append(getKeyValueBeanName());
            sb.append(Inputs.buildQueryString(null, null, null, null, input));
            sb.append("{").append(Constants.RETURN);
            if (key) {
                sb.append(Fields.KEY.getName()).append(Constants.RETURN);
            }
            if (value) {
                sb.append(Fields.VALUE.getName()).append(Constants.RETURN);
            }
            sb.append("}").append(Constants.RETURN);
            return sb.toString();
        }

        public abstract <T extends KeyValue> T build();

    }

}
