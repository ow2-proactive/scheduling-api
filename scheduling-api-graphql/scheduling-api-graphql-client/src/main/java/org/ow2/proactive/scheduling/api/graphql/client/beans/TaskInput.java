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
package org.ow2.proactive.scheduling.api.graphql.client.beans;

import org.ow2.proactive.scheduling.api.graphql.common.Fields;
import com.google.common.base.Strings;
import lombok.Data;

/**
 * @author ActiveEon Team
 */
@Data
public class TaskInput implements ApiType {

    private final String queryString;

    private TaskInput(String queryString) {
        this.queryString = queryString;
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
                sb.append(this.taskName);
                sb.append(Constants.QUOTE);
                sb.append(" ");
            }
            if (!Strings.isNullOrEmpty(this.status)) {
                sb.append(' ');
                sb.append(Fields.STATUS.getName());
                sb.append(" : ");
                sb.append(Constants.QUOTE);
                sb.append(this.status);
                sb.append(Constants.QUOTE);
            }
            sb.append(" }");
            return new TaskInput(sb.toString());
        }
    }

}
