package org.ow2.proactive.scheduling.api.graphql.client.beans;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.ow2.proactive.scheduling.api.graphql.common.Fields;

import static com.google.common.truth.Truth.assertThat;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.*;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.*;

@Log4j2
public class ViewerTest {
    private static final String ALL = String.format(
            "%s {\n%s\n%s\n%s( %s:\"after\" %s:\"before\" %s:10 %s:10 ){\n%s{\n%s\n%s\n" +
                    "%s\n%s\n}\n%s{\n%s\n%s{\n%s\n%s\n%s{\n%s\n%s\n}\n%s\n%s\n%s\n" +
                    "%s\n%s\n%s\n%s\n%s{\n%s\n%s\n%s\n%s\n}\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n" +
                    "%s\n%s\n%s\n%s\n%s\n}\n}\n}\n}", VIEWER.getName(), LOGIN.getName(), SESSION_ID.getName(),
            JOBS.getName(), AFTER.getName(), BEFORE.getName(), FIRST.getName(), LAST.getName(), PAGE_INFO.getName(),
            HAS_NEXT_PAGE.getName(), HAS_PREVIOUS_PAGE.getName(), START_CURSOR.getName(), END_CURSOR.getName(), EDGES.getName(),
            CURSOR.getName(), NODE.getName(), DESCRIPTION.getName(), FINISHED_TIME.getName(),
            GENERIC_INFORMATION.getName(), KEY.getName(), VALUE.getName(), Fields.ID.getName(),
            IN_ERROR_TIME.getName(), MAX_NUMBER_OF_EXECUTION.getName(), NAME.getName(),
            ON_TASK_ERROR.getName(), START_TIME.getName(), STATUS.getName(), DATA_MANAGEMENT.getName(),
            GLOBAL_SPACE_URL.getName(), INPUT_SPACE_URL.getName(), OUTPUT_SPACE_URL.getName(),
            USER_SPACE_URL.getName(), NUMBER_OF_FAILED_TASKS.getName(), NUMBER_OF_FAULTY_TASKS.getName(),
            NUMBER_OF_FINISHED_TASKS.getName(), NUMBER_OF_IN_ERROR_TASKS.getName(),
            NUMBER_OF_PENDING_TASKS.getName(), NUMBER_OF_RUNNING_TASKS.getName(), OWNER.getName(),
            PRIORITY.getName(), PROJECT_NAME.getName(), REMOVED_TIME.getName(), SUBMITTED_TIME.getName(),
            TOTAL_NUMBER_OF_TASKS.getName());

    @Test
    public void getViewerQueryString() {

        Jobs jobs = new Jobs.Builder().after("after").before("before")
                .first(10).last(10).excludeVariables().build();

        Viewer viewer = new Viewer.Builder().jobs(jobs).build();

        System.out.println(viewer.getQueryString());

        System.out.println(ALL);

        assertThat(viewer.getQueryString()).isEqualTo(ALL);
    }

}