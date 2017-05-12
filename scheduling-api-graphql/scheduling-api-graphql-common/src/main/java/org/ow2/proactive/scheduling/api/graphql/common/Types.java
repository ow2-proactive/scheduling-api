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
 * Defines the type of the different GraphQL types which are used in the API.
 *
 * @author ActiveEon Team
 */
public enum Types {

    //Items are ordered following alphabetic order
    COMPARABLE_ID_INPUT,
    DATA_MANAGEMENT,
    GENERIC_INFORMATION,
    JOB,
    JOB_INPUT,
    JOB_PRIORITY,
    JOB_STATUS,
    JOB_TASK_COMMON,
    KEY_VALUE,
    KEY_VALUE_INPUT,
    LAST_UPDATED_TIME_INPUT,
    ON_TASK_ERROR,
    QUERY,
    RESTART_MODE,
    SUBMITTED_TIME_INPUT,
    TASK,
    TASK_INPUT,
    TASK_STATUS,
    USER,
    VARIABLE;

    public String getName() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name());
    }

}
