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

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import org.ow2.proactive.scheduling.api.graphql.common.Fields;


/**
 * @author ActiveEon Team
 */
@Data
public class Tasks implements ApiType {

    private final String queryString;

    private Tasks(String query) {
        this.queryString = query;
    }

    public static class Builder extends JobsTasksCommonBuilder {

        private boolean executionDuration = true;

        private boolean executionHostName = true;

        /**
         * query filter input list
         */
        private List<TaskInput> input = new ArrayList<>();

        private boolean jobId = true;

        private boolean numberOfExecutionLeft = true;

        private boolean numberOfExecutionOnFailureLeft = true;

        private boolean progress = true;

        private boolean restartMode = true;

        private boolean scheduledTime = true;

        private boolean tag = true;

        @Override
        public Builder after(String after) {
            super.after(after);
            return this;
        }

        @Override
        public Builder before(String before) {
            super.before(before);
            return this;
        }

        @Override
        public Builder excludeCursor() {
            super.excludeCursor();
            return this;
        }

        @Override
        public Builder excludeDescription() {
            super.excludeDescription();
            return this;
        }

        public Tasks.Builder excludeExecutionDuration() {
            this.description = false;
            return this;
        }

        public Tasks.Builder excludeExecutionHostName() {
            this.description = false;
            return this;
        }

        @Override
        public Builder excludeFinishedTime() {
            super.excludeFinishedTime();
            return this;
        }

        @Override
        public Builder excludeGenericInformation() {
            super.excludeGenericInformation();
            return this;
        }

        @Override
        public Builder excludeId() {
            super.excludeId();
            return this;
        }

        @Override
        public Builder excludeInErrorTime() {
            super.excludeInErrorTime();
            return this;
        }

        public Tasks.Builder excludeJobId() {
            this.jobId = false;
            return this;
        }

        @Override
        public Builder excludeMaxNumberOfExecution() {
            super.excludeMaxNumberOfExecution();
            return this;
        }

        @Override
        public Tasks.Builder excludeName() {
            super.excludeName();
            return this;
        }

        public Tasks.Builder excludeNumberOfExecutionLeft() {
            this.numberOfExecutionLeft = false;
            return this;
        }

        public Tasks.Builder excludeNumberOfExecutionOnFailureLeft() {
            this.numberOfExecutionOnFailureLeft = false;
            return this;
        }

        @Override
        public Builder excludeOnTaskError() {
            super.excludeOnTaskError();
            return this;
        }

        @Override
        public Builder excludePageInfo() {
            super.excludePageInfo();
            return this;
        }

        public Tasks.Builder excludeProgress() {
            this.progress = false;
            return this;
        }

        public Tasks.Builder excludeRestartMode() {
            this.restartMode = false;
            return this;
        }

        public Tasks.Builder excludeScheduledTime() {
            this.scheduledTime = false;
            return this;
        }

        @Override
        public Builder excludeStartTime() {
            super.excludeStartTime();
            return this;
        }

        @Override
        public Builder excludeStatus() {
            super.excludeStatus();
            return this;
        }

        public Tasks.Builder excludeTag() {
            this.tag = false;
            return this;
        }

        @Override
        public Builder excludeVariables() {
            super.excludeVariables();
            return this;
        }

        @Override
        public Builder first(Integer first) {
            super.first(first);
            return this;
        }

        @Override
        public Builder genericInformation(GenericInformation genericInformation) {
            super.genericInformation(genericInformation);
            return this;
        }

        public Builder last(Integer last) {
            super.last(last);
            return this;
        }

        @Override
        public Builder pageInfo(PageInfo pageInfo) {
            super.pageInfo(pageInfo);
            return this;
        }

        @Override
        public Builder variables(Variables variables) {
            super.variables(variables);
            return this;
        }

        public Tasks build() {
            this.build(() -> Fields.TASKS.getName(), input);

            if (executionDuration) {
                sb.append(Fields.EXECUTION_DURATION.getName()).append(Constants.RETURN);
            }
            if (executionHostName) {
                sb.append(Fields.EXECUTION_HOST_NAME.getName()).append(Constants.RETURN);
            }
            if (jobId) {
                sb.append(Fields.JOB_ID.getName()).append(Constants.RETURN);
            }
            if (numberOfExecutionLeft) {
                sb.append(Fields.NUMBER_OF_EXECUTION_LEFT.getName()).append(Constants.RETURN);
            }
            if (numberOfExecutionOnFailureLeft) {
                sb.append(Fields.NUMBER_OF_EXECUTION_ON_FAILURE_LEFT.getName()).append(Constants.RETURN);
            }
            if (scheduledTime) {
                sb.append(Fields.SCHEDULED_TIME.getName()).append(Constants.RETURN);
            }
            if (restartMode) {
                sb.append(Fields.RESTART_MODE.getName()).append(Constants.RETURN);
            }
            if (tag) {
                sb.append(Fields.TAG.getName()).append(Constants.RETURN);
            }

            sb.append("}")
              .append(Constants.RETURN)
              .append("}")
              .append(Constants.RETURN)
              .append("}")
              .append(Constants.RETURN);
            return new Tasks(sb.toString());
        }
    }

}
