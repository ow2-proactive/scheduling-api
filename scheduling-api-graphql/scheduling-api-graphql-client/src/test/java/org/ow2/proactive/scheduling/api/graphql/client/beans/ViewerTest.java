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
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.SESSION_ID;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.START_CURSOR;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.START_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.STATUS;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.SUBMITTED_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.TOTAL_NUMBER_OF_TASKS;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.USER_SPACE_URL;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VALUE;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VIEWER;

import lombok.extern.log4j.Log4j2;

import org.junit.Test;
import org.ow2.proactive.scheduling.api.graphql.common.Fields;


@Log4j2
public class ViewerTest {
    private static final String ALL = String.format("%s {\n%s\n%s\n%s( %s:\"after\" %s:\"before\" %s:10 %s:10 ){\n%s{\n%s\n%s\n" +
                                                    "%s\n%s\n}\n%s{\n%s\n%s{\n%s\n%s\n%s{\n%s\n%s\n}\n%s\n%s\n%s\n" +
                                                    "%s\n%s\n%s\n%s\n%s{\n%s\n%s\n%s\n%s\n}\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n" +
                                                    "%s\n%s\n%s\n%s\n%s\n}\n}\n}\n}",
                                                    VIEWER.getName(),
                                                    LOGIN.getName(),
                                                    SESSION_ID.getName(),
                                                    JOBS.getName(),
                                                    AFTER.getName(),
                                                    BEFORE.getName(),
                                                    FIRST.getName(),
                                                    LAST.getName(),
                                                    PAGE_INFO.getName(),
                                                    HAS_NEXT_PAGE.getName(),
                                                    HAS_PREVIOUS_PAGE.getName(),
                                                    START_CURSOR.getName(),
                                                    END_CURSOR.getName(),
                                                    EDGES.getName(),
                                                    CURSOR.getName(),
                                                    NODE.getName(),
                                                    DESCRIPTION.getName(),
                                                    FINISHED_TIME.getName(),
                                                    GENERIC_INFORMATION.getName(),
                                                    KEY.getName(),
                                                    VALUE.getName(),
                                                    Fields.ID.getName(),
                                                    IN_ERROR_TIME.getName(),
                                                    MAX_NUMBER_OF_EXECUTION.getName(),
                                                    NAME.getName(),
                                                    ON_TASK_ERROR.getName(),
                                                    START_TIME.getName(),
                                                    STATUS.getName(),
                                                    DATA_MANAGEMENT.getName(),
                                                    GLOBAL_SPACE_URL.getName(),
                                                    INPUT_SPACE_URL.getName(),
                                                    OUTPUT_SPACE_URL.getName(),
                                                    USER_SPACE_URL.getName(),
                                                    NUMBER_OF_FAILED_TASKS.getName(),
                                                    NUMBER_OF_FAULTY_TASKS.getName(),
                                                    NUMBER_OF_FINISHED_TASKS.getName(),
                                                    NUMBER_OF_IN_ERROR_TASKS.getName(),
                                                    NUMBER_OF_PENDING_TASKS.getName(),
                                                    NUMBER_OF_RUNNING_TASKS.getName(),
                                                    OWNER.getName(),
                                                    PRIORITY.getName(),
                                                    PROJECT_NAME.getName(),
                                                    REMOVED_TIME.getName(),
                                                    SUBMITTED_TIME.getName(),
                                                    TOTAL_NUMBER_OF_TASKS.getName());

    @Test
    public void getViewerQueryString() {

        Jobs jobs = new Jobs.Builder().after("after").before("before").first(10).last(10).excludeVariables().build();

        Viewer viewer = new Viewer.Builder().jobs(jobs).build();

        System.out.println(viewer.getQueryString());

        System.out.println(ALL);

        assertThat(viewer.getQueryString()).isEqualTo(ALL);
    }

}
