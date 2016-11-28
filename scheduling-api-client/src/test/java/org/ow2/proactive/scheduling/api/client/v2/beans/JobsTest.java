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
package org.ow2.proactive.scheduling.api.client.v2.beans;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JobsTest {
    private static final String ALL = "{\njobs( after:\"after\" before:\"before\" first:10 last:10 ){\n" +
            "pageInfo{\n" +
            "hasNextPage\n" +
            "hasPreviousPage\n" +
            "startCursor\n" +
            "endCursor\n" +
            "}\n" +
            "edges{\n" +
            "cursor\n" +
            "node{\n" +
            "description\n" +
            "finishedTime\n" +
            "genericInformation{\n" +
            "key\n" +
            "value\n" +
            "}\n" +
            "id\n" +
            "inErrorTime\n" +
            "maxNumberOfExecution\n" +
            "name\n" +
            "onTaskError\n" +
            "startTime\n" +
            "status\n" +
            "dataManagement{\n" +
            "globalSpaceUrl\n" +
            "inputSpaceUrl\n" +
            "outputSpaceUrl\n" +
            "userSpaceUrl\n" +
            "}\n" +
            "numberOfFailedTasks\n" +
            "numberOfFaultyTasks\n" +
            "numberOfFinishedTasks\n" +
            "numberOfInErrorTasks\n" +
            "numberOfPendingTasks\n" +
            "numberOfRunningTasks\n" +
            "owner\n" +
            "priority\n" +
            "projectName\n" +
            "removedTime\n" +
            "submittedTime\n" +
            "totalNumberOfTasks\n}\n}\n}\n}";

    @Test
    public void getJobsQueryString() {

        Jobs jobs = new Jobs.Builder().after("after").before("before").first(10).last(10).excludeVariables().build();

        System.out.println(jobs.getQueryString());

        assertThat(jobs.getQueryString(), is(ALL));
        // How graphiql vaildate the input query on the fly?
    }
}
