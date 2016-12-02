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
package org.ow2.proactive.scheduling.api.graphql.client.beans;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.*;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.*;

public class JobTest {

    private static final String ALL = String.format(
            "%s( %s:\"after\" %s:\"before\" %s:10 %s:10 ){\n%s{\n%s\n%s\n" +
                    "%s\n%s\n}\n%s{\n%s\n%s{\n%s\n%s\n%s{\n%s\n%s\n}\n%s\n%s\n%s\n" +
                    "%s\n%s\n%s\n%s\n%s{\n%s\n%s\n%s\n%s\n}\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n" +
                    "%s\n%s\n%s\n%s\n%s\n}\n}\n}", JOBS.getName(), AFTER.getName(), BEFORE.getName(),
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
