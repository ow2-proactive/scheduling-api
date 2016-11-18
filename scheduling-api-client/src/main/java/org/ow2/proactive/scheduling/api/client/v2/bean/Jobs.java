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

import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.JOB_NAME;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.NUMBER_OF_FAILED_TASKS;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.NUMBER_OF_FAULTY_TASKS;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.NUMBER_OF_FINISHED_TASKS;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.NUMBER_OF_IN_ERROR_TASKS;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.NUMBER_OF_PENDING_TASKS;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.NUMBER_OF_RUNNING_TASKS;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.OWNER;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.PRIORITY;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.PROJECT_NAME;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.REMOVED_TIME;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.SUBMITTED_TIME;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.TOTAL_NUMBER_OF_TASKS;
import static org.ow2.proactive.scheduling.api.client.v2.bean.Constants.RETURN;

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
        private boolean jobName = true;
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

        public Builder dataManagement(DataManagement dataManagement) {
            this.dataManagement = dataManagement;
            return this;
        }

        public Builder excludeDataManagement() {
            this.dataManagement = null;
            return this;
        }

        public Builder excludeJobName() {
            this.jobName = false;
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
            sb.append("{").append(RETURN);
            this.build(() -> ApiTypeKeyEnum.JOBS.getKey(), input);

            if (dataManagement != null) {
                sb.append(dataManagement.getQueryString()).append(RETURN);
            }
            if (jobName) {
                sb.append(JOB_NAME.getKey()).append(RETURN);
            }
            if (numberOfFailedTasks) {
                sb.append(NUMBER_OF_FAILED_TASKS.getKey()).append(RETURN);
            }
            if (numberOfFaultyTasks) {
                sb.append(NUMBER_OF_FAULTY_TASKS.getKey()).append(RETURN);
            }
            if (numberOfFinishedTasks) {
                sb.append(NUMBER_OF_FINISHED_TASKS.getKey()).append(RETURN);
            }
            if (numberOfInErrorTasks) {
                sb.append(NUMBER_OF_IN_ERROR_TASKS.getKey()).append(RETURN);
            }
            if (numberOfPendingTasks) {
                sb.append(NUMBER_OF_PENDING_TASKS.getKey()).append(RETURN);
            }
            if (numberOfRunningTasks) {
                sb.append(NUMBER_OF_RUNNING_TASKS.getKey()).append(RETURN);
            }
            if (owner) {
                sb.append(OWNER.getKey()).append(RETURN);
            }
            if (priority) {
                sb.append(PRIORITY.getKey()).append(RETURN);
            }
            if (projectName) {
                sb.append(PROJECT_NAME.getKey()).append(RETURN);
            }
            if (removedTime) {
                sb.append(REMOVED_TIME.getKey()).append(RETURN);
            }
            if (submittedTime) {
                sb.append(SUBMITTED_TIME.getKey()).append(RETURN);
            }
            if (tasks != null) {
                sb.append(tasks.getQueryString());
            }
            if (totalNumberOfTasks) {
                sb.append(TOTAL_NUMBER_OF_TASKS.getKey()).append(RETURN);
            }

            sb.append("}").append(RETURN).append("}").append(RETURN).append("}").append(
                    RETURN).append("}");
            return new Jobs(sb.toString());
        }
    }
}
