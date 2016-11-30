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
package org.ow2.proactive.scheduling.api.graphql.client.beans;

import org.ow2.proactive.scheduling.api.graphql.common.Arguments;
import org.ow2.proactive.scheduling.api.graphql.common.Fields;
import com.google.common.truth.Truth;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.AFTER;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.BEFORE;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.FIRST;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.LAST;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.*;

public class JobTest {

    private static final String ALL = String.format(
            "{\n%s( %s:\"after\" %s:\"before\" %s:10 %s:10 ){\n%s{\n%s\n%s\n" +
                    "%s\n%s\n}\n%s{\n%s\n%s{\n%s\n%s\n%s{\n%s\n%s\n}\n%s\n%s\n%s\n" +
                    "%s\n%s\n%s\n%s\n%s{\n%s\n%s\n%s\n%s\n}\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n" +
                    "%s\n%s\n%s\n%s\n%s\n}\n}\n}\n}", JOBS.getName(), AFTER.getName(), BEFORE.getName(),
            FIRST.getName(), LAST.getName(), PAGE_INFO.getName(), HAS_NEXT_PAGE.getName(),
            HAS_PREVIOUS_PAGE.getName(), START_CURSOR.getName(), END_CURSOR.getName(), EDGES.getName(),
            CURSOR.getName(), NODE.getName(), DESCRIPTION.getName(), FINISHED_TIME.getName(),
            GENERIC_INFORMATION.getName(), KEY.getName(), VALUE.getName(), ID.getName(),
            IN_ERROR_TIME.getName(), MAX_NUMBER_OF_EXECUTION.getName(), NAME.getName(),
            ON_TASK_ERROR.getName(), START_TIME.getName(), STATUS.getName(), DATA_MANAGEMENT.getName(),
            GLOBAL_SPACE_URL.getName(), INPUT_SPACE_URL.getName(), OUTPUT_SPACE_URL.getName(),
            USER_SPACE_URL.getName(), NUMBER_OF_FAILED_TASKS.getName(), NUMBER_OF_FAULTY_TASKS.getName(),
            NUMBER_OF_FINISHED_TASKS.getName(), NUMBER_OF_IN_ERROR_TASKS.getName(),
            NUMBER_OF_PENDING_TASKS.getName(), NUMBER_OF_RUNNING_TASKS.getName(), OWNER.getName(),
            PRIORITY.getName(), PROJECT_NAME.getName(), REMOVED_TIME.getName(), SUBMITTED_TIME.getName(),
            TOTAL_NUMBER_OF_TASKS.getName());

    @Test
    public void getJobsQueryString() {

        Jobs jobs =
                new Jobs.Builder().after("after").before("before")
                        .first(10).last(10).excludeVariables().build();

        System.out.println(jobs.getQueryString());

        assertThat(jobs.getQueryString()).isEqualTo(ALL);
        // How graphiql vaildate the input query on the fly?
    }
}
