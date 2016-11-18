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

import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.EXECUTION_DURATION;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.EXECUTION_HOST_NAME;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.JOB_ID;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.NUMBER_OF_EXECUTION_LEFT;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.NUMBER_OF_EXECUTION_ON_FAILURE_LEFT;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.PROGRESS;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.RESTART_MODE;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.SCHEDULED_TIME;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.TAG;
import static org.ow2.proactive.scheduling.api.client.v2.bean.ApiTypeKeyEnum.TASKS;
import static org.ow2.proactive.scheduling.api.client.v2.bean.Constants.RETURN;

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

        public Tasks.Builder excludeExecutionDuration() {
            this.description = false;
            return this;
        }

        public Tasks.Builder excludeExecutionHostName() {
            this.description = false;
            return this;
        }

        public Tasks.Builder excludeJobId() {
            this.jobId = false;
            return this;
        }

        public Tasks.Builder excludeName() {
            this.name = false;
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

        public Tasks.Builder excludeProgress() {
            this.progress = false;
            return this;
        }

        public Tasks.Builder excludeScheduledTime() {
            this.scheduledTime = false;
            return this;
        }

        public Tasks.Builder excludeRestartMode() {
            this.restartMode = false;
            return this;
        }

        public Tasks.Builder excludeTag() {
            this.tag = false;
            return this;
        }

        public Tasks build() {
            this.build(()-> TASKS.getKey(), input);

            if (executionDuration) {
                sb.append(EXECUTION_DURATION.getKey()).append(RETURN);
            }
            if (executionHostName) {
                sb.append(EXECUTION_HOST_NAME.getKey()).append(RETURN);
            }
            if (jobId) {
                sb.append(JOB_ID.getKey()).append(RETURN);
            }
            if (numberOfExecutionLeft) {
                sb.append(NUMBER_OF_EXECUTION_LEFT.getKey()).append(RETURN);
            }
            if (numberOfExecutionOnFailureLeft) {
                sb.append(NUMBER_OF_EXECUTION_ON_FAILURE_LEFT.getKey()).append(
                        RETURN);
            }
            if (progress) {
                sb.append(PROGRESS.getKey()).append(RETURN);
            }
            if (scheduledTime) {
                sb.append(SCHEDULED_TIME.getKey()).append(RETURN);
            }
            if(restartMode) {
                sb.append(RESTART_MODE.getKey()).append(RETURN);
            }
            if (tag) {
                sb.append(TAG.getKey()).append(RETURN);
            }

            sb.append("}").append(RETURN).append("}").append(RETURN).append("}").append(
                    RETURN);
            return new Tasks(sb.toString());
        }
    }
}
