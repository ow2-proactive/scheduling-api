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
package org.ow2.proactive.scheduling.api.client.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import static org.ow2.proactive.scheduling.api.client.bean.Constants.RETURN;

@Data
public class Tasks implements ApiType {
    private final String queryString;

    private Tasks(String query) {
        this.queryString = query;
    }

    public static class Builder {
        private boolean cursor = true;
        private boolean description = true;
        private boolean executionDuration = true;
        private boolean executionHostName = true;
        private boolean finishedTime = true;
        private GenericInformation genericInformation = null;
        private boolean id = true;
        private boolean inErrorTime = true;
        /**
         * query filter input list
         */
        private List<JobInput> input = new ArrayList<>();
        private boolean jobId = true;
        private boolean name = true;
        private boolean numberOfExecutionLeft = true;
        private boolean numberOfExecutionOnFailureLeft = true;
        private boolean onTaskError = true;
        private PageInfo pageInfo = new PageInfo.Builder().build();
        private boolean progress = true;
        private boolean scheduledTime = true;
        private boolean startTime = true;
        private boolean status = true;
        private boolean tag = true;
        private Variables variables = null;

        private StringBuilder sb = new StringBuilder();

        public Tasks.Builder excludeCursor() {
            this.cursor = false;
            return this;
        }

        public Tasks.Builder excludeDescription() {
            this.description = false;
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

        public Tasks.Builder excludeFinishedTime() {
            this.finishedTime = false;
            return this;
        }

        public Tasks.Builder excludeGenericInformation() {
            this.genericInformation = null;
            return this;
        }

        public Tasks.Builder excludeId() {
            this.id = false;
            return this;
        }

        public Tasks.Builder excludeInErrorTime() {
            this.inErrorTime = false;
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

        public Tasks.Builder excludeOnTaskError() {
            this.onTaskError = false;
            return this;
        }

        public Tasks.Builder excludePageInfo() {
            this.pageInfo = null;
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

        public Tasks.Builder excludeStartTime() {
            this.startTime = false;
            return this;
        }

        public Tasks.Builder excludeStatus() {
            this.status = false;
            return this;
        }

        public Tasks.Builder excludeTag() {
            this.tag = false;
            return this;
        }

        public Tasks.Builder excludeVariables() {
            this.variables = null;
            return this;
        }

        public Tasks.Builder genericInformation(GenericInformation genericInformation) {
            this.genericInformation = genericInformation;
            return this;
        }

        public Tasks.Builder input(List<JobInput> input) {
            this.input = input;
            return this;
        }

        public Tasks.Builder pageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
            return this;
        }

        public Tasks.Builder variables(Variables variables) {
            this.variables = variables;
            return this;
        }


        public Tasks build() {
            sb.append("tasks");
            sb.append(Inputs.buildQueryString(input));
            sb.append("{").append(RETURN);
            if (pageInfo != null) {
                sb.append(pageInfo.getQueryString());
            }
            sb.append("edges{").append(RETURN);

            if (cursor) {
                sb.append("cursor").append(RETURN);
            }

            sb.append("node{").append(RETURN);

            if (description) {
                sb.append("description").append(RETURN);
            }
            if (executionDuration) {
                sb.append("executionDuration").append(RETURN);
            }
            if (executionHostName) {
                sb.append("executionHostName").append(RETURN);
            }
            if (finishedTime) {
                sb.append("finishedTime").append(RETURN);
            }
            if (genericInformation != null) {
                sb.append(genericInformation.getQueryString()).append(RETURN);
            }
            if (id) {
                sb.append("id").append(RETURN);
            }
            if (inErrorTime) {
                sb.append("inErrorTime").append(RETURN);
            }
            if (jobId) {
                sb.append("jobId").append(RETURN);
            }
            if (name) {
                sb.append("name").append(RETURN);
            }
            if (numberOfExecutionLeft) {
                sb.append("numberOfExecutionLeft").append(RETURN);
            }
            if (numberOfExecutionOnFailureLeft) {
                sb.append("numberOfExecutionOnFailureLeft").append(RETURN);
            }
            if (onTaskError) {
                sb.append("onTaskError").append(RETURN);
            }
            if (progress) {
                sb.append("progress").append(RETURN);
            }
            if (scheduledTime) {
                sb.append("scheduledTime").append(RETURN);
            }
            if (startTime) {
                sb.append("startTime").append(RETURN);
            }
            if (status) {
                sb.append("status").append(RETURN);
            }
            if (tag) {
                sb.append("tag").append(RETURN);
            }
            if (variables != null) {
                sb.append(variables.getQueryString());
            }

            sb.append("}").append(RETURN).append("}").append(RETURN).append("}").append(RETURN);
            return new Tasks(sb.toString());
        }
    }
}
