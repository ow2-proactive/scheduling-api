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
package org.ow2.proactive.scheduling.api.graphql.schema.type;

import org.ow2.proactive.scheduling.api.graphql.common.Types;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLEnumType;

import static graphql.schema.GraphQLEnumType.newEnum;

/**
 * @author ActiveEon Team
 */
public class OnTaskError {

    public final static TypeSingleton<GraphQLEnumType> TYPE = new TypeSingleton<GraphQLEnumType>() {
        @Override
        public GraphQLEnumType buildType(DataFetcher... dataFetchers) {
            return newEnum().name(Types.ON_TASK_ERROR.getName())
                    .description("Defines the behaviour that is applied on Tasks when an error occurs.")
                    .value("CANCEL_JOB", "CANCEL_JOB", "Cancel job after all execution attempts.")
                    .value("CONTINUE_JOB_EXECUTION", "CONTINUE_JOB_EXECUTION",
                            "Continue job execution (try all execution attempts).")
                    .value("NONE", "NONE", "None.")
                    .value("PAUSE_JOB", "PAUSE_JOB", "Suspend task after first and pause job.")
                    .value("PAUSE_TASK", "PAUSE_TASK", "Suspend task after first error and continue others.")
                    .build();
        }
    };

    private OnTaskError() {
    }

}
