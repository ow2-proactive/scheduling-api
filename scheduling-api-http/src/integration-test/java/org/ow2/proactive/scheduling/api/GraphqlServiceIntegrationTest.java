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

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ow2.proactive.authentication.UserData;
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
import org.ow2.proactive.scheduling.api.graphql.common.NullStatus;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Query;
import org.ow2.proactive.scheduling.api.graphql.service.GraphqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class GraphqlServiceIntegrationTest {

    private static final UserData CONTEXT_USER_DATA = new UserData();

    static {
        CONTEXT_USER_DATA.setUserName("bobot");
    }

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

        Map<String, Object> queryResult = executeGraphqlQuery("{ jobs { edges { node { id name onTaskError } } } }");
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        assertThat(jobNodes).hasSize(10);
    }

    @Rollback
    @Test
    @Transactional
    public void testOnErrorEnum() {
        addJobData(1);

        Map<String, Object> queryResult = executeGraphqlQuery("{ jobs { edges { node { id name onTaskError } } } }");
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        List<?> errors = (List<?>) getField(queryResult, "errors");
        assertThat(errors).isNull();

        assertThat(jobNodes).hasSize(1);
        Object onTaskError = ((Map) ((Map) jobNodes.get(0)).get("node")).get("onTaskError");
        assertThat(onTaskError).isEqualTo("PAUSE_TASK");
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsPaginated() {
        addJobData(DefaultValues.PAGE_SIZE + 10);

        Map<String, Object> queryResult = executeGraphqlQuery("{ jobs { totalCount edges { node { id name } } pageInfo { hasNextPage hasPreviousPage } } }");
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        assertThat(jobNodes).hasSize(DefaultValues.PAGE_SIZE);
        assertJobsHasPreviousPageIs(queryResult, false);
        assertJobsHasNextPageIs(queryResult, true);
        assertThat(getField(queryResult, "data", "jobs", "totalCount")).isEqualTo(DefaultValues.PAGE_SIZE + 10);
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
        assertJobsHasPreviousPageIs(queryResult, false);
        assertJobsHasNextPageIs(queryResult, true);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsPaginatedLastArgumentWith100Jobs() {
        addJobData(100);

        Map<String, Object> queryResult = executeJobQueryWithPagination(null, 20, null, null);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        assertThat(jobNodes).hasSize(20);
        assertJobsHasPreviousPageIs(queryResult, true);
        assertJobsHasNextPageIs(queryResult, false);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsPaginatedFirstArgument() {
        addJobData(10);

        Map<String, Object> queryResult = executeJobQueryWithPagination(3, null, null, null);

        checkJobsAndGetCursor(queryResult, 3, ImmutableMap.of(0, "job1", 2, "job3"), 2, false, true);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsPaginatedLastArgument() {
        addJobData(10);

        Map<String, Object> queryResult = executeJobQueryWithPagination(null, 3, null, null);

        checkJobsAndGetCursor(queryResult, 3, ImmutableMap.of(0, "job8", 2, "job10"), 0, true, false);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsPaginatedFirstAfterArgument() {
        addJobData(10);

        Map<String, Object> queryResult = executeJobQueryWithPagination(1, null, null, null);

        String cursor = checkJobsAndGetCursor(queryResult, 1, Collections.emptyMap(), 0, false, true);

        queryResult = executeJobQueryWithPagination(3, null, cursor, null);

        cursor = checkJobsAndGetCursor(queryResult, 3, ImmutableMap.of(0, "job2", 2, "job4"), 2, true, true);

        queryResult = executeJobQueryWithPagination(6, null, cursor, null);
        checkJobsAndGetCursor(queryResult, 6, ImmutableMap.of(0, "job5", 5, "job10"), 5, true, false);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsPaginatedLastBeforeArgument() {
        addJobData(10);

        Map<String, Object> queryResult = executeJobQueryWithPagination(null, 1, null, null);

        String cursor = checkJobsAndGetCursor(queryResult, 1, Collections.emptyMap(), 0, true, false);

        queryResult = executeJobQueryWithPagination(null, 3, null, cursor);

        cursor = checkJobsAndGetCursor(queryResult, 3, ImmutableMap.of(0, "job7", 2, "job9"), 0, true, true);

        queryResult = executeJobQueryWithPagination(null, 6, null, cursor);
        checkJobsAndGetCursor(queryResult, 6, ImmutableMap.of(0, "job1", 5, "job6"), 0, false, true);
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

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{%s:\"!job5\"}) { edges { cursor node { id name } } } }",
                                                                            FILTER.getName(),
                                                                            NAME.getName()));

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(9);
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
    public void testQueryJobsFilterByEmptyParentId() {
        addJobData(5);
        addJobDataWithParentId(5);

        String queryTest = "{\n" + "  jobs (filter: {parentId: {nullStatus:%s}}){\n" + "    edges {\n" +
                           "      node {\n" + "        id\n" + "        parentId\n" + "        name\n" +
                           "        owner\n" + "        submittedTime\n" + "        status\n" + "      }\n" +
                           "    }\n" + "  }\n" + "}";

        Map<String, Object> queryResultAny = executeGraphqlQuery(String.format(queryTest, NullStatus.ANY));
        List<?> jobNodes = (List<?>) getField(queryResultAny, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(10);

        Map<String, Object> queryResultNotNull = executeGraphqlQuery(String.format(queryTest, NullStatus.NOT_NULL));
        jobNodes = (List<?>) getField(queryResultNotNull, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(5);
        assertThat(jobNodes.stream().allMatch(jobNode -> getField(jobNode, "node", "parentId") != null)).isTrue();

        Map<String, Object> queryResultNull = executeGraphqlQuery(String.format(queryTest, NullStatus.NULL));
        jobNodes = (List<?>) getField(queryResultNull, "data", "jobs", "edges");
        assertThat(jobNodes.stream().allMatch(jobNode -> getField(jobNode, "node", "parentId") == null)).isTrue();
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByStartAtIsNull() {
        List<List<JobData>> jobDatas = Lists.partition(addJobData(10), 5);
        List<JobData> jobDataWithStartAt = jobDatas.get(0);
        jobDataWithStartAt.forEach(jobData -> {
            jobData.setStartAt(Instant.now().toEpochMilli());
            entityManager.persist(jobData);
        });

        String queryTest = "{\n" + "  jobs (filter: {startAt: {nullStatus:%s}}){\n" + "    edges {\n" +
                           "      node {\n" + "        id\n" + "        startAt\n" + "        name\n" +
                           "        owner\n" + "        submittedTime\n" + "        status\n" + "      }\n" +
                           "    }\n" + "  }\n" + "}";

        Map<String, Object> queryResultAny = executeGraphqlQuery(String.format(queryTest, NullStatus.ANY));
        List<?> jobNodes = (List<?>) getField(queryResultAny, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(10);

        Map<String, Object> queryResultNotNull = executeGraphqlQuery(String.format(queryTest, NullStatus.NOT_NULL));
        jobNodes = (List<?>) getField(queryResultNotNull, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(5);
        assertThat(jobNodes.stream().allMatch(jobNode -> getField(jobNode, "node", "startAt") != null)).isTrue();

        Map<String, Object> queryResultNull = executeGraphqlQuery(String.format(queryTest, NullStatus.NULL));
        jobNodes = (List<?>) getField(queryResultNull, "data", "jobs", "edges");
        assertThat(jobNodes.stream().allMatch(jobNode -> getField(jobNode, "node", "startAt") == null)).isTrue();
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByStartAtBeforeAndAfter() {
        long nowMilli = Instant.now().toEpochMilli();
        long nowMinusOneMinuteMilli = nowMilli - 60000L;
        long nowPlusOneMinuteMilli = nowMilli + 60000L;

        List<List<JobData>> jobDatas = Lists.partition(addJobData(10), 5);
        List<JobData> jobDataWithStartAtBeforeNow = jobDatas.get(0);
        List<JobData> jobDataWithStartAtAfterNow = jobDatas.get(1);

        jobDataWithStartAtBeforeNow.forEach(jobData -> {
            jobData.setStartAt(nowMinusOneMinuteMilli);
            entityManager.persist(jobData);
        });

        jobDataWithStartAtAfterNow.forEach(jobData -> {
            jobData.setStartAt(nowPlusOneMinuteMilli);
            entityManager.persist(jobData);
        });

        String queryTest = "{\n" + "  jobs (filter: {startAt: {%s:%s}}){\n" + "    edges {\n" + "      node {\n" +
                           "        id\n" + "        startAt\n" + "        name\n" + "        owner\n" +
                           "        submittedTime\n" + "        status\n" + "      }\n" + "    }\n" + "  }\n" + "}";

        Map<String, Object> queryResultBefore = executeGraphqlQuery(String.format(queryTest, "before", nowMilli));
        List<?> jobNodes = (List<?>) getField(queryResultBefore, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(5);
        assertThat(jobNodes.stream()
                           .allMatch(jobNode -> (Long) getField(jobNode, "node", "startAt") < nowMilli)).isTrue();
        assertThat(jobNodes.stream()
                           .allMatch(jobNode -> jobDataWithStartAtBeforeNow.stream()
                                                                           .anyMatch(jobData -> jobData.getId()
                                                                                                       .equals(getField(jobNode,
                                                                                                                        "node",
                                                                                                                        "id")))));

        Map<String, Object> queryResultAfter = executeGraphqlQuery(String.format(queryTest, "after", nowMilli));
        jobNodes = (List<?>) getField(queryResultAfter, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(5);
        assertThat(jobNodes.stream()
                           .allMatch(jobNode -> (Long) getField(jobNode, "node", "startAt") > nowMilli)).isTrue();
        assertThat(jobNodes.stream()
                           .allMatch(jobNode -> jobDataWithStartAtAfterNow.stream()
                                                                          .anyMatch(jobData -> jobData.getId()
                                                                                                      .equals(getField(jobNode,
                                                                                                                       "node",
                                                                                                                       "id")))));

        Map<String, Object> queryResultAfterNoResult = executeGraphqlQuery(String.format(queryTest,
                                                                                         "after",
                                                                                         nowMilli + 600000));
        jobNodes = (List<?>) getField(queryResultAfterNoResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(0);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByNotContainNames() {
        addJobData(10);
        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{%s:\"!*ob7*\"}) { edges { cursor node { id name } } } }",
                                                                            FILTER.getName(),
                                                                            NAME.getName()));

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(9);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByEqualOwners() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:[{owner:\"%s\"} {owner:\"owner9\"}]) " +
                                                                            "{ edges { cursor node { id owner } } } }",
                                                                            FILTER.getName(),
                                                                            CONTEXT_USER_DATA.getUserName()));
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
                                                                            CONTEXT_USER_DATA.getUserName()));
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
                                                                            CONTEXT_USER_DATA.getUserName()));
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
                                                                            CONTEXT_USER_DATA.getUserName()));
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
    public void testQueryJobsFilterByNumberOfPendingTasks() {
        JobData job1 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.STALLED);
        job1.setNumberOfPendingTasks(2);
        entityManager.persist(job1);

        JobData job2 = createJobData("job2", "bobot", JobPriority.HIGH, "test", JobStatus.STALLED);
        job2.setNumberOfPendingTasks(1);
        entityManager.persist(job2);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{numberOfPendingTasks: {after: %d}}) " +
                                                                            "{ edges { cursor node { numberOfPendingTasks } } } }",
                                                                            FILTER.getName(),
                                                                            2));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);

        queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{numberOfPendingTasks: {before: %d}}) " +
                                                        "{ edges { cursor node { numberOfPendingTasks } } } }",
                                                        FILTER.getName(),
                                                        1));
        jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByNumberOfRunningTasks() {
        JobData job1 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.STALLED);
        job1.setNumberOfRunningTasks(2);
        entityManager.persist(job1);

        JobData job2 = createJobData("job2", "bobot", JobPriority.HIGH, "test", JobStatus.STALLED);
        job2.setNumberOfRunningTasks(1);
        entityManager.persist(job2);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{numberOfRunningTasks: {after: %d}}) " +
                                                                            "{ edges { cursor node { numberOfRunningTasks } } } }",
                                                                            FILTER.getName(),
                                                                            2));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);

        queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{numberOfRunningTasks: {before: %d}}) " +
                                                        "{ edges { cursor node { numberOfRunningTasks } } } }",
                                                        FILTER.getName(),
                                                        1));
        jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByNumberOfFinishedTasks() {
        JobData job1 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.STALLED);
        job1.setNumberOfFinishedTasks(2);
        entityManager.persist(job1);

        JobData job2 = createJobData("job2", "bobot", JobPriority.HIGH, "test", JobStatus.STALLED);
        job2.setNumberOfFinishedTasks(1);
        entityManager.persist(job2);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{numberOfFinishedTasks: {after: %d}}) " +
                                                                            "{ edges { cursor node { numberOfFinishedTasks } } } }",
                                                                            FILTER.getName(),
                                                                            2));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);

        queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{numberOfFinishedTasks: {before: %d}}) " +
                                                        "{ edges { cursor node { numberOfFinishedTasks } } } }",
                                                        FILTER.getName(),
                                                        1));
        jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByNumberOfFaultyTasks() {
        JobData job1 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.STALLED);
        job1.setNumberOfFaultyTasks(2);
        entityManager.persist(job1);

        JobData job2 = createJobData("job2", "bobot", JobPriority.HIGH, "test", JobStatus.STALLED);
        job2.setNumberOfFaultyTasks(1);
        entityManager.persist(job2);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{numberOfFaultyTasks: {after: %d}}) " +
                                                                            "{ edges { cursor node { numberOfFaultyTasks } } } }",
                                                                            FILTER.getName(),
                                                                            2));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);

        queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{numberOfFaultyTasks: {before: %d}}) " +
                                                        "{ edges { cursor node { numberOfFaultyTasks } } } }",
                                                        FILTER.getName(),
                                                        1));
        jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByNumberOfFailedTasks() {
        JobData job1 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.STALLED);
        job1.setNumberOfFailedTasks(2);
        entityManager.persist(job1);

        JobData job2 = createJobData("job2", "bobot", JobPriority.HIGH, "test", JobStatus.STALLED);
        job2.setNumberOfFailedTasks(1);
        entityManager.persist(job2);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{numberOfFailedTasks: {after: %d}}) " +
                                                                            "{ edges { cursor node { numberOfFailedTasks } } } }",
                                                                            FILTER.getName(),
                                                                            2));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);

        queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{numberOfFailedTasks: {before: %d}}) " +
                                                        "{ edges { cursor node { numberOfFailedTasks } } } }",
                                                        FILTER.getName(),
                                                        1));
        jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByNumberOfInErrorTasks() {
        JobData job1 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.STALLED);
        job1.setNumberOfInErrorTasks(2);
        entityManager.persist(job1);

        JobData job2 = createJobData("job2", "bobot", JobPriority.HIGH, "test", JobStatus.STALLED);
        job2.setNumberOfInErrorTasks(1);
        entityManager.persist(job2);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{numberOfInErrorTasks: {after: %d}}) " +
                                                                            "{ edges { cursor node { numberOfInErrorTasks } } } }",
                                                                            FILTER.getName(),
                                                                            2));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);

        queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{numberOfInErrorTasks: {before: %d}}) " +
                                                        "{ edges { cursor node { numberOfInErrorTasks } } } }",
                                                        FILTER.getName(),
                                                        1));
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
                                     "{ edges { cursor node { id owner } } } }",
                                     FILTER.getName(),
                                     CONTEXT_USER_DATA.getUserName());

        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        assertThat(jobNodes).hasSize(6);
    }

    @Rollback
    @Test
    @Transactional
    @Ignore("Exclude removed support has been disabled")
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
    public void testQueryJobsFilterBySubmittedTime() {
        JobData job1 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        job1.setSubmittedTime(100);
        entityManager.persist(job1);

        JobData job2 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        job2.setSubmittedTime(2000);
        entityManager.persist(job2);

        JobData job3 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        entityManager.persist(job3);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{submittedTime: {after: %s}}) " +
                                                                            "{ edges { cursor node { id owner submittedTime} } } }",
                                                                            FILTER.getName(),
                                                                            1000));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);

        queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{submittedTime: {before: %s}}) " +
                                                        "{ edges { cursor node { id owner submittedTime} } } }",
                                                        FILTER.getName(),
                                                        1000));
        jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByLastUpdatedTime() {
        JobData job1 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        job1.setSubmittedTime(100);
        job1.setLastUpdatedTime(job1.getSubmittedTime());
        entityManager.persist(job1);

        JobData job2 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        job2.setSubmittedTime(100);
        job2.setLastUpdatedTime(job2.getSubmittedTime() + 2000);
        entityManager.persist(job2);

        JobData job3 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        job3.setSubmittedTime(100);
        entityManager.persist(job3);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{lastUpdatedTime: {after: %s}}) " +
                                                                            "{ edges { cursor node { id owner lastUpdatedTime} } } }",
                                                                            FILTER.getName(),
                                                                            job2.getSubmittedTime() + 1000));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);

        queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{lastUpdatedTime: {before: %s}}) " +
                                                        "{ edges { cursor node { id owner lastUpdatedTime} } } }",
                                                        FILTER.getName(),
                                                        job2.getSubmittedTime() + 1000));
        jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByStartTime() {
        JobData job1 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        job1.setSubmittedTime(100);
        job1.setStartTime(job1.getSubmittedTime() + 10);
        entityManager.persist(job1);

        JobData job2 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        job2.setSubmittedTime(100);
        job2.setStartTime(job2.getSubmittedTime() + 2000);
        entityManager.persist(job2);

        JobData job3 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        job3.setSubmittedTime(100);
        entityManager.persist(job3);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{startTime: {after: %s}}) " +
                                                                            "{ edges { cursor node { id owner startTime} } } }",
                                                                            FILTER.getName(),
                                                                            job2.getSubmittedTime() + 1000));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);

        queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{startTime: {before: %s}}) " +
                                                        "{ edges { cursor node { id owner startTime} } } }",
                                                        FILTER.getName(),
                                                        job2.getSubmittedTime() + 1000));
        jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByFinishedTime() {
        JobData job1 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        job1.setSubmittedTime(100);
        job1.setFinishedTime(job1.getSubmittedTime() + 10);
        entityManager.persist(job1);

        JobData job2 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        job2.setSubmittedTime(100);
        job2.setFinishedTime(job2.getSubmittedTime() + 2000);
        entityManager.persist(job2);

        JobData job3 = createJobData("job1", "bobot", JobPriority.HIGH, "test", JobStatus.KILLED);
        job3.setSubmittedTime(100);
        entityManager.persist(job3);

        Map<String, Object> queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{finishedTime: {after: %s}}) " +
                                                                            "{ edges { cursor node { id owner finishedTime} } } }",
                                                                            FILTER.getName(),
                                                                            job2.getSubmittedTime() + 1000));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(1);

        queryResult = executeGraphqlQuery(String.format("{ jobs(%s:{finishedTime: {before: %s}}) " +
                                                        "{ edges { cursor node { id owner finishedTime} } } }",
                                                        FILTER.getName(),
                                                        job2.getSubmittedTime() + 1000));
        jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
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
                       "pageInfo { hasNextPage hasPreviousPage } } } } } }";

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
                       "{ edges { node { id } } pageInfo { hasNextPage hasPreviousPage } } } } } }";

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

        Map<String, Object> queryResult = executeJobQueryWithTasksPagination(3, null, null, null);

        checkTasksAndGetCursor(queryResult, 3, ImmutableMap.of(0, "0", 2, "2"), 0, 2, false, true);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryTasksPaginatedLastArgument() {
        addJobDataWithTasks(10);

        Map<String, Object> queryResult = executeJobQueryWithTasksPagination(null, 3, null, null);

        checkTasksAndGetCursor(queryResult, 3, ImmutableMap.of(0, "7", 2, "9"), 0, 0, true, false);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryTasksPaginatedFirstAfterArgument() {
        addJobDataWithTasks(10);

        Map<String, Object> queryResult = executeJobQueryWithTasksPagination(1, null, null, null);

        String cursor = checkTasksAndGetCursor(queryResult, 1, Collections.emptyMap(), 0, 0, false, true);

        queryResult = executeJobQueryWithTasksPagination(3, null, cursor, null);

        cursor = checkTasksAndGetCursor(queryResult, 3, ImmutableMap.of(0, "1", 2, "3"), 0, 2, true, true);

        queryResult = executeJobQueryWithTasksPagination(6, null, cursor, null);

        checkTasksAndGetCursor(queryResult, 6, ImmutableMap.of(0, "4", 5, "9"), 0, 5, true, false);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryTasksPaginatedLastBeforeArgument() {
        addJobDataWithTasks(10);

        Map<String, Object> queryResult = executeJobQueryWithTasksPagination(null, 1, null, null);

        String cursor = checkTasksAndGetCursor(queryResult, 1, Collections.emptyMap(), 0, 0, true, false);

        queryResult = executeJobQueryWithTasksPagination(null, 3, null, cursor);

        cursor = checkTasksAndGetCursor(queryResult, 3, ImmutableMap.of(0, "6", 2, "8"), 0, 0, true, true);

        queryResult = executeJobQueryWithTasksPagination(null, 6, null, cursor);

        checkTasksAndGetCursor(queryResult, 6, ImmutableMap.of(0, "0", 5, "5"), 0, 0, false, true);
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

        assertThat(getField(queryResult, "data", "viewer", "login")).isEqualTo(CONTEXT_USER_DATA.getUserName());
        assertThat(getField(queryResult, "data", "viewer", "sessionId")).isEqualTo(CONTEXT_SESSION_ID);
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryViewerJobs() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery("{ viewer { jobs  { edges { node { id owner } } } } }");

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "viewer", "jobs", "edges");
        assertThat(jobNodes).hasSize(5);

        jobNodes.forEach(jobNode -> assertThat(getField(jobNode,
                                                        "node",
                                                        "owner")).isEqualTo(CONTEXT_USER_DATA.getUserName()));
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

        jobNodes.forEach(jobNode -> assertThat(getField(jobNode,
                                                        "node",
                                                        "owner")).isEqualTo(CONTEXT_USER_DATA.getUserName()));
    }

    @Rollback
    @Test
    @Transactional
    public void testQueryJobsFilterByVariables() {
        addJobData(1);

        // Jobs which should not be matched
        // Job with null variable list
        JobData uselessJob1 = createJobData("Useless Toto 1", "ninipou", JobPriority.NORMAL, "", JobStatus.RUNNING);
        entityManager.persist(uselessJob1);
        // Job with a different variable
        JobData uselessJob2 = createJobData("Useless Toto 2", "ninipou", JobPriority.NORMAL, "", JobStatus.RUNNING);
        entityManager.persist(uselessJob2);
        Map<String, JobDataVariable> notMatchingVariables = new HashMap<>();
        JobDataVariable notMatchingVariable = createJobDataVariable(uselessJob2, "notMatching", "goodbye toto");
        notMatchingVariables.put("notMatching", notMatchingVariable);
        entityManager.persist(notMatchingVariable);
        uselessJob2.setVariables(notMatchingVariables);
        // Job with empty variable list
        JobData uselessJob3 = createJobData("Useless Toto 3", "ninipou", JobPriority.NORMAL, "", JobStatus.RUNNING);
        entityManager.persist(uselessJob3);
        Map<String, JobDataVariable> emptyVariables = new HashMap<>();
        uselessJob3.setVariables(emptyVariables);

        // Jobs which should be matched
        JobData importantJob1 = createJobData("Important Job 1", "ninipou", JobPriority.HIGH, "", JobStatus.RUNNING);
        entityManager.persist(importantJob1);
        Map<String, JobDataVariable> variables1 = new HashMap<>();
        JobDataVariable cmdVariable1 = createJobDataVariable(importantJob1, "cmd", "echo hello toto 1 !");
        variables1.put("cmd", cmdVariable1);
        entityManager.persist(cmdVariable1);
        JobDataVariable argTotoVariable = createJobDataVariable(importantJob1, "arg", "toto");
        variables1.put("arg", argTotoVariable);
        entityManager.persist(argTotoVariable);
        importantJob1.setVariables(variables1);

        JobData importantJob2 = createJobData("Important Job 2", "ninipou", JobPriority.HIGH, "", JobStatus.RUNNING);
        entityManager.persist(importantJob2);
        Map<String, JobDataVariable> variables2 = new HashMap<>();
        JobDataVariable cmdVariable2 = createJobDataVariable(importantJob2, "cmd", "echo hello toto 2 !");
        variables2.put("cmd", cmdVariable2);
        entityManager.persist(cmdVariable2);
        importantJob2.setVariables(variables2);

        // Should match all jobs as the composite filter matches either the job name or the variables
        Map<String, Object> queryAllTotoJobsResult = executeGraphqlQuery("{ jobs (filter: [{variables: " +
                                                                         "[{key: \"cmd\", value: \"%\"}]} {name: \"Useless*\"}]) { totalCount edges { node { id name variables {key value} } } } }");
        System.out.println(queryAllTotoJobsResult);
        assertThat((Integer) getField(queryAllTotoJobsResult, "data", "jobs", "totalCount")).isEqualTo(5);

        // Should match "Important Job 1" and "Important Job 2"
        Map<String, Object> queryImportantJobResultByKey = executeGraphqlQuery("{ jobs (filter: [{variables: " +
                                                                               "[{key: \"cmd\"}]}]) { totalCount edges { node { id name variables {key value} } } } }");
        System.out.println(queryImportantJobResultByKey);
        assertThat((Integer) getField(queryImportantJobResultByKey, "data", "jobs", "totalCount")).isEqualTo(2);

        // Should match "Important Job 1" and "Important Job 2"
        Map<String, Object> queryImportantJobResult = executeGraphqlQuery("{ jobs (filter: {variables: " +
                                                                          "[{key: \"cmd\", value: \"%\"}]}) { totalCount edges { node { id name variables {key value} } } } }");
        System.out.println(queryImportantJobResult);
        assertThat((Integer) getField(queryImportantJobResult, "data", "jobs", "totalCount")).isEqualTo(2);

        // Should match "Important Job 1" only
        // NOTE: due to an issue in HSQLDB introduced in version 2.6, a like filter with "something%" fails with a SQL error General Error 458. This appears only in queries generated by this test but not on other queries (also not in other projects such as the catalog)
        Map<String, Object> queryImportantJobResultWithStricterFilter = executeGraphqlQuery("{ jobs (filter: " +
                                                                                            "{variables: [{key: \"cmd\", value: \"echo hello%!\"}, {key: \"arg\", value: \"toto\"}]}) " +
                                                                                            "{ totalCount edges { node { id name variables {key value} } } } }");
        System.out.println(queryImportantJobResultWithStricterFilter);
        assertThat((Integer) getField(queryImportantJobResultWithStricterFilter,
                                      "data",
                                      "jobs",
                                      "totalCount")).isEqualTo(1);

        // Should match all five jobs, and only 5 (no duplicates) !
        Map<String, Object> queryJobResultWithUnionFilter = executeGraphqlQuery("{ jobs (filter: " +
                                                                                "[{variables: {key: \"arg\", value: \"toto\"}}, {variables: {key: \"cmd\", value: \"echo hello toto 2 !\"}}, " +
                                                                                "{name: \"Useless*\"}])" +
                                                                                "{ totalCount edges { node { id name variables {key value} } } } }");
        System.out.println(queryJobResultWithUnionFilter);
        assertThat((Integer) getField(queryJobResultWithUnionFilter, "data", "jobs", "totalCount")).isEqualTo(5);
    }

    private JobDataVariable createJobDataVariable(JobData jobData, String key, String value) {
        JobDataVariable jobDataVariable = new JobDataVariable();
        jobDataVariable.setName(key);
        jobDataVariable.setValue(value);
        jobDataVariable.setJobData(jobData);
        return jobDataVariable;
    }

    private List<JobData> addJobData(int nbJobs) {
        List<JobData> jobData = createJobData(nbJobs);
        jobData.forEach(job -> entityManager.persist(job));
        return jobData;
    }

    private void addJobDataWithParentId(int nbJobs) {
        List<JobData> jobData = createJobDataWithParentId(nbJobs);
        jobData.forEach(job -> entityManager.persist(job));
    }

    private void addJobDataWithTasks(int nbTasks) {
        JobData jobData = createJobData("job" + UUID.randomUUID().toString(),
                                        CONTEXT_USER_DATA.getUserName(),
                                        JobPriority.HIGH,
                                        "projectName",
                                        JobStatus.RUNNING);

        entityManager.persist(jobData);

        createTaskData(jobData, nbTasks).forEach(taskData -> entityManager.persist(taskData));
    }

    private List<JobData> createJobData(int count) {
        return IntStream.range(1, count + 1)
                        .mapToObj(index -> createJobData("job" + index,
                                                         index % 2 == 0 ? CONTEXT_USER_DATA.getUserName()
                                                                        : "owner" + index,
                                                         index % 2 == 0 ? JobPriority.IDLE : JobPriority.HIGH,
                                                         "projectName" + index,
                                                         index % 2 == 0 ? JobStatus.CANCELED : JobStatus.KILLED))
                        .collect(Collectors.toList());
    }

    private List<JobData> createJobDataWithParentId(int count) {
        return IntStream.range(1, count + 1)
                        .mapToObj(index -> createJobData("job" + index,
                                                         index % 2 == 0 ? CONTEXT_USER_DATA.getUserName()
                                                                        : "owner" + index,
                                                         index % 2 == 0 ? JobPriority.IDLE : JobPriority.HIGH,
                                                         "projectName" + index,
                                                         index % 2 == 0 ? JobStatus.CANCELED : JobStatus.KILLED,
                                                         (long) count))
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
        jobData.setOnTaskErrorString(OnTaskError.PAUSE_TASK.toString());

        return jobData;
    }

    private JobData createJobData(String name, String owner, JobPriority priority, String projectName, JobStatus status,
            Long parentId) {
        JobData jobData = new JobData();
        jobData.setJobName(name);
        jobData.setOwner(owner);
        jobData.setPriority(priority);
        jobData.setProjectName(projectName);
        jobData.setStatus(status);
        jobData.setParentId(parentId);
        jobData.setOnTaskErrorString(OnTaskError.PAUSE_TASK.toString());

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
        taskData.setOnTaskErrorString(OnTaskError.PAUSE_TASK.toString());
        taskData.setRestartModeId(RestartMode.ANYWHERE.getIndex());
        taskData.setTaskName(name);
        taskData.setTaskStatus(id % 2 == 0 ? TaskStatus.SUBMITTED : TaskStatus.IN_ERROR);
        taskData.setTaskType("FORKED_SCRIPT_TASK");
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
                                           new GraphqlContext(CONTEXT_USER_DATA, CONTEXT_SESSION_ID),
                                           variables);
    }

    private Map<String, Object> executeJobQueryWithPagination(Integer first, Integer last, String after,
            String before) {
        return executeGraphqlQuery(String.format("{ jobs(" + (first != null ? " first: " + first : "") +
                                                 (last != null ? " last: " + last : "") +
                                                 (after != null ? " after: \"" + after + "\"" : "") +
                                                 (before != null ? " before: \"" + before + "\"" : "") +
                                                 " ) { edges { cursor node { id name } } " +
                                                 "pageInfo { hasNextPage hasPreviousPage } } }"));
    }

    private Map<String, Object> executeJobQueryWithTasksPagination(Integer first, Integer last, String after,
            String before) {
        return executeGraphqlQuery(String.format("{ jobs { edges { cursor node { id tasks(" +
                                                 (first != null ? " first: " + first : "") +
                                                 (last != null ? " last: " + last : "") +
                                                 (after != null ? " after: \"" + after + "\"" : "") +
                                                 (before != null ? " before: \"" + before + "\"" : "") +
                                                 " ) { edges { cursor node { id name } } pageInfo { hasNextPage hasPreviousPage } } } } } }"));
    }

    private String checkJobsAndGetCursor(Map<String, Object> queryResult, int expectedSize,
            Map<Integer, String> expectedJobNames, int cursorIndex, boolean hasPrevious, boolean hasNext) {
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(expectedSize);
        expectedJobNames.entrySet()
                        .stream()
                        .forEach(indexName -> assertNameInListEqual(jobNodes,
                                                                    indexName.getKey(),
                                                                    indexName.getValue()));
        assertJobsHasPreviousPageIs(queryResult, hasPrevious);
        assertJobsHasNextPageIs(queryResult, hasNext);
        return (String) getField(jobNodes.get(cursorIndex), "cursor");
    }

    private String checkTasksAndGetCursor(Map<String, Object> queryResult, int expectedSize,
            Map<Integer, String> expectedTasksIds, int jobIndex, int cursorIndex, boolean hasPrevious,
            boolean hasNext) {

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        Object jobNode = jobNodes.get(jobIndex);
        List<?> taskNodes = (List<?>) getField(jobNode, "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(expectedSize);

        expectedTasksIds.entrySet()
                        .stream()
                        .forEach(indexName -> assertIdInListEqual(taskNodes, indexName.getKey(), indexName.getValue()));

        assertThat(getField(jobNode, "node", "tasks", "pageInfo", "hasPreviousPage")).isEqualTo(hasPrevious);
        assertThat(getField(jobNode, "node", "tasks", "pageInfo", "hasNextPage")).isEqualTo(hasNext);

        Object firstTaskNode = taskNodes.get(cursorIndex);
        return (String) getField(firstTaskNode, "cursor");
    }

    private void assertNameInListEqual(List<?> jobNodes, int index, String name) {
        assertThat(getField(jobNodes.get(index), "node", "name")).isEqualTo(name);
    }

    private void assertIdInListEqual(List<?> jobNodes, int index, String name) {
        assertThat(getField(jobNodes.get(index), "node", "id")).isEqualTo(name);
    }

    private void assertJobsHasPreviousPageIs(Map<String, Object> queryResult, boolean expected) {
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasPreviousPage")).isEqualTo(expected);
    }

    private void assertJobsHasNextPageIs(Map<String, Object> queryResult, boolean expected) {
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasNextPage")).isEqualTo(expected);
    }

}
