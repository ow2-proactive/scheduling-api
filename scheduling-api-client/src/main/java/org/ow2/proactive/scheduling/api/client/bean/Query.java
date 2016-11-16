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

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Query {

    private static final String KEY_OPERATION_NAME = "operationName";

    private static final String KEY_QUERY = "query";

    private static final String KEY_VARIABLES = "variables";

    private final String operationName;

    private final String query;

    private final Map<String, String> queryMap;

    private final String variables;

    private Query(Map<String, String> query) {
        this.operationName = query.get(KEY_OPERATION_NAME);
        this.query = query.get(KEY_QUERY);
        this.queryMap = query;
        this.variables = query.get(KEY_VARIABLES);
    }

    public static class Builder {
        private String operationName;
        private String query;
        private String variables;

        public Builder operationName(String operationName) {
            this.operationName = operationName;
            return this;
        }

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public Builder variables(String variables) {
            this.variables = variables;
            return this;
        }

        public Query build() {
            Map<String, String> result = new LinkedHashMap<>();
            if(this.operationName != null) {
                result.put(KEY_OPERATION_NAME, this.operationName);
            }
            if(this.query != null) {
                result.put(KEY_QUERY, this.query);
            }
            if(this.variables != null) {
                result.put(KEY_VARIABLES, this.variables);
            }
            return new Query(result);
        }

    }

}
