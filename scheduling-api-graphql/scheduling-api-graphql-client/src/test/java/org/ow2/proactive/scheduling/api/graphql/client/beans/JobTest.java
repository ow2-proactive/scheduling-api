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
package org.ow2.proactive.scheduling.api.graphql.client.beans;

import static com.google.common.truth.Truth.assertThat;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.AFTER;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.BEFORE;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.FILTER;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.FIRST;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.LAST;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.*;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.EXCLUDE_REMOVED;

import java.util.StringJoiner;

import org.junit.Test;
import org.ow2.proactive.scheduling.api.graphql.beans.input.Constants;
import org.ow2.proactive.scheduling.api.graphql.beans.input.JobInput;
import org.ow2.proactive.scheduling.api.graphql.beans.input.Jobs;

import com.google.common.collect.ImmutableList;


public class JobTest {

    private static final StringJoiner ALL = new StringJoiner(Constants.RETURN).add(String.format("%s( %s:\"after\" %s:\"before\" %s:10 %s:10 %s : [{ %s : false }] ){",
                                                                                                 JOBS.getName(),
                                                                                                 AFTER.getName(),
                                                                                                 BEFORE.getName(),
                                                                                                 FIRST.getName(),
                                                                                                 LAST.getName(),
                                                                                                 FILTER.getName(),
                                                                                                 EXCLUDE_REMOVED.getName()))
                                                                              .add(PAGE_INFO.getName() + "{")
                                                                              .add(HAS_NEXT_PAGE.getName())
                                                                              .add(HAS_PREVIOUS_PAGE.getName())
                                                                              .add(START_CURSOR.getName())
                                                                              .add(END_CURSOR.getName())
                                                                              .add("}")
                                                                              .add(TOTAL_COUNT.getName())
                                                                              .add(EDGES.getName() + "{")
                                                                              .add(CURSOR.getName())
                                                                              .add(NODE.getName() + "{")
                                                                              .add(DESCRIPTION.getName())
                                                                              .add(FINISHED_TIME.getName())
                                                                              .add(GENERIC_INFORMATION.getName() + "{")
                                                                              .add(KEY.getName())
                                                                              .add(VALUE.getName())
                                                                              .add("}")
                                                                              .add(ID.getName())
                                                                              .add(IN_ERROR_TIME.getName())
                                                                              .add(MAX_NUMBER_OF_EXECUTION.getName())
                                                                              .add(NAME.getName())
                                                                              .add(ON_TASK_ERROR.getName())
                                                                              .add(TASK_RETRY_DELAY.getName())
                                                                              .add(START_TIME.getName())
                                                                              .add(STATUS.getName())
                                                                              .add(DATA_MANAGEMENT.getName() + "{")
                                                                              .add(GLOBAL_SPACE_URL.getName())
                                                                              .add(INPUT_SPACE_URL.getName())
                                                                              .add(OUTPUT_SPACE_URL.getName())
                                                                              .add(USER_SPACE_URL.getName())
                                                                              .add("}")
                                                                              .add(LAST_UPDATED_TIME.getName())
                                                                              .add(NUMBER_OF_FAILED_TASKS.getName())
                                                                              .add(NUMBER_OF_FAULTY_TASKS.getName())
                                                                              .add(NUMBER_OF_FINISHED_TASKS.getName())
                                                                              .add(NUMBER_OF_IN_ERROR_TASKS.getName())
                                                                              .add(NUMBER_OF_PENDING_TASKS.getName())
                                                                              .add(NUMBER_OF_RUNNING_TASKS.getName())
                                                                              .add(OWNER.getName())
                                                                              .add(TENANT.getName())
                                                                              .add(CUMULATED_CORE_TIME.getName())
                                                                              .add(PARENT_ID.getName())
                                                                              .add(CHILDREN_COUNT.getName())
                                                                              .add(NUMBER_OF_NODES.getName())
                                                                              .add(NUMBER_OF_NODES_IN_PARALLEL.getName())
                                                                              .add(PRIORITY.getName())
                                                                              .add(PROJECT_NAME.getName())
                                                                              .add(BUCKET_NAME.getName())
                                                                              .add(REMOVED_TIME.getName())
                                                                              .add(RESULT_MAP.getName() + "{")
                                                                              .add(KEY.getName())
                                                                              .add(VALUE.getName())
                                                                              .add("}")
                                                                              .add(SUBMITTED_TIME.getName())
                                                                              .add(TOTAL_NUMBER_OF_TASKS.getName())
                                                                              .add(SUBMISSION_MODE.getName())
                                                                              .add("}")
                                                                              .add("}")
                                                                              .add("}");

    @Test
    public void getJobsQueryString() {

        JobInput input = new JobInput.Builder().isExcludeRemoved(false).build();

        Jobs jobs = new Jobs.Builder().after("after")
                                      .before("before")
                                      .first(10)
                                      .last(10)
                                      .input(ImmutableList.of(input))
                                      .excludeVariables()
                                      .build();

        System.out.println("job query string: \n" + jobs.getQueryString());

        System.out.println("expected: \n" + ALL);

        assertThat(jobs.getQueryString()).isEqualTo(ALL.toString());
    }
}
