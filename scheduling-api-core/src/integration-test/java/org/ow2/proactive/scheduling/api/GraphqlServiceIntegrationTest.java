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
package org.ow2.proactive.scheduling.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;

import org.ow2.proactive.scheduler.common.job.JobPriority;
import org.ow2.proactive.scheduler.common.job.JobStatus;
import org.ow2.proactive.scheduler.common.task.OnTaskError;
import org.ow2.proactive.scheduler.core.db.JobData;
import org.ow2.proactive.scheduling.api.services.GraphqlService;
import org.ow2.proactive.scheduling.api.util.Constants;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.truth.Truth.assertThat;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class GraphqlServiceIntegrationTest {

    private static final String CONTEXT_LOGIN = "bobot";

    private static final String CONTEXT_SESSION_ID = "sessionId";

    @Autowired
    private GraphqlService graphqlService;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testQueryJobs() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery("{ jobs { edges { node { id name } } } }");
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        assertThat(jobNodes).hasSize(10);
    }

    @Test
    public void testQueryJobsPaginated() {
        addJobData(60);

        Map<String, Object> queryResult = executeGraphqlQuery(
                "{ jobs { edges { node { id name } } pageInfo { hasNextPage hasPreviousPage } } }");
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        assertThat(jobNodes).hasSize(Constants.PAGINATION_DEFAULT_SIZE);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasPreviousPage")).isEqualTo(false);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasNextPage")).isEqualTo(true);
    }

    @Test
    public void testQueryJobsPaginatedFirstArgument() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(
                "{ jobs(first: 3) { edges { node { id name } } pageInfo { hasNextPage hasPreviousPage } } }");
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(3);

        assertThat(getField(jobNodes.get(0), "node", "id")).isEqualTo("1");
        assertThat(getField(jobNodes.get(2), "node", "id")).isEqualTo("3");
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasPreviousPage")).isEqualTo(false);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasNextPage")).isEqualTo(true);
    }

    @Test
    public void testQueryJobsPaginatedLastArgument() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(
                "{ jobs(last: 3) { edges { node { id name } } pageInfo { hasNextPage hasPreviousPage } } }");
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(3);

        assertThat(getField(jobNodes.get(0), "node", "id")).isEqualTo("8");
        assertThat(getField(jobNodes.get(2), "node", "id")).isEqualTo("10");
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasPreviousPage")).isEqualTo(true);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasNextPage")).isEqualTo(false);
    }

    @Test
    public void testQueryJobsPaginatedFirstAfterArgument() {
        addJobData(10);

        Map<String, Object> queryResult =
                executeGraphqlQuery("{ jobs(first: 1) { edges { cursor node { id name } } } }");

        List<Object> edges = (List<Object>) getField(queryResult, "data", "jobs", "edges");

        String cursor = (String) getField(edges.get(0), "cursor");

        queryResult = executeGraphqlQuery(
                "{ jobs(first: 3 after: \"" + cursor + "\") { edges { node { id name } } pageInfo { hasNextPage hasPreviousPage } } }");
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(3);

        assertThat(getField(jobNodes.get(0), "node", "id")).isEqualTo("2");
        assertThat(getField(jobNodes.get(2), "node", "id")).isEqualTo("4");
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasPreviousPage")).isEqualTo(false);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasNextPage")).isEqualTo(true);
    }

    @Test
    public void testQueryJobsPaginatedLastBeforeArgument() {
        addJobData(10);

        Map<String, Object> queryResult =
                executeGraphqlQuery("{ jobs(last: 1) { edges { cursor node { id name } } } }");

        List<Object> edges = (List<Object>) getField(queryResult, "data", "jobs", "edges");

        String cursor = (String) getField(edges.get(0), "cursor");

        queryResult = executeGraphqlQuery(
                "{ jobs(last: 3 before: \"" + cursor + "\") { edges { node { id name } } pageInfo { hasNextPage hasPreviousPage } } }");
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(3);

        assertThat(getField(jobNodes.get(0), "node", "id")).isEqualTo("7");
        assertThat(getField(jobNodes.get(2), "node", "id")).isEqualTo("9");
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasPreviousPage")).isEqualTo(true);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasNextPage")).isEqualTo(false);
    }

    private void addJobData(int nbJobs) {
        List<JobData> jobData = createJobData(nbJobs);
        jobData.forEach(job -> entityManager.persist(job));
    }

    private List<JobData> createJobData(int count) {
        return IntStream.range(1, count + 1).mapToObj(index -> createJobData("job" + index)).collect(
                Collectors.toList());
    }

    private JobData createJobData(String name) {
        JobData jobData = new JobData();
        jobData.setJobName(name);
        jobData.setOnTaskErrorString(OnTaskError.NONE);
        jobData.setPriority(JobPriority.HIGH);
        jobData.setStatus(JobStatus.CANCELED);
        jobData.setOwner("owner");

        return jobData;
    }

    @Test
    public void testInvalidQuery() {
        Map<String, Object> queryResult = executeGraphqlQuery("invalid query");
        assertThat(getField(queryResult, "errors")).isNotNull();
    }

    @Test
    public void testQueryVersion() {
        String query = "{ version }";
        Map<String, Object> queryResult = executeGraphqlQuery(query);

        assertThat(getField(queryResult, "data", "version")).isEqualTo(
                Constants.VERSION_API);
    }

    @Test
    public void testQueryViewer() {
        String query = "{ viewer { login sessionId  } }";
        Map<String, Object> queryResult = executeGraphqlQuery(query);

        assertThat(getField(queryResult, "data", "viewer", "login")).isEqualTo(
                CONTEXT_LOGIN);
        assertThat(getField(queryResult, "data", "viewer", "sessionId")).isEqualTo(
                CONTEXT_SESSION_ID);
    }

    private Object getField(Object object, String... fields) {
        for (String field : fields) {
            object = ((Map<String, Object>) object).get(field);
        }

        return object;
    }

    private Map<String, Object> executeGraphqlQuery(String query) {
        return graphqlService.executeQuery(query, null,
                new GraphqlService.GraphqlContext(CONTEXT_SESSION_ID, CONTEXT_LOGIN),
                ImmutableMap.of());
    }

}
