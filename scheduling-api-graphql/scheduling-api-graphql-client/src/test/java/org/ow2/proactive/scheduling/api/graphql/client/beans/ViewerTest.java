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
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.FIRST;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.LAST;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.CURSOR;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.DATA_MANAGEMENT;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.DESCRIPTION;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.EDGES;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.END_CURSOR;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.FINISHED_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.GENERIC_INFORMATION;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.GLOBAL_SPACE_URL;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.HAS_NEXT_PAGE;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.HAS_PREVIOUS_PAGE;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.INPUT_SPACE_URL;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.IN_ERROR_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.JOBS;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.KEY;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.LAST_UPDATED_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.LOGIN;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.MAX_NUMBER_OF_EXECUTION;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.NAME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.NODE;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.NUMBER_OF_FAILED_TASKS;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.NUMBER_OF_FAULTY_TASKS;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.NUMBER_OF_FINISHED_TASKS;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.NUMBER_OF_IN_ERROR_TASKS;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.NUMBER_OF_PENDING_TASKS;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.NUMBER_OF_RUNNING_TASKS;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.ON_TASK_ERROR;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.OUTPUT_SPACE_URL;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.OWNER;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.PAGE_INFO;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.PRIORITY;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.PROJECT_NAME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.REMOVED_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.RESULT_MAP;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.SESSION_ID;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.START_CURSOR;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.START_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.STATUS;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.SUBMITTED_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.TASK_RETRY_DELAY;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.TOTAL_COUNT;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.TOTAL_NUMBER_OF_TASKS;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.USER_SPACE_URL;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VALUE;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VIEWER;

import org.apache.commons.lang3.text.StrBuilder;
import org.junit.Test;
import org.ow2.proactive.scheduling.api.graphql.beans.input.Jobs;
import org.ow2.proactive.scheduling.api.graphql.beans.input.Viewer;
import org.ow2.proactive.scheduling.api.graphql.common.Fields;

import lombok.extern.log4j.Log4j2;


@Log4j2
public class ViewerTest {
    private static final StrBuilder ALL = new StrBuilder().appendln(VIEWER.getName() + " {")
                                                          .appendln(LOGIN.getName())
                                                          .appendln(SESSION_ID.getName())
                                                          .appendln(String.format("%s( %s:\"after\" %s:\"before\" %s:10 %s:10 ){",
                                                                                  JOBS.getName(),
                                                                                  AFTER.getName(),
                                                                                  BEFORE.getName(),
                                                                                  FIRST.getName(),
                                                                                  LAST.getName()))
                                                          .appendln(PAGE_INFO.getName() + "{")
                                                          .appendln(HAS_NEXT_PAGE.getName())
                                                          .appendln(HAS_PREVIOUS_PAGE.getName())
                                                          .appendln(START_CURSOR.getName())
                                                          .appendln(END_CURSOR.getName())
                                                          .appendln("}")
                                                          .appendln(TOTAL_COUNT.getName())
                                                          .appendln(EDGES.getName() + "{")
                                                          .appendln(CURSOR.getName())
                                                          .appendln(NODE.getName() + "{")
                                                          .appendln(DESCRIPTION.getName())
                                                          .appendln(FINISHED_TIME.getName())
                                                          .appendln(GENERIC_INFORMATION.getName() + "{")
                                                          .appendln(KEY.getName())
                                                          .appendln(VALUE.getName())
                                                          .appendln("}")
                                                          .appendln(Fields.ID.getName())
                                                          .appendln(IN_ERROR_TIME.getName())
                                                          .appendln(MAX_NUMBER_OF_EXECUTION.getName())
                                                          .appendln(NAME.getName())
                                                          .appendln(ON_TASK_ERROR.getName())
                                                          .appendln(TASK_RETRY_DELAY.getName())
                                                          .appendln(START_TIME.getName())
                                                          .appendln(STATUS.getName())
                                                          .appendln(DATA_MANAGEMENT.getName() + "{")
                                                          .appendln(GLOBAL_SPACE_URL.getName())
                                                          .appendln(INPUT_SPACE_URL.getName())
                                                          .appendln(OUTPUT_SPACE_URL.getName())
                                                          .appendln(USER_SPACE_URL.getName())
                                                          .appendln("}")
                                                          .appendln(LAST_UPDATED_TIME.getName())
                                                          .appendln(NUMBER_OF_FAILED_TASKS.getName())
                                                          .appendln(NUMBER_OF_FAULTY_TASKS.getName())
                                                          .appendln(NUMBER_OF_FINISHED_TASKS.getName())
                                                          .appendln(NUMBER_OF_IN_ERROR_TASKS.getName())
                                                          .appendln(NUMBER_OF_PENDING_TASKS.getName())
                                                          .appendln(NUMBER_OF_RUNNING_TASKS.getName())
                                                          .appendln(OWNER.getName())
                                                          .appendln(PRIORITY.getName())
                                                          .appendln(PROJECT_NAME.getName())
                                                          .appendln(REMOVED_TIME.getName())
                                                          .appendln(RESULT_MAP.getName() + "{")
                                                          .appendln(KEY.getName())
                                                          .appendln(VALUE.getName())
                                                          .appendln("}")
                                                          .appendln(SUBMITTED_TIME.getName())
                                                          .appendln(TOTAL_NUMBER_OF_TASKS.getName())
                                                          .appendln("}")
                                                          .appendln("}")
                                                          .appendln("}")
                                                          .append("}");

    @Test
    public void getViewerQueryString() {

        Jobs jobs = new Jobs.Builder().after("after").before("before").first(10).last(10).excludeVariables().build();

        Viewer viewer = new Viewer.Builder().jobs(jobs).build();

        System.out.println("viewer query string: \n" + viewer.getQueryString());

        System.out.println("expected: \n" + ALL);

        assertThat(viewer.getQueryString()).isEqualTo(ALL.toString());
    }

}
