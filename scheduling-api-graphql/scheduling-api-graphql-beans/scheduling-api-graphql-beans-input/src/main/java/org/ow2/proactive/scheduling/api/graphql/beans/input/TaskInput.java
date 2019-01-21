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

import org.apache.commons.lang3.StringEscapeUtils;
import org.ow2.proactive.scheduling.api.graphql.common.Fields;

import com.google.common.base.Strings;


/**
 * @author ActiveEon Team
 */
public class TaskInput extends AbstractApiType {

    private TaskInput(String queryString) {
        super(queryString);
    }

    public static class Builder {

        private String id;

        private String taskName;

        private String status;

        private StringBuilder sb = new StringBuilder();

        public TaskInput.Builder id(String id) {
            this.id = id;
            return this;
        }

        public TaskInput.Builder taskName(String taskName) {
            this.taskName = taskName;
            return this;
        }

        public TaskInput.Builder status(String status) {
            this.status = status;
            return this;
        }

        public TaskInput build() {
            sb.append("{");
            if (!Strings.isNullOrEmpty(this.id)) {
                sb.append(' ');
                sb.append(Fields.ID.getName());
                sb.append(" : ");
                sb.append(this.id);
            }
            if (!Strings.isNullOrEmpty(this.taskName)) {
                sb.append(' ');
                sb.append(Fields.NAME.getName());
                sb.append(" : ");
                sb.append(Constants.QUOTE);
                sb.append(StringEscapeUtils.escapeJson(this.taskName));
                sb.append(Constants.QUOTE);
                sb.append(" ");
            }
            if (!Strings.isNullOrEmpty(this.status)) {
                sb.append(' ');
                sb.append(Fields.STATUS.getName());
                sb.append(" : ");
                sb.append(Constants.QUOTE);
                sb.append(StringEscapeUtils.escapeJson(this.status));
                sb.append(Constants.QUOTE);
            }
            sb.append(" }");
            return new TaskInput(sb.toString());
        }
    }

}
