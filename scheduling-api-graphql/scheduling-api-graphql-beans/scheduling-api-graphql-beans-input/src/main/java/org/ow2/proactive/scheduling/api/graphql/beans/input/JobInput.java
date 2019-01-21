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
import org.ow2.proactive.scheduling.api.graphql.common.InputFields;

import com.google.common.base.Strings;


/**
 * @author ActiveEon Team
 */
public class JobInput extends AbstractApiType {

    private JobInput(String queryString) {
        super(queryString);
    }

    public static class Builder {

        private String afterLastUpdatedTime;

        private String afterSubmittedTime;

        private String beforeLastUpdatedTime;

        private String beforeSubmittedTime;

        private String afterId;

        private String beforeId;

        private boolean excludeRemoved = true;

        private String id;

        private String jobName;

        private String owner;

        private String priority;

        private String projectName;

        private String status;

        private String variableName;

        private String variableValue;

        private StringBuilder sb = new StringBuilder();

        public JobInput.Builder afterLastUpdatedTime(String afterLastUpdatedTime) {
            this.afterLastUpdatedTime = afterLastUpdatedTime;
            return this;
        }

        public JobInput.Builder afterSubmittedTime(String afterSubmittedTime) {
            this.afterSubmittedTime = afterSubmittedTime;
            return this;
        }

        public JobInput.Builder beforeLastUpdatedTime(String beforeLastUpdatedTime) {
            this.beforeLastUpdatedTime = beforeLastUpdatedTime;
            return this;
        }

        public JobInput.Builder beforeSubmittedTime(String beforeSubmittedTime) {
            this.beforeSubmittedTime = beforeSubmittedTime;
            return this;
        }

        public JobInput.Builder afterId(String afterId) {
            this.afterId = afterId;
            return this;
        }

        public JobInput.Builder beforeId(String beforeId) {
            this.beforeId = beforeId;
            return this;
        }

        public JobInput.Builder isExcludeRemoved(boolean excludeRemoved) {
            this.excludeRemoved = excludeRemoved;
            return this;
        }

        public JobInput.Builder id(String id) {
            this.id = id;
            return this;
        }

        public JobInput.Builder jobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public JobInput.Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public JobInput.Builder variableName(String variableName) {
            this.variableName = variableName;
            return this;
        }

        public JobInput.Builder variableValue(String variableValue) {
            this.variableValue = variableValue;
            return this;
        }

        public JobInput.Builder priority(String priority) {
            this.priority = priority;
            return this;
        }

        public JobInput.Builder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public JobInput.Builder status(String status) {
            this.status = status;
            return this;
        }

        public JobInput build() {
            sb.append("{");
            if (!excludeRemoved) {
                sb.append(' ');
                sb.append(InputFields.EXCLUDE_REMOVED.getName());
                sb.append(" : ");
                sb.append(this.excludeRemoved);
            }
            if (!Strings.isNullOrEmpty(this.id)) {
                sb.append(' ');
                sb.append(InputFields.ID.getName());
                sb.append(" : ");
                sb.append(this.id);
            }
            comparableLongString(InputFields.COMPARABLE_ID.getName(), this.beforeId, this.afterId);
            if (!Strings.isNullOrEmpty(this.jobName)) {
                sb.append(' ');
                sb.append(InputFields.NAME.getName());
                sb.append(" : ");
                sb.append(Constants.QUOTE);
                sb.append(StringEscapeUtils.escapeJson(this.jobName));
                sb.append(Constants.QUOTE);
            }

            comparableLongString(InputFields.LAST_UPDATED_TIME.getName(),
                                 this.beforeLastUpdatedTime,
                                 this.afterLastUpdatedTime);

            if (!Strings.isNullOrEmpty(this.owner)) {
                sb.append(' ');
                sb.append(InputFields.OWNER.getName());
                sb.append(" : ");
                sb.append(Constants.QUOTE);
                sb.append(StringEscapeUtils.escapeJson(this.owner));
                sb.append(Constants.QUOTE);
            }
            if (!Strings.isNullOrEmpty(this.priority)) {
                sb.append(' ');
                sb.append(InputFields.PRIORITY.getName());
                sb.append(" : ");
                sb.append(this.priority);
            }
            if (!Strings.isNullOrEmpty(this.projectName)) {
                sb.append(' ');
                sb.append(InputFields.PROJECT_NAME.getName());
                sb.append(" : ");
                sb.append(Constants.QUOTE);
                sb.append(StringEscapeUtils.escapeJson(this.projectName));
                sb.append(Constants.QUOTE);
            }
            if (!Strings.isNullOrEmpty(this.variableName) || !Strings.isNullOrEmpty(this.variableValue)) {
                sb.append(' ');
                sb.append("variables: ");
                sb.append(new KeyValueInput.Builder().key(variableName).value(variableValue).build().getQueryString());
            }
            if (!Strings.isNullOrEmpty(this.status)) {
                sb.append(' ');
                sb.append(InputFields.STATUS.getName());
                sb.append(" : ");
                sb.append(this.status);
            }

            comparableLongString(InputFields.SUBMITTED_TIME.getName(),
                                 this.beforeSubmittedTime,
                                 this.afterSubmittedTime);

            sb.append(" }");
            return new JobInput(sb.toString());
        }

        private void comparableLongString(String longName, String beforeValue, String afterValue) {
            if (!Strings.isNullOrEmpty(beforeValue) || !Strings.isNullOrEmpty(afterValue)) {
                sb.append(' ');
                sb.append(longName);
                sb.append(" : {");
                if (!Strings.isNullOrEmpty(beforeValue)) {
                    sb.append(String.format(" %s : ", InputFields.BEFORE.getName())).append(beforeValue);
                }
                if (!Strings.isNullOrEmpty(afterValue)) {
                    sb.append(String.format(" %s : ", InputFields.AFTER.getName())).append(afterValue);
                }
                sb.append(" }").append(Constants.RETURN);
            }
        }
    }

}
