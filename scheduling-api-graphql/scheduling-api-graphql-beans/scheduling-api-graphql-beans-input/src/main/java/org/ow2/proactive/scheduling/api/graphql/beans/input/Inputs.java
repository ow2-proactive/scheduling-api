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

import java.util.List;

import org.ow2.proactive.scheduling.api.graphql.common.Arguments;

import com.google.common.base.Strings;


/**
 * @author ActiveEon Team
 */
public class Inputs {

    private Inputs() {
    }

    public static final String buildQueryString(String after, String before, Integer first, Integer last,
            List<? extends ApiType> input) {

        if (!input.isEmpty() || !Strings.isNullOrEmpty(after) || !Strings.isNullOrEmpty(before) || first != null ||
            last != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            if (!Strings.isNullOrEmpty(after)) {
                sb.append(' ')
                  .append(Arguments.AFTER.getName())
                  .append(':')
                  .append(Constants.QUOTE)
                  .append(after)
                  .append(Constants.QUOTE);
            }
            if (!Strings.isNullOrEmpty(before)) {
                sb.append(' ')
                  .append(Arguments.BEFORE.getName())
                  .append(':')
                  .append(Constants.QUOTE)
                  .append(before)
                  .append(Constants.QUOTE);
            }
            if (first != null) {
                sb.append(' ').append(Arguments.FIRST.getName()).append(':').append(first);
            }
            if (last != null) {
                sb.append(' ').append(Arguments.LAST.getName()).append(':').append(last);
            }
            if (!input.isEmpty()) {
                sb.append(' ').append(Arguments.FILTER.getName()).append(" : [");

                for (int i = 0; i < input.size(); i++) {
                    ApiType apiType = input.get(i);

                    sb.append(apiType.getQueryString());

                    if (i < input.size() - 1) {
                        sb.append(',');
                    }
                }

                sb.append("]");
            }
            sb.append(" )");
            return sb.toString();
        }
        return "";
    }

}
