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

import org.ow2.proactive.scheduling.api.graphql.common.Fields;
import lombok.Data;

/**
 * @author ActiveEon Team
 */
@Data
public class Jobs implements ApiType {

    private final String queryString;

    private Jobs(String query) {
        this.queryString = query;
    }

    public static class Builder extends JobsTasksCommonBuilder {

        private DataManagement dataManagement = new DataManagement.Builder().build();

        /**
         * query filter input list
         */
        private List<JobInput> input = new ArrayList<>();
        private boolean numberOfFailedTasks = true;
        private boolean numberOfFaultyTasks = true;
        private boolean numberOfFinishedTasks = true;
        private boolean numberOfInErrorTasks = true;
        private boolean numberOfPendingTasks = true;
        private boolean numberOfRunningTasks = true;
        private boolean owner = true;
        private boolean priority = true;
        private boolean projectName = true;
        private boolean removedTime = true;
        private boolean submittedTime = true;
        private Tasks tasks = null;
        private boolean totalNumberOfTasks = true;

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

        public Builder dataManagement(DataManagement dataManagement) {
            this.dataManagement = dataManagement;
            return this;
        }

        @Override
        public Builder excludeCursor() {
            super.excludeCursor();
            return this;
        }

        public Builder excludeDataManagement() {
            this.dataManagement = null;
            return this;
        }

        @Override
        public Builder excludeDescription() {
            super.excludeDescription();
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

        @Override
        public Builder excludeMaxNumberOfExecution() {
            super.excludeMaxNumberOfExecution();
            return this;
        }

        @Override
        public Builder excludeName() {
            super.excludeName();
            return this;
        }

        public Builder excludeNumberOfFailedTasks() {
            this.numberOfFailedTasks = false;
            return this;
        }

        public Builder excludeNumberOfFaultyTasks() {
            this.numberOfFaultyTasks = false;
            return this;
        }

        public Builder excludeNumberOfFinishedTasks() {
            this.numberOfFinishedTasks = false;
            return this;
        }

        public Builder excludeNumberOfInErrorTasks() {
            this.numberOfInErrorTasks = false;
            return this;
        }

        public Builder excludeNumberOfPendingTasks() {
            this.numberOfPendingTasks = false;
            return this;
        }

        public Builder excludeNumberOfRunningTasks() {
            this.numberOfRunningTasks = false;
            return this;
        }

        @Override
        public Builder excludeOnTaskError() {
            super.excludeOnTaskError();
            return this;
        }

        public Builder excludeOwner() {
            this.owner = false;
            return this;
        }

        @Override
        public Builder excludePageInfo() {
            super.excludePageInfo();
            return this;
        }

        public Builder excludePriority() {
            this.priority = false;
            return this;
        }

        public Builder excludeProjectName() {
            this.projectName = false;
            return this;
        }

        public Builder excludeRemovedTime() {
            this.removedTime = false;
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

        @Override
        public Builder excludeVariables() {
            super.excludeVariables();
            return this;
        }

        public Builder excludeSubmittedTime() {
            this.submittedTime = false;
            return this;
        }

        public Builder excludeTotalNumberOfTasks() {
            this.totalNumberOfTasks = false;
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

        public Builder input(List<JobInput> input) {
            this.input = input;
            return this;
        }

        @Override
        public Builder last(Integer last) {
            super.last(last);
            return this;
        }

        @Override
        public Builder pageInfo(PageInfo pageInfo) {
            super.pageInfo(pageInfo);
            return this;
        }

        public Builder tasks(Tasks tasks) {
            this.tasks = tasks;
            return this;
        }

        @Override
        public Builder variables(Variables variables) {
            super.variables(variables);
            return this;
        }

        public Jobs build() {
            this.build(Fields.JOBS::getName, input);

            if (dataManagement != null) {
                sb.append(dataManagement.getQueryString());
            }
            if (numberOfFailedTasks) {
                sb.append(Fields.NUMBER_OF_FAILED_TASKS.getName()).append(Constants.RETURN);
            }
            if (numberOfFaultyTasks) {
                sb.append(Fields.NUMBER_OF_FAULTY_TASKS.getName()).append(Constants.RETURN);
            }
            if (numberOfFinishedTasks) {
                sb.append(Fields.NUMBER_OF_FINISHED_TASKS.getName()).append(Constants.RETURN);
            }
            if (numberOfInErrorTasks) {
                sb.append(Fields.NUMBER_OF_IN_ERROR_TASKS.getName()).append(Constants.RETURN);
            }
            if (numberOfPendingTasks) {
                sb.append(Fields.NUMBER_OF_PENDING_TASKS.getName()).append(Constants.RETURN);
            }
            if (numberOfRunningTasks) {
                sb.append(Fields.NUMBER_OF_RUNNING_TASKS.getName()).append(Constants.RETURN);
            }
            if (owner) {
                sb.append(Fields.OWNER.getName()).append(Constants.RETURN);
            }
            if (priority) {
                sb.append(Fields.PRIORITY.getName()).append(Constants.RETURN);
            }
            if (projectName) {
                sb.append(Fields.PROJECT_NAME.getName()).append(Constants.RETURN);
            }
            if (removedTime) {
                sb.append(Fields.REMOVED_TIME.getName()).append(Constants.RETURN);
            }
            if (submittedTime) {
                sb.append(Fields.SUBMITTED_TIME.getName()).append(Constants.RETURN);
            }
            if (tasks != null) {
                sb.append(tasks.getQueryString());
            }
            if (totalNumberOfTasks) {
                sb.append(Fields.TOTAL_NUMBER_OF_TASKS.getName()).append(Constants.RETURN);
            }

            sb.append("}").append(Constants.RETURN).append("}").append(Constants.RETURN).append("}");
            return new Jobs(sb.toString());
        }
    }

}
