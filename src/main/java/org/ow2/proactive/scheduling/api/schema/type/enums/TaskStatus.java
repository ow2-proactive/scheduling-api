/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2016 INRIA/University of
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
package org.ow2.proactive.scheduling.api.schema.type.enums;

import graphql.annotations.GraphQLDescription;
import graphql.annotations.GraphQLType;

@GraphQLType
public enum TaskStatus {

    @GraphQLDescription("The task has just been submitted by the user.")
    SUBMITTED,
    @GraphQLDescription("The task is in the scheduler pending queue.")
    PENDING,
    @GraphQLDescription("The task is paused.")
    PAUSED,
    @GraphQLDescription("The task is executing.")
    RUNNING,
    @GraphQLDescription("The task is waiting for restart after an error (i.e. native code != 0 or exception).")
    WAITING_ON_ERROR,
    @GraphQLDescription("The task is waiting for restart after a failure (i.e. node down).")
    WAITING_ON_FAILURE,
    @GraphQLDescription("The task is failed (only if max execution time has been reached and the node on which it was started is down).")
    FAILED,
    @GraphQLDescription("The task could not be started. It means that the task could not be started due to one ore more dependency failure.")
    NOT_STARTED,
    @GraphQLDescription("The task could not be restarted. It means that the task could not be restarted after an error during the previous execution.")
    NOT_RESTARTED,
    @GraphQLDescription("The task has been aborted by an exception on an other task while the task is running (job has cancelOnError=true). Can be also in this status if the job is killed while the concerned task was running.")
    ABORTED,
    @GraphQLDescription("The task has finished execution with error code (!=0) or exception.")
    FAULTY,
    @GraphQLDescription("The task has finished execution.")
    FINISHED,
    @GraphQLDescription("The task was not executed: it was the non-selected branch of an IF/ELSE control flow action.")
    SKIPPED,
    @GraphQLDescription("The task is suspended after first error and is waiting for a manual restart action.")
    IN_ERROR;

}
