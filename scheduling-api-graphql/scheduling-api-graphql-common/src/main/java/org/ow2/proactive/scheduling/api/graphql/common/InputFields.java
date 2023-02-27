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
package org.ow2.proactive.scheduling.api.graphql.common;

import com.google.common.base.CaseFormat;


/**
 * Defines the name of the different GraphQL input types which are used in the API.
 *
 * @author ActiveEon Team
 */
public enum InputFields {

    //Items are ordered following alphabetic order
    AFTER,
    BEFORE,
    COMPARABLE_ID,
    EXCLUDE_REMOVED,
    ID,
    KEY,
    LAST_UPDATED_TIME,
    NAME,
    OWNER,
    TENANT,
    PRIORITY,
    PROJECT_NAME,
    BUCKET_NAME,
    JOB_STATUS,
    TASK_STATUS,
    SUBMITTED_TIME,
    START_TIME,
    FINISHED_TIME,
    NUMBER_OF_PENDING_TASKS,
    NUMBER_OF_RUNNING_TASKS,
    NUMBER_OF_FINISHED_TASKS,
    NUMBER_OF_FAULTY_TASKS,
    NUMBER_OF_FAILED_TASKS,
    NUMBER_OF_IN_ERROR_TASKS,
    TOTAL_NUMBER_OF_TASKS,
    CUMULATED_CORE_TIME,
    PARENT_ID,
    CHILDREN_COUNT,
    NUMBER_OF_NODES,
    NUMBER_OF_NODES_IN_PARALLEL,
    VALUE,
    SUBMISSION_MODE;

    public String getName() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }

}
