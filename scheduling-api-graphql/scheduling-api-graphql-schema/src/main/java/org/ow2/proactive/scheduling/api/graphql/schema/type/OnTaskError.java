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
package org.ow2.proactive.scheduling.api.graphql.schema.type;

import static graphql.schema.GraphQLEnumType.newEnum;

import org.ow2.proactive.scheduling.api.graphql.common.Types;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLEnumType;


/**
 * @author ActiveEon Team
 */
public class OnTaskError {

    public final static TypeSingleton<GraphQLEnumType> TYPE = new TypeSingleton<GraphQLEnumType>() {
        @Override
        public GraphQLEnumType buildType(DataFetcher... dataFetchers) {
            return newEnum().name(Types.ON_TASK_ERROR.getName())
                            .description("Defines the behaviour that is applied on Tasks when an error occurs.")
                            // values below correspond to the UPPER_CAMEL UPPER_UNDERSCORE conversion of database values
                            // see JobDataFetcher and TaskDataFetcher
                            .value("CANCEL_JOB", "CANCEL_JOB", "Cancel job after all execution attempts.")
                            .value("CONTINUE_JOB_EXECUTION",
                                   "CONTINUE_JOB_EXECUTION",
                                   "Continue job execution (try all execution attempts).")
                            .value("NONE", "NONE", "None.")
                            .value("PAUSE_JOB", "PAUSE_JOB", "Suspend task after first and pause job.")
                            .value("PAUSE_TASK", "SUSPEND_TASK", "Suspend task after first error and continue others.")
                            .build();
        }
    };

    private OnTaskError() {
    }

}
