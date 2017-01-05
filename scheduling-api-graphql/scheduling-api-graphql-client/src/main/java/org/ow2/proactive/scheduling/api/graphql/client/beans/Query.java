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

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;


/**
 * @author ActiveEon Team
 */
@Data
public class Query {

    private static final String KEY_OPERATION_NAME = "operationName";

    private static final String KEY_QUERY = "query";

    private static final String KEY_VARIABLES = "variables";

    private final String operationName;

    private final String query;

    private final Map<String, String> queryMap;

    private Map<String, Object> queryResponse;

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
            this.query = "{ " + query + " }";
            return this;
        }

        public Builder variables(String variables) {
            this.variables = variables;
            return this;
        }

        public Query build() {
            Map<String, String> result = new LinkedHashMap<>();
            result.put(KEY_OPERATION_NAME, this.operationName);
            result.put(KEY_QUERY, this.query);
            result.put(KEY_VARIABLES, this.variables);
            return new Query(result);
        }

    }

}
