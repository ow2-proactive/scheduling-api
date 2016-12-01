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
import com.google.common.base.Strings;
import lombok.Data;

import static org.ow2.proactive.scheduling.api.graphql.client.beans.Constants.QUOTE;

/**
 * @author ActiveEon Team
 */
@Data
public class KeyValueInput implements ApiType {

    private final String queryString;

    private KeyValueInput(String queryString) {
        this.queryString = queryString;
    }

    public static class Builder {

        private String key;
        private String value;

        private StringBuilder sb = new StringBuilder();

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public KeyValueInput build() {
            sb.append("{");
            if (!Strings.isNullOrEmpty(this.key)) {
                sb.append(' ');
                sb.append(Fields.KEY.getName());
                sb.append(" : ");
                sb.append(QUOTE);
                sb.append(this.key);
                sb.append(QUOTE);
            }
            if (!Strings.isNullOrEmpty(this.value)) {
                sb.append(' ');
                sb.append(Fields.VALUE.getName());
                sb.append(" : ").append(QUOTE);
                sb.append(this.value).append(QUOTE);
            }
            sb.append(" }");
            return new KeyValueInput(sb.toString());
        }

    }
}
