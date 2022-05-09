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

import org.apache.commons.lang3.StringEscapeUtils;
import org.ow2.proactive.scheduling.api.graphql.common.Fields;
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

        private String afterStartTime;

        private String afterFinishedTime;

        private String beforeLastUpdatedTime;

        private String beforeSubmittedTime;

        private String beforeStartTime;

        private String beforeFinishedTime;

        private String afterId;

        private String beforeId;

        private boolean excludeRemoved = true;

        private String id;

        private String jobName;

        private String owner;

        private String tenant;

        private String priority;

        private String projectName;

        private String status;

        private String variableName;

        private String variableValue;

        private String afterNumberOfPendingTasks;

        private String beforeNumberOfPendingTasks;

        private String afterNumberOfRunningTasks;

        private String beforeNumberOfRunningTasks;

        private String afterNumberOfFinishedTasks;

        private String beforeNumberOfFinishedTasks;

        private String afterNumberOfFaultyTasks;

        private String beforeNumberOfFaultyTasks;

        private String afterNumberOfFailedTasks;

        private String beforeNumberOfFailedTasks;

        private String afterNumberOfInErrorTasks;

        private String beforeNumberOfInErrorTasks;

        private StringBuilder sb = new StringBuilder();

        public JobInput.Builder afterLastUpdatedTime(String afterLastUpdatedTime) {
            this.afterLastUpdatedTime = afterLastUpdatedTime;
            return this;
        }

        public JobInput.Builder afterSubmittedTime(String afterSubmittedTime) {
            this.afterSubmittedTime = afterSubmittedTime;
            return this;
        }

        public JobInput.Builder afterStartTime(String afterStartTime) {
            this.afterStartTime = afterStartTime;
            return this;
        }

        public JobInput.Builder afterFinishedTime(String afterFinishedTime) {
            this.afterFinishedTime = afterFinishedTime;
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

        public JobInput.Builder beforeStartTime(String beforeStartTime) {
            this.beforeStartTime = beforeStartTime;
            return this;
        }

        public JobInput.Builder beforeFinishedTime(String beforeFinishedTime) {
            this.beforeFinishedTime = beforeFinishedTime;
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

        public JobInput.Builder afterNumberOfPendingTasks(String afterNumberOfPendingTasks) {
            this.afterNumberOfPendingTasks = afterNumberOfPendingTasks;
            return this;
        }

        public JobInput.Builder beforeNumberOfPendingTasks(String beforeNumberOfPendingTasks) {
            this.beforeNumberOfPendingTasks = beforeNumberOfPendingTasks;
            return this;
        }

        public JobInput.Builder afterNumberOfRunningTasks(String afterNumberOfRunningTasks) {
            this.afterNumberOfRunningTasks = afterNumberOfRunningTasks;
            return this;
        }

        public JobInput.Builder beforeNumberOfRunningTasks(String beforeNumberOfRunningTasks) {
            this.beforeNumberOfRunningTasks = beforeNumberOfRunningTasks;
            return this;
        }

        public JobInput.Builder afterNumberOfFinishedTasks(String afterNumberOfFinishedTasks) {
            this.afterNumberOfFinishedTasks = afterNumberOfFinishedTasks;
            return this;
        }

        public JobInput.Builder beforeNumberOfFinishedTasks(String beforeNumberOfFinishedTasks) {
            this.beforeNumberOfFinishedTasks = beforeNumberOfFinishedTasks;
            return this;
        }

        public JobInput.Builder afterNumberOfFaultyTasks(String afterNumberOfFaultyTasks) {
            this.afterNumberOfFaultyTasks = afterNumberOfFaultyTasks;
            return this;
        }

        public JobInput.Builder beforeNumberOfFaultyTasks(String beforeNumberOfFaultyTasks) {
            this.beforeNumberOfFaultyTasks = beforeNumberOfFaultyTasks;
            return this;
        }

        public JobInput.Builder afterNumberOfFailedTasks(String afterNumberOfFailedTasks) {
            this.afterNumberOfFailedTasks = afterNumberOfFailedTasks;
            return this;
        }

        public JobInput.Builder beforeNumberOfFailedTasks(String beforeNumberOfFailedTasks) {
            this.beforeNumberOfFailedTasks = beforeNumberOfFailedTasks;
            return this;
        }

        public JobInput.Builder afterNumberOfInErrorTasks(String afterNumberOfInErrorTasks) {
            this.afterNumberOfInErrorTasks = afterNumberOfInErrorTasks;
            return this;
        }

        public JobInput.Builder beforeNumberOfInErrorTasks(String beforeNumberOfInErrorTasks) {
            this.beforeNumberOfInErrorTasks = beforeNumberOfInErrorTasks;
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

        public JobInput.Builder tenant(String tenant) {
            this.tenant = tenant;
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

        public JobInput.Builder status(List<String> status) {
            this.status = status.toString();
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

            if (!Strings.isNullOrEmpty(this.owner)) {
                sb.append(' ');
                sb.append(InputFields.OWNER.getName());
                sb.append(" : ");
                sb.append(Constants.QUOTE);
                sb.append(StringEscapeUtils.escapeJson(this.owner));
                sb.append(Constants.QUOTE);
            }
            if (!Strings.isNullOrEmpty(this.tenant)) {
                sb.append(' ');
                sb.append(InputFields.TENANT.getName());
                sb.append(" : ");
                sb.append(Constants.QUOTE);
                sb.append(StringEscapeUtils.escapeJson(this.tenant));
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
                sb.append(Fields.STATUS.getName());
                sb.append(" : ");
                sb.append(this.status);
            }

            comparableLongString(InputFields.SUBMITTED_TIME.getName(),
                                 this.beforeSubmittedTime,
                                 this.afterSubmittedTime);

            comparableLongString(InputFields.LAST_UPDATED_TIME.getName(),
                                 this.beforeLastUpdatedTime,
                                 this.afterLastUpdatedTime);

            comparableLongString(InputFields.START_TIME.getName(), this.beforeStartTime, this.afterStartTime);

            comparableLongString(InputFields.FINISHED_TIME.getName(), this.beforeFinishedTime, this.afterFinishedTime);

            comparableLongString(InputFields.NUMBER_OF_PENDING_TASKS.getName(),
                                 this.beforeNumberOfPendingTasks,
                                 this.afterNumberOfPendingTasks);

            comparableLongString(InputFields.NUMBER_OF_RUNNING_TASKS.getName(),
                                 this.beforeNumberOfRunningTasks,
                                 this.afterNumberOfRunningTasks);

            comparableLongString(InputFields.NUMBER_OF_FINISHED_TASKS.getName(),
                                 this.beforeNumberOfFinishedTasks,
                                 this.afterNumberOfFinishedTasks);

            comparableLongString(InputFields.NUMBER_OF_FAULTY_TASKS.getName(),
                                 this.beforeNumberOfFaultyTasks,
                                 this.afterNumberOfFaultyTasks);

            comparableLongString(InputFields.NUMBER_OF_FAILED_TASKS.getName(),
                                 this.beforeNumberOfFailedTasks,
                                 this.afterNumberOfFailedTasks);

            comparableLongString(InputFields.NUMBER_OF_IN_ERROR_TASKS.getName(),
                                 this.beforeNumberOfInErrorTasks,
                                 this.afterNumberOfInErrorTasks);

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
