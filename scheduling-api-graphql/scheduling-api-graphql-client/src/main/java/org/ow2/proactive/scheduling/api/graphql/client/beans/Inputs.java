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

import com.google.common.base.Strings;

import java.util.List;
import java.util.stream.Collectors;

import org.ow2.proactive.scheduling.api.graphql.common.Arguments;


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
                sb.append(String.format(" %s:", Arguments.AFTER.getName()))
                  .append(Constants.QUOTE)
                  .append(after)
                  .append(Constants.QUOTE);
            }
            if (!Strings.isNullOrEmpty(before)) {
                sb.append(String.format(" %s:", Arguments.BEFORE.getName()))
                  .append(Constants.QUOTE)
                  .append(before)
                  .append(Constants.QUOTE);
            }
            if (first != null) {
                sb.append(String.format(" %s:", Arguments.FIRST.getName())).append(first);
            }
            if (last != null) {
                sb.append(String.format(" %s:", Arguments.LAST.getName())).append(last);
            }
            if (!input.isEmpty()) {
                sb.append(String.format(" %s : [", Arguments.FILTER.getName()));
                String inputQuery = input.stream().map(ApiType::getQueryString).collect(Collectors.joining(","));
                sb.append(inputQuery);
                sb.append("]");
            }
            sb.append(" )");
            return sb.toString();
        }
        return "";
    }

}
