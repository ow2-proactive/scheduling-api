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

public enum ApiTypeKeyEnum {
    CURSOR("cursor"),
    DATA_MANAGEMENT("dataManagement"),
    DESCRIPTION("description"),
    EDGES("edges"),
    END_CURSOR("endCursor"),
    EXECUTION_DURATION("executionDuration"),
    EXECUTION_HOST_NAME("executionHostName"),
    FINISHED_TIME("finishedTime"),
    GENERIC_INFORMATION("genericInformation"),
    GLOBLE_SPACE_URL("globalSpaceUrl"),
    HAS_NEXT_PAGE("hasNextPage"),
    HAS_PREVIOUS_PAGE("hasPreviousPage"),
    ID("id"),
    INPUT_SPACE_URL("inputSpaceUrl"),
    IN_ERROR_TIME("inErrorTime"),
    MAX_NUMBER_OF_EXECUTION("maxNumberOfExecution"),
    JOB_ID("jobId"),
    JOB_NAME("jobName"),
    JOBS("jobs"),
    KEY("key"),
    LOGIN("login"),
    NAME("name"),
    NUMBER_OF_EXECUTION_LEFT("numberOfExecutionLeft"),
    NUMBER_OF_EXECUTION_ON_FAILURE_LEFT("numberOfExecutionOnFailureLeft"),
    NUMBER_OF_FAILED_TASKS("numberOfFailedTasks"),
    NUMBER_OF_FAULTY_TASKS("numberOfFaultyTasks"),
    NUMBER_OF_FINISHED_TASKS("numberOfFinishedTasks"),
    NUMBER_OF_IN_ERROR_TASKS("numberOfInErrorTasks"),
    NUMBER_OF_PENDING_TASKS("numberOfPendingTasks"),
    NUMBER_OF_RUNNING_TASKS("numberOfRunningTasks"),
    NODE("node"),
    ON_TASK_ERROR("onTaskError"),
    OUTPUT_SPACE_URL("outputSpaceUrl"),
    OWNER("owner"),
    PAGE_INFO("pageInfo"),
    PRIORITY("priority"),
    PROGRESS("progress"),
    PROJECT_NAME("projectName"),
    QUERY_RESPONSE("queryResponse"),
    REMOVED_TIME("removedTime"),
    RESTART_MODE("restartMode"),
    SCHEDULED_TIME("scheduledTime"),
    SESSION_ID("sessionId"),
    START_CURSOR("startCursor"),
    START_TIME("startTime"),
    STATUS("status"),
    SUBMITTED_TIME("submittedTime"),
    TAG("tag"),
    TASKS("tasks"),
    TOTAL_NUMBER_OF_TASKS("totalNumberOfTasks"),
    USER_SPACE_URL("userSpaceUrl"),
    VALUE("value"),
    VARIABLES("variables"),
    VIEWER("viewer");

    private String key;

    ApiTypeKeyEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

}
