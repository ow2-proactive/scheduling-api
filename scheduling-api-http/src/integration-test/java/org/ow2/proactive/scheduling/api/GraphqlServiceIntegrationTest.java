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
package org.ow2.proactive.scheduling.api;

import static com.google.common.truth.Truth.assertThat;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.FILTER;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.NAME;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ow2.proactive.scheduler.common.job.JobPriority;
import org.ow2.proactive.scheduler.common.job.JobStatus;
import org.ow2.proactive.scheduler.common.task.OnTaskError;
import org.ow2.proactive.scheduler.common.task.RestartMode;
import org.ow2.proactive.scheduler.common.task.TaskStatus;
import org.ow2.proactive.scheduler.core.db.JobData;
import org.ow2.proactive.scheduler.core.db.JobDataVariable;
import org.ow2.proactive.scheduler.core.db.TaskData;
import org.ow2.proactive.scheduling.api.graphql.common.DefaultValues;
import org.ow2.proactive.scheduling.api.graphql.common.GraphqlContext;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Query;
import org.ow2.proactive.scheduling.api.graphql.service.GraphqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableMap;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class GraphqlServiceIntegrationTest {

    private static final String CONTEXT_LOGIN = "bobot";

    private static final String CONTEXT_SESSION_ID = "sessionId";

    @Autowired
    private GraphqlService graphqlService;

    @PersistenceContext
    private EntityManager entityManager;

    @Rollback
    @Test
    @Transactional
    public void testQueryJobs() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery("{ jobs { edges { node { id name } } } }");
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        assertThat(jobNodes).hasSize(10);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsPaginated() {
        addJobData(DefaultValues.PAGE_SIZE + 10);

        Map<String, Object> queryResult = executeGraphqlQuery("{ jobs { totalCount edges { node { id name } } pageInfo { hasNextPage hasPreviousPage } } }");
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        assertThat(jobNodes).hasSize(DefaultValues.PAGE_SIZE);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasPreviousPage")).isEqualTo(false);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasNextPage")).isEqualTo(true);
        assertThat(getField(queryResult, "data", "jobs", "totalCount")).isEqualTo(DefaultValues.PAGE_SIZE + 10);
        //        tearDown();
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsPaginatedWithVariable() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery("query($count:Int!) { jobs(first: $count) { edges { node { id name } } " +
                                                              "pageInfo { hasNextPage hasPreviousPage } } }",
                                                              ImmutableMap.of("count", 2));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        assertThat(jobNodes).hasSize(2);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasPreviousPage")).isEqualTo(false);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasNextPage")).isEqualTo(true);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsPaginatedFirstArgument() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery("{ jobs(first: 3) { edges { node { id name } } pageInfo { hasNextPage hasPreviousPage } } }");
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(3);

        assertThat(getField(jobNodes.get(0), "node", "name")).isEqualTo("job1");
        assertThat(getField(jobNodes.get(2), "node", "name")).isEqualTo("job3");
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasPreviousPage")).isEqualTo(false);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasNextPage")).isEqualTo(true);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsPaginatedLastArgument() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery("{ jobs(last: 3) { edges { node { id name } } pageInfo { hasNextPage hasPreviousPage } } }");
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(3);

        assertThat(getField(jobNodes.get(0), "node", "name")).isEqualTo("job8");
        assertThat(getField(jobNodes.get(2), "node", "name")).isEqualTo("job10");
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasPreviousPage")).isEqualTo(true);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasNextPage")).isEqualTo(false);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsPaginatedFirstAfterArgument() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery("{ jobs(first: 1) { edges { cursor node { id name } } } }");

        List<Object> edges = (List<Object>) getField(queryResult, "data", "jobs", "edges");

        String cursor = (String) getField(edges.get(0), "cursor");

        queryResult = executeGraphqlQuery(String.format("{ jobs(first: 3 after: \"%s\") { edges { node { id name } } " +
                                                        "pageInfo { hasNextPage hasPreviousPage } } }", cursor));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(3);

        assertThat(getField(jobNodes.get(0), "node", "name")).isEqualTo("job2");
        assertThat(getField(jobNodes.get(2), "node", "name")).isEqualTo("job4");
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasPreviousPage")).isEqualTo(false);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasNextPage")).isEqualTo(true);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsPaginatedLastBeforeArgument() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery("{ jobs(last: 1) { edges { cursor node { id name } } } }");

        List<Object> edges = (List<Object>) getField(queryResult, "data", "jobs", "edges");

        String cursor = (String) getField(edges.get(0), "cursor");

        queryResult = executeGraphqlQuery(String.format("{ jobs(last: 3 before: \"%s\") { edges { node { id name } } " +
                                                        "pageInfo { hasNextPage hasPreviousPage } } }", cursor));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(3);

        assertThat(getField(jobNodes.get(0), "node", "name")).isEqualTo("job7");
        assertThat(getField(jobNodes.get(2), "node", "name")).isEqualTo("job9");
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasPreviousPage")).isEqualTo(true);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasNextPage")).isEqualTo(false);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByIds() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:[{%s:\"job7\"} {%s:\"job9\"}]) { edges { cursor node { id name } } } }",
                                                                            FILTER.getName(),
                                                                            NAME.getName(),
                                                                            NAME.getName()));

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        String job7id = (String) getField(jobNodes.get(0), "node", "id");
        String job9id = (String) getField(jobNodes.get(1), "node", "id");

        queryResult = executeGraphqlQuery(String.format("{ jobs(%s:[{id:" + job7id + "}, {id:" + job9id +
                                                        "}]) { edges { cursor node { id name } } } }",
                                                        FILTER.getName()));
        jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(2);

        assertThat(getField(jobNodes.get(0), "node", "id")).isEqualTo(job7id);
        assertThat(getField(jobNodes.get(1), "node", "id")).isEqualTo(job9id);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByEqualNames() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:[{%s:\"job7\"} {%s:\"job9\"}]) { edges { cursor node { id name } } } }",
                                                                            FILTER.getName(),
                                                                            NAME.getName(),
                                                                            NAME.getName()));

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(2);

        assertThat(getField(jobNodes.get(0), "node", "name")).isEqualTo("job7");
        assertThat(getField(jobNodes.get(1), "node", "name")).isEqualTo("job9");
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByNotEqualNames() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{%s:\"!fakeJob0\"}) { edges { cursor node { id name } } } }",
                                                                            FILTER.getName(),
                                                                            NAME.getName()));

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(10);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByContainNames() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{%s:\"*ob*\"}) { edges { cursor node { id name } } } }",
                                                                            FILTER.getName(),
                                                                            NAME.getName()));

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(10);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByNotContainNames() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{%s:\"!*ob*\"}) { edges { cursor node { id name } } } }",
                                                                            FILTER.getName(),
                                                                            NAME.getName()));

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(0);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByEqualOwners() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:[{owner:\"%s\"} {owner:\"owner9\"}]) " +
                                                                            "{ edges { cursor node { id owner } } } }",
                                                                            FILTER.getName(),
                                                                            CONTEXT_LOGIN));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(6);

        assertThat(getField(jobNodes.get(4), "node", "owner")).isEqualTo("owner9");
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByNotEqualOwners() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{owner:\"!bobot\"}) " +
                                                                            "{ edges { cursor node { id owner } } } }",
                                                                            FILTER.getName(),
                                                                            CONTEXT_LOGIN));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(5);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByContainOwners() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:[{owner:\"*ow*\"} {owner:\"*ner*\"}]) " +
                                                                            "{ edges { cursor node { id owner } } } }",
                                                                            FILTER.getName(),
                                                                            CONTEXT_LOGIN));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(5);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByNotContainOwners() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{owner:\"!*owner*\"}) " +
                                                                            "{ edges { cursor node { id owner } } } }",
                                                                            FILTER.getName(),
                                                                            CONTEXT_LOGIN));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(5);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByComparableId() {
        JobData job1 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        entityManager.persist(job1);

        JobData job2 = createJobData("job2", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        entityManager.persist(job2);

        long min = Math.min(job1.getId(), job2.getId());
        long max = Math.max(job1.getId(), job2.getId());

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{comparableId: {after: %d}}) " +
                                                                            "{ edges { cursor node { id } } } }",
                                                                            FILTER.getName(),
                                                                            max));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);

        queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{comparableId: {before: %d}}) " +
                                                        "{ edges { cursor node { id } } } }", FILTER.getName(), min));
        jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByPriority() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{priority:IDLE}) " +
                                                                            "{ edges { cursor node { id owner } } } }",
                                                                            FILTER.getName()));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(5);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByEqualProjectNames() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:[{projectName:\"projectName7\"}, {projectName:\"projectName9\"}]) " +
                                                                            "{ edges { cursor node { id name } } } }",
                                                                            FILTER.getName()));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(2);

        assertThat(getField(jobNodes.get(0), "node", "name")).isEqualTo("job7");
        assertThat(getField(jobNodes.get(1), "node", "name")).isEqualTo("job9");
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByNotEqualProjectNames() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:[{projectName:\"!fakeProjectName0\"}, {projectName:\"!fakeProjectName1\"}]) " +
                                                                            "{ edges { cursor node { id name } } } }",
                                                                            FILTER.getName()));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(10);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByContainProjectNames() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:[{projectName:\"*project*\"}, {projectName:\"*Name*\"}]) " +
                                                                            "{ edges { cursor node { id name } } } }",
                                                                            FILTER.getName()));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(10);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByNotContainProjectNames() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:[{projectName:\"!*project*\"}, {projectName:\"!*Name*\"}]) " +
                                                                            "{ edges { cursor node { id name } } } }",
                                                                            FILTER.getName()));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(0);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByEqualStatus() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{status: KILLED}) " +
                                                                            "{ edges { cursor node { id owner } } } }",
                                                                            FILTER.getName()));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(5);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterWithConjunctionsAndDisjunctions() {
        addJobData(10);

        String query = String.format("{ jobs(%s:[{owner:\"%s\" " +
                                     "priority:IDLE status:CANCELED},{projectName:\"projectName7\" status:KILLED}])" +
                                     "{ edges { cursor node { id owner } } } }", FILTER.getName(), CONTEXT_LOGIN);

        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        assertThat(jobNodes).hasSize(6);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByExcludeRemoved() {
        addJobData(10);
        JobData removedJob = createJobData("removed", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        removedJob.setRemovedTime(14232323);
        entityManager.persist(removedJob);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{status: KILLED}) " +
                                                                            "{ edges { cursor node { id owner } } } }",
                                                                            FILTER.getName()));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(5);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByLastUpdatedTime() {
        JobData job1 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        job1.setLastUpdatedTime(job1.getSubmittedTime());
        entityManager.persist(job1);

        JobData job2 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        job2.setLastUpdatedTime(job2.getSubmittedTime() + 2000);
        entityManager.persist(job2);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{lastUpdatedTime: {after: %s}}) " +
                                                                            "{ edges { cursor node { id owner lastUpdatedTime} } } }",
                                                                            FILTER.getName(),
                                                                            job2.getSubmittedTime() + 1000));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByIncludeRemoved() {
        addJobData(10);
        JobData removedJob = createJobData("removed", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        removedJob.setRemovedTime(14232323);
        entityManager.persist(removedJob);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{status: KILLED, excludeRemoved: false}) " +
                                                                            "{ edges { cursor node { id owner } } } }",
                                                                            FILTER.getName()));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(6);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryTasks() {
        addJobDataWithTasks(10);

        Map<String, Object> queryResult = executeGraphqlQuery("{ jobs { edges { cursor node { id tasks { edges { node { id } } } } } } }");

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        List<?> taskNodes = (List<?>) getField(jobNodes.get(0), "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(10);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryTasksPaginated() {
        addJobDataWithTasks(DefaultValues.PAGE_SIZE + 10);

        String query = "{ jobs { edges { cursor node { id tasks { edges { node { id } } " +
                       "pageInfo { hasNextPage hasPreviousPage } } } } } } }";

        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        Object firstJobNode = jobNodes.get(0);
        List<?> taskNodes = (List<?>) getField(firstJobNode, "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(DefaultValues.PAGE_SIZE);
        assertThat(getField(firstJobNode, "node", "tasks", "pageInfo", "hasPreviousPage")).isEqualTo(false);
        assertThat(getField(firstJobNode, "node", "tasks", "pageInfo", "hasNextPage")).isEqualTo(true);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryTasksPaginatedWithVariable() {
        addJobDataWithTasks(DefaultValues.PAGE_SIZE + 10);

        String query = "query($count:Int!) { jobs { edges { cursor node { id tasks(first: $count) " +
                       "{ edges { node { id } } pageInfo { hasNextPage hasPreviousPage } } } } } } }";

        Map<String, Object> queryResult = executeGraphqlQuery(query, ImmutableMap.of("count", 2));

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        Object firstJobNode = jobNodes.get(0);
        List<?> taskNodes = (List<?>) getField(firstJobNode, "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(2);
        assertThat(getField(firstJobNode, "node", "tasks", "pageInfo", "hasPreviousPage")).isEqualTo(false);
        assertThat(getField(firstJobNode, "node", "tasks", "pageInfo", "hasNextPage")).isEqualTo(true);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryTasksPaginatedFirstArgument() {
        addJobDataWithTasks(10);

        String query = "{ jobs { edges { cursor node { id tasks(first: 3) { edges { node { id } } " +
                       "pageInfo { hasNextPage hasPreviousPage } } } } } } }";

        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        Object firstJobNode = jobNodes.get(0);
        List<?> taskNodes = (List<?>) getField(firstJobNode, "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(3);
        assertThat(getField(taskNodes.get(0), "node", "id")).isEqualTo("0");
        assertThat(getField(taskNodes.get(2), "node", "id")).isEqualTo("2");
        assertThat(getField(firstJobNode, "node", "tasks", "pageInfo", "hasPreviousPage")).isEqualTo(false);
        assertThat(getField(firstJobNode, "node", "tasks", "pageInfo", "hasNextPage")).isEqualTo(true);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryTasksPaginatedLastArgument() {
        addJobDataWithTasks(10);

        String query = "{ jobs { edges { cursor node { id tasks(last: 3) { edges { node { id } } " +
                       "pageInfo { hasNextPage hasPreviousPage } } } } } } }";

        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        Object firstJobNode = jobNodes.get(0);
        List<?> taskNodes = (List<?>) getField(firstJobNode, "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(3);
        assertThat(getField(taskNodes.get(0), "node", "id")).isEqualTo("7");
        assertThat(getField(taskNodes.get(2), "node", "id")).isEqualTo("9");
        assertThat(getField(firstJobNode, "node", "tasks", "pageInfo", "hasPreviousPage")).isEqualTo(true);
        assertThat(getField(firstJobNode, "node", "tasks", "pageInfo", "hasNextPage")).isEqualTo(false);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryTasksPaginatedFirstAfterArgument() {
        addJobDataWithTasks(10);

        String query = "{ jobs { edges { cursor node { id tasks(first: 1) { edges { cursor node { id } } } } } } }";
        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        List<?> taskNodes = (List<?>) getField(jobNodes.get(0), "node", "tasks", "edges");

        Object firstTaskNode = taskNodes.get(0);
        String cursor = (String) getField(firstTaskNode, "cursor");

        query = String.format("{ jobs { edges { cursor node { id tasks(first: 3 after: \"%s\") { edges " +
                              "{ cursor node { id name } } pageInfo { hasNextPage hasPreviousPage } } } } } }", cursor);

        queryResult = executeGraphqlQuery(query);

        jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        Object firstJobNode = jobNodes.get(0);
        taskNodes = (List<?>) getField(firstJobNode, "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(3);

        assertThat(getField(taskNodes.get(0), "node", "id")).isEqualTo("1");
        assertThat(getField(taskNodes.get(2), "node", "id")).isEqualTo("3");
        assertThat(getField(firstJobNode, "node", "tasks", "pageInfo", "hasPreviousPage")).isEqualTo(false);
        assertThat(getField(firstJobNode, "node", "tasks", "pageInfo", "hasNextPage")).isEqualTo(true);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryTasksPaginatedLastBeforeArgument() {
        addJobDataWithTasks(10);

        String query = "{ jobs { edges { cursor node { id tasks(last: 1) { edges { cursor node { id } } } } } } }";
        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        List<?> taskNodes = (List<?>) getField(jobNodes.get(0), "node", "tasks", "edges");

        Object firstTaskNode = taskNodes.get(0);
        String cursor = (String) getField(firstTaskNode, "cursor");

        query = String.format("{ jobs { edges { cursor node { id tasks(last: 3 before: \"%s\") { edges { " +
                              "cursor node { id name } } pageInfo { hasNextPage hasPreviousPage } } } } } }", cursor);

        queryResult = executeGraphqlQuery(query);

        jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        Object firstJobNode = jobNodes.get(0);
        taskNodes = (List<?>) getField(firstJobNode, "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(3);

        assertThat(getField(taskNodes.get(0), "node", "id")).isEqualTo("6");
        assertThat(getField(taskNodes.get(2), "node", "id")).isEqualTo("8");
        assertThat(getField(firstJobNode, "node", "tasks", "pageInfo", "hasPreviousPage")).isEqualTo(true);
        assertThat(getField(firstJobNode, "node", "tasks", "pageInfo", "hasNextPage")).isEqualTo(false);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryTasksFilterByIds() {
        addJobDataWithTasks(10);

        String query = String.format("{ jobs { edges { cursor node { id tasks(%s:[{id:3} {id:5}]) " +
                                     "{ edges { node { id } } } } } } }", FILTER.getName());
        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        List<?> taskNodes = (List<?>) getField(jobNodes.get(0), "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(2);
        assertThat(getField(taskNodes.get(0), "node", "id")).isEqualTo("3");
        assertThat(getField(taskNodes.get(1), "node", "id")).isEqualTo("5");
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryTasksFilterByName() {
        addJobDataWithTasks(10);

        String query = String.format("{ jobs { edges { cursor node { id tasks(%s:[{%s:\"task4\"} {%s:\"task6\"}]) " +
                                     "{ edges { node { id } } } } } } }",
                                     FILTER.getName(),
                                     NAME.getName(),
                                     NAME.getName());
        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        List<?> taskNodes = (List<?>) getField(jobNodes.get(0), "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(2);
        assertThat(getField(taskNodes.get(0), "node", "id")).isEqualTo("3");
        assertThat(getField(taskNodes.get(1), "node", "id")).isEqualTo("5");
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryTasksFilterByStartWithName() {
        addJobDataWithTasks(10);

        String query = String.format("{ jobs { edges { cursor node { id tasks(%s:[{%s:\"task*\"}]) " +
                                     "{ edges { node { id } } } } } } }", FILTER.getName(), NAME.getName());
        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        List<?> taskNodes = (List<?>) getField(jobNodes.get(0), "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(10);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryTasksFilterByEndWithName() {
        addJobDataWithTasks(10);

        String query = String.format("{ jobs { edges { cursor node { id tasks(%s:[{%s:\"*4\"}]) " +
                                     "{ edges { node { id } } } } } } }", FILTER.getName(), NAME.getName());
        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        List<?> taskNodes = (List<?>) getField(jobNodes.get(0), "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(1);
        assertThat(getField(taskNodes.get(0), "node", "id")).isEqualTo("3");
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryTasksFilterByStatus() {
        addJobDataWithTasks(10);

        String query = String.format("{ jobs { edges { cursor node { id tasks(%s:{status:IN_ERROR}) " +
                                     "{ edges { node { id } } } } } } }", FILTER.getName());
        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        List<?> taskNodes = (List<?>) getField(jobNodes.get(0), "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(5);

        int last = 1;
        for (int i = 0; i < 5; i++) {
            assertThat(getField(taskNodes.get(i), "node", "id")).isEqualTo(Integer.toString(last));
            last += 2;
        }
    }

    @Rollback
    @Test
    @Transactional
    public void testInvalidQuery() {
        Map<String, Object> queryResult = executeGraphqlQuery("invalid query");
        assertThat(getField(queryResult, "errors")).isNotNull();
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryVersion() {
        String query = "{ version }";
        Map<String, Object> queryResult = executeGraphqlQuery(query);

        assertThat(getField(queryResult, "data", "version")).isEqualTo(Query.VERSION_API);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryViewer() {
        String query = "{ viewer { login sessionId  } }";
        Map<String, Object> queryResult = executeGraphqlQuery(query);

        assertThat(getField(queryResult, "data", "viewer", "login")).isEqualTo(CONTEXT_LOGIN);
        assertThat(getField(queryResult, "data", "viewer", "sessionId")).isEqualTo(CONTEXT_SESSION_ID);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryViewerJobs() {
        addJobData(10);
        JobData removedJob = createJobData("removed", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        removedJob.setRemovedTime(14232323);
        entityManager.persist(removedJob);

        Map<String, Object> queryResult = executeGraphqlQuery("{ viewer { jobs  { edges { node { id owner } } } } }");

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "viewer", "jobs", "edges");
        assertThat(jobNodes).hasSize(5);

        jobNodes.forEach(jobNode -> assertThat(getField(jobNode, "node", "owner")).isEqualTo(CONTEXT_LOGIN));
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryViewerIncludeRemovedJobs() {
        addJobData(10);
        JobData removedJob = createJobData("removed", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        removedJob.setRemovedTime(14232323);
        entityManager.persist(removedJob);

        Map<String, Object> queryResult = executeGraphqlQuery("{ viewer { jobs (filter: {excludeRemoved: false, status: KILLED}) { edges { node { id owner } } } } }");

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "viewer", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);

        jobNodes.forEach(jobNode -> assertThat(getField(jobNode, "node", "owner")).isEqualTo(CONTEXT_LOGIN));
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByVariables() {
        addJobData(1);

        JobData uselessJob = createJobData("Useless Toto Job", "ninipou", JobPriority.NORMAL, "", JobStatus.RUNNING);
        entityManager.persist(uselessJob);
        JobData importantJob = createJobData("Important Toto Job", "ninipou", JobPriority.HIGH, "", JobStatus.RUNNING);
        entityManager.persist(importantJob);
        Map<String, JobDataVariable> variables = new HashMap<>();
        JobDataVariable cmdVariable = createJobDataVariable(importantJob, "cmd", "echo hello toto !");
        variables.put("cmd", cmdVariable);
        entityManager.persist(cmdVariable);
        JobDataVariable argTotoVariable = createJobDataVariable(importantJob, "arg", "toto");
        variables.put("arg", argTotoVariable);
        entityManager.persist(argTotoVariable);
        importantJob.setVariables(variables);

        Map<String, Object> queryAllTotoJobsResult = executeGraphqlQuery("{ jobs (filter: [{variables: " +
                                                                         "[{key: \"cmd\", value: \"%\"}]} {name: \"*Job\"}]) { totalCount edges { node { name } } } }");
        assertThat((Integer) getField(queryAllTotoJobsResult, "data", "jobs", "totalCount")).isEqualTo(2);

        Map<String, Object> queryImportantJobResultByKey = executeGraphqlQuery("{ jobs (filter: [{variables: " +
                                                                               "[{key: \"cmd\"}]}]) { totalCount edges { node { name } } } }");
        assertThat((Integer) getField(queryImportantJobResultByKey, "data", "jobs", "totalCount")).isEqualTo(1);

        Map<String, Object> queryImportantJobResult = executeGraphqlQuery("{ jobs (filter: {variables: " +
                                                                          "[{key: \"cmd\", value: \"%\"}]}) { totalCount edges { node { name } } } }");
        assertThat((Integer) getField(queryImportantJobResult, "data", "jobs", "totalCount")).isEqualTo(1);

        Map<String, Object> queryImportantJobResultWithStricterFilter = executeGraphqlQuery("{ jobs (filter: " +
                                                                                            "{variables: [{key: \"cmd\", value: \"%\"}, {key: \"arg\", value: \"toto\"}]}) " +
                                                                                            "{ totalCount edges { node { name } } } }");
        assertThat((Integer) getField(queryImportantJobResultWithStricterFilter,
                                      "data",
                                      "jobs",
                                      "totalCount")).isEqualTo(1);
    }

    private JobDataVariable createJobDataVariable(JobData jobData, String key, String value) {
        JobDataVariable jobDataVariable = new JobDataVariable();
        jobDataVariable.setName(key);
        jobDataVariable.setValue(value);
        jobDataVariable.setJobData(jobData);
        return jobDataVariable;
    }

    private void addJobData(int nbJobs) {
        List<JobData> jobData = createJobData(nbJobs);
        jobData.forEach(job -> entityManager.persist(job));
    }

    private void addJobDataWithTasks(int nbTasks) {
        JobData jobData = createJobData("job" + UUID.randomUUID().toString(),
                                        CONTEXT_LOGIN,
                                        JobPriority.HIGH,
                                        "projectName",
                                        JobStatus.RUNNING);

        entityManager.persist(jobData);

        createTaskData(jobData, nbTasks).forEach(taskData -> entityManager.persist(taskData));
    }

    private List<JobData> createJobData(int count) {
        return IntStream.range(1, count + 1)
                        .mapToObj(index -> createJobData("job" + index,
                                                         index % 2 == 0 ? CONTEXT_LOGIN : "owner" + index,
                                                         index % 2 == 0 ? JobPriority.IDLE : JobPriority.HIGH,
                                                         "projectName" + index,
                                                         index % 2 == 0 ? JobStatus.CANCELED : JobStatus.KILLED))
                        .collect(Collectors.toList());
    }

    private JobData createJobData(String name, String owner, JobPriority priority, String projectName,
            JobStatus status) {
        JobData jobData = new JobData();
        jobData.setJobName(name);
        jobData.setOwner(owner);
        jobData.setPriority(priority);
        jobData.setProjectName(projectName);
        jobData.setStatus(status);
        jobData.setOnTaskErrorString(OnTaskError.NONE);

        return jobData;
    }

    private List<TaskData> createTaskData(JobData jobData, int nbTasks) {
        return IntStream.range(1, nbTasks + 1)
                        .mapToObj(index -> createTaskData(jobData, index - 1, "task" + index))
                        .collect(Collectors.toList());
    }

    private TaskData createTaskData(JobData jobData, long id, String name) {
        TaskData.DBTaskId dbTaskId = new TaskData.DBTaskId();
        dbTaskId.setJobId(jobData.getId());
        dbTaskId.setTaskId(id);

        TaskData taskData = new TaskData();
        taskData.setId(dbTaskId);
        taskData.setJobData(jobData);
        taskData.setOnTaskErrorString(OnTaskError.PAUSE_TASK);
        taskData.setRestartModeId(RestartMode.ANYWHERE.getIndex());
        taskData.setTaskName(name);
        taskData.setTaskStatus(id % 2 == 0 ? TaskStatus.SUBMITTED : TaskStatus.IN_ERROR);
        taskData.setTaskType("taskType");
        taskData.setVariables(ImmutableMap.of());

        return taskData;
    }

    private Object getField(Object object, String... fields) {
        for (String field : fields) {
            object = ((Map<String, Object>) object).get(field);
        }

        return object;
    }

    private Map<String, Object> executeGraphqlQuery(String query) {
        System.out.println(query);
        return executeGraphqlQuery(query, ImmutableMap.of());
    }

    private Map<String, Object> executeGraphqlQuery(String query, Map<String, Object> variables) {
        return graphqlService.executeQuery(query,
                                           null,
                                           new GraphqlContext(CONTEXT_LOGIN, CONTEXT_SESSION_ID),
                                           variables);
    }

}
