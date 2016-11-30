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
public final class TaskStatus {

    public static final TypeSingleton<GraphQLEnumType> TYPE = new TypeSingleton<GraphQLEnumType>() {
        @Override
        public GraphQLEnumType buildType(DataFetcher... dataFetchers) {
            return newEnum().name(Types.TASK_STATUS.getName())
                    .description("Task status list")
                    .value("ABORTED",
                            "ABORTED",
                            "The task has been aborted by an exceptions on an other task while the task is running (job has cancelOnError=true). Can be also in this status if the job is killed while the concerned task was running.")
                    .value("FAILED",
                            "FAILED",
                            "The task is failed (only if max execution time has been reached and the node on which it was started is down).")
                    .value("FAULTY",
                            "FAULTY",
                            "The task has finished execution with error code (!=0) or exceptions.")
                    .value("FINISHED",
                            "FINISHED",
                            "The task has finished execution.")
                    .value("IN_ERROR",
                            "IN_ERROR",
                            "The task is suspended after first error and is waiting for a manual restart action.")
                    .value("NOT_RESTARTED",
                            "NOT_RESTARTED",
                            "The task could not be restarted. It means that the task could not be restarted after an error during the previous execution.")
                    .value("NOT_STARTED",
                            "NOT_STARTED",
                            "The task could not be started. It means that the task could not be started due to one ore more dependency failure.")
                    .value("PAUSED", "PAUSED", "The task is paused")
                    .value("PENDING",
                            "PENDING",
                            "The task is in the scheduler pending queue.")
                    .value("RUNNING",
                            "RUNNING",
                            "The task is executing.")
                    .value("SKIPPED",
                            "SKIPPED",
                            "The task was not executed: it was the non-selected branch of an IF/ELSE control flow action.")
                    .value("SUBMITTED",
                            "SUBMITTED",
                            "The task has just been submitted by the user.")
                    .value("WAITING_ON_ERROR",
                            "WAITING_ON_ERROR",
                            "The task is waiting for restart after an error (i.e. native code != 0 or exceptions).")
                    .value("WAITING_ON_FAILURE",
                            "WAITING_ON_FAILURE",
                            "The task is waiting for restart after a failure (i.e. node down).")
                    .build();
        }
    };

    private TaskStatus() {
    }

}
