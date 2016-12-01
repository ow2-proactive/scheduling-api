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
import lombok.EqualsAndHashCode;

/**
 * @author ActiveEon Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Variables extends KeyValue {

    public Variables(String queryString) {
        super(queryString);
    }

    public static class Builder extends KeyValue.Builder {

        @Override
        public String getKeyValueBeanName() {
            return Fields.VARIABLES.getName();
        }

        @Override
        public Variables build() {
            // FIXME, not really implemented yet
            return new Variables(buildQueryString());
        }
    }

}
