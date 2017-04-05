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

import org.ow2.proactive.scheduling.api.graphql.common.Arguments;
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

        private boolean excludeRemoved = true;

        private String id;

        private String jobName;

        private String owner;

        private String priority;

        private String projectName;

        private String status;

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
            if (!Strings.isNullOrEmpty(this.jobName)) {
                sb.append(' ');
                sb.append(InputFields.NAME.getName());
                sb.append(" : ");
                sb.append(Constants.QUOTE);
                sb.append(this.jobName);
                sb.append(Constants.QUOTE);
            }

            timeString(InputFields.LAST_UPDATED_TIME.getName(), this.beforeLastUpdatedTime, this.afterLastUpdatedTime);

            if (!Strings.isNullOrEmpty(this.owner)) {
                sb.append(' ');
                sb.append(InputFields.OWNER.getName());
                sb.append(" : ");
                sb.append(Constants.QUOTE);
                sb.append(this.owner);
                sb.append(Constants.QUOTE);
            }
            if (!Strings.isNullOrEmpty(this.priority)) {
                sb.append(' ');
                sb.append(InputFields.PRIORITY.getName());
                sb.append(" : ");
                sb.append(Constants.QUOTE);
                sb.append(this.priority);
                sb.append(Constants.QUOTE);
            }
            if (!Strings.isNullOrEmpty(this.projectName)) {
                sb.append(' ');
                sb.append(InputFields.PROJECT_NAME.getName());
                sb.append(" : ");
                sb.append(Constants.QUOTE);
                sb.append(this.projectName);
                sb.append(Constants.QUOTE);
            }
            if (!Strings.isNullOrEmpty(this.status)) {
                sb.append(' ');
                sb.append(InputFields.STATUS.getName());
                sb.append(" : ");
                sb.append(this.status);
            }

            timeString(InputFields.SUBMITTED_TIME.getName(), this.beforeSubmittedTime, this.afterSubmittedTime);

            sb.append(" }");
            return new JobInput(sb.toString());
        }

        private void timeString(String timeName, String timeBeforeValue, String timeAfterValue) {
            if (!Strings.isNullOrEmpty(timeBeforeValue) || !Strings.isNullOrEmpty(timeAfterValue)) {
                sb.append(' ');
                sb.append(timeName);
                sb.append(" : {");
                if (!Strings.isNullOrEmpty(timeBeforeValue)) {
                    sb.append(String.format(" %s : ", Arguments.BEFORE.getName())).append(timeBeforeValue);
                }
                if (!Strings.isNullOrEmpty(timeAfterValue)) {
                    sb.append(String.format(" %s : ", Arguments.AFTER.getName())).append(timeAfterValue);
                }
                sb.append(" }").append(Constants.RETURN);
            }
        }
    }

}
