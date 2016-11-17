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
package org.ow2.proactive.scheduling.api.client.v2.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

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

        private StringBuilder sb = new StringBuilder();

        public Builder dataManagement(DataManagement dataManagement) {
            this.dataManagement = dataManagement;
            return this;
        }

        public Builder excludeDataManagement() {
            this.dataManagement = null;
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

        public Builder excludeOwner() {
            this.owner = false;
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

        public Builder excludeSubmittedTime() {
            this.submittedTime = false;
            return this;
        }

        public Builder excludeTotalNumberOfTasks() {
            this.totalNumberOfTasks = false;
            return this;
        }

        public Builder input(List<JobInput> input) {
            this.input = input;
            return this;
        }

        public Builder tasks(Tasks tasks) {
            this.tasks = tasks;
            return this;
        }

        public Jobs build() {
            sb.append("{").append(Constants.RETURN);
            this.build(() -> ApiTypeKeyEnum.JOBS.getKey(), input);

            if (dataManagement != null) {
                sb.append(dataManagement.getQueryString()).append(Constants.RETURN);
            }
            if (numberOfFailedTasks) {
                sb.append(ApiTypeKeyEnum.NUMBER_OF_FAILED_TASKS.getKey()).append(Constants.RETURN);
            }
            if (numberOfFaultyTasks) {
                sb.append(ApiTypeKeyEnum.NUMBER_OF_FAULTY_TASKS.getKey()).append(Constants.RETURN);
            }
            if (numberOfFinishedTasks) {
                sb.append(ApiTypeKeyEnum.NUMBER_OF_FINISHED_TASKS.getKey()).append(Constants.RETURN);
            }
            if (numberOfInErrorTasks) {
                sb.append(ApiTypeKeyEnum.NUMBER_OF_IN_ERROR_TASKS.getKey()).append(Constants.RETURN);
            }
            if (numberOfPendingTasks) {
                sb.append(ApiTypeKeyEnum.NUMBER_OF_PENDING_TASKS.getKey()).append(Constants.RETURN);
            }
            if (numberOfRunningTasks) {
                sb.append(ApiTypeKeyEnum.NUMBER_OF_RUNNING_TASKS.getKey()).append(Constants.RETURN);
            }
            if (owner) {
                sb.append(ApiTypeKeyEnum.OWNER.getKey()).append(Constants.RETURN);
            }
            if (priority) {
                sb.append(ApiTypeKeyEnum.PRIORITY.getKey()).append(Constants.RETURN);
            }
            if (projectName) {
                sb.append(ApiTypeKeyEnum.PROJECT_NAME.getKey()).append(Constants.RETURN);
            }
            if (removedTime) {
                sb.append(ApiTypeKeyEnum.REMOVED_TIME.getKey()).append(Constants.RETURN);
            }
            if (submittedTime) {
                sb.append(ApiTypeKeyEnum.SUBMITTED_TIME.getKey()).append(Constants.RETURN);
            }
            if (tasks != null) {
                sb.append(tasks.getQueryString());
            }
            if (totalNumberOfTasks) {
                sb.append(ApiTypeKeyEnum.TOTAL_NUMBER_OF_TASKS.getKey()).append(Constants.RETURN);
            }

            sb.append("}").append(Constants.RETURN).append("}").append(Constants.RETURN).append("}").append(
                    Constants.RETURN).append("}");
            return new Jobs(sb.toString());
        }
    }
}
