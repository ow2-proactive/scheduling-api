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
package org.ow2.proactive.scheduling.api.graphql.common;

import com.google.common.base.CaseFormat;


/**
 * Defines the name of the different GraphQL fields which are used in the API.
 *
 * @author ActiveEon Team
 */
public enum Fields {

    ADDITIONAL_CLASSPATH,
    CURSOR,
    DATA_MANAGEMENT,
    DESCRIPTION,
    EDGES,
    END_CURSOR,
    EXECUTION_DURATION,
    EXECUTION_HOST_NAME,
    FINISHED_TIME,
    GENERIC_INFORMATION,
    GLOBAL_SPACE_URL,
    HAS_NEXT_PAGE,
    HAS_PREVIOUS_PAGE,
    ID,
    IN_ERROR_TIME,
    INPUT_SPACE_URL,
    JAVA_HOME,
    JOB_ID,
    JOBS,
    JVM_ARGUMENTS,
    KEY,
    LOGIN,
    MAX_NUMBER_OF_EXECUTION,
    NAME,
    NODE,
    NUMBER_OF_EXECUTION_LEFT,
    NUMBER_OF_EXECUTION_ON_FAILURE_LEFT,
    NUMBER_OF_FAILED_TASKS,
    NUMBER_OF_FAULTY_TASKS,
    NUMBER_OF_FINISHED_TASKS,
    NUMBER_OF_IN_ERROR_TASKS,
    NUMBER_OF_PENDING_TASKS,
    NUMBER_OF_RUNNING_TASKS,
    ON_TASK_ERROR,
    OUTPUT_SPACE_URL,
    OWNER,
    PAGE_INFO,
    PRECIOUS_LOGS,
    PRECIOUS_RESULT,
    PRIORITY,
    PROJECT_NAME,
    REMOVED_TIME,
    RESTART_MODE,
    RESULT_PREVIEW,
    RUN_AS_ME,
    SCHEDULED_TIME,
    SESSION_ID,
    START_CURSOR,
    START_TIME,
    STATUS,
    SUBMITTED_TIME,
    TAG,
    TASKS,
    TOTAL_NUMBER_OF_TASKS,
    USER_SPACE_URL,
    VALUE,
    VARIABLES,
    VERSION,
    VIEWER,
    WALLTIME,
    WORKING_DIR;

    public String getName() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

}
