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
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;

import org.ow2.proactive.scheduler.common.job.JobPriority;
import org.ow2.proactive.scheduler.common.job.JobStatus;
import org.ow2.proactive.scheduler.common.task.OnTaskError;
import org.ow2.proactive.scheduler.common.task.RestartMode;
import org.ow2.proactive.scheduler.common.task.TaskStatus;
import org.ow2.proactive.scheduler.core.db.JobData;
import org.ow2.proactive.scheduler.core.db.TaskData;
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
import static org.ow2.proactive.scheduling.api.schema.type.inputs.InputFieldNameEnum.NAME;
import static org.ow2.proactive.scheduling.api.util.Constants.ARGUMENT_NAME_FILTER;


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
        addJobData(Constants.PAGINATION_DEFAULT_SIZE + 10);

        Map<String, Object> queryResult = executeGraphqlQuery(
                "{ jobs { edges { node { id name } } pageInfo { hasNextPage hasPreviousPage } } }");
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        assertThat(jobNodes).hasSize(Constants.PAGINATION_DEFAULT_SIZE);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasPreviousPage")).isEqualTo(false);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasNextPage")).isEqualTo(true);
    }

    @Test
    public void testQueryJobsPaginatedWithVariable() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(
                "query($count:Int!) { jobs(first: $count) { edges { node { id name } } " +
                        "pageInfo { hasNextPage hasPreviousPage } } }",
                ImmutableMap.of("count", 2));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        assertThat(jobNodes).hasSize(2);
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
                String.format(
                        "{ jobs(first: 3 after: \"%s\") { edges { node { id name } } " +
                                "pageInfo { hasNextPage hasPreviousPage } } }",
                        cursor));
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
                String.format(
                        "{ jobs(last: 3 before: \"%s\") { edges { node { id name } } " +
                                "pageInfo { hasNextPage hasPreviousPage } } }", cursor));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(3);

        assertThat(getField(jobNodes.get(0), "node", "id")).isEqualTo("7");
        assertThat(getField(jobNodes.get(2), "node", "id")).isEqualTo("9");
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasPreviousPage")).isEqualTo(true);
        assertThat(getField(queryResult, "data", "jobs", "pageInfo", "hasNextPage")).isEqualTo(false);
    }

    @Test
    public void testQueryJobsFilterByIds() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(
                String.format("{ jobs(%s:[{id:3}, {id:5}]) { edges { cursor node { id name } } } }",
                        ARGUMENT_NAME_FILTER));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(2);

        assertThat(getField(jobNodes.get(0), "node", "id")).isEqualTo("3");
        assertThat(getField(jobNodes.get(1), "node", "id")).isEqualTo("5");
    }

    @Test
    public void testQueryJobsFilterByNames() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(
                String.format(
                        "{ jobs(%s:[{%s:\"job7\"} {%s:\"job9\"}]) { edges { cursor node { id name } } } }",
                        ARGUMENT_NAME_FILTER, NAME.value(), NAME.value()));

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(2);

        assertThat(getField(jobNodes.get(0), "node", "id")).isEqualTo("7");
        assertThat(getField(jobNodes.get(1), "node", "id")).isEqualTo("9");
    }

    @Test
    public void testQueryJobsFilterByOwners() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(
                String.format("{ jobs(%s:[{owner:\"%s\"} {owner:\"owner9\"}]) " +
                        "{ edges { cursor node { id owner } } } }", ARGUMENT_NAME_FILTER, CONTEXT_LOGIN));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(6);

        assertThat(getField(jobNodes.get(4), "node", "id")).isEqualTo("9");
    }

    @Test
    public void testQueryJobsFilterByPriority() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(
                String.format("{ jobs(%s:{status: KILLED}) " +
                        "{ edges { cursor node { id owner } } } }", ARGUMENT_NAME_FILTER));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(5);
    }

    @Test
    public void testQueryJobsFilterByProjectNames() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(
                String.format("{ jobs(%s:[{projectName:\"projectName7\"}, {projectName:\"projectName9\"}]) " +
                        "{ edges { cursor node { id name } } } }", ARGUMENT_NAME_FILTER));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(2);

        assertThat(getField(jobNodes.get(0), "node", "id")).isEqualTo("7");
        assertThat(getField(jobNodes.get(1), "node", "id")).isEqualTo("9");
    }

    @Test
    public void testQueryJobsFilterByStatus() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(
                String.format("{ jobs(%s:{status: KILLED}) " +
                        "{ edges { cursor node { id owner } } } }", ARGUMENT_NAME_FILTER));
        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        assertThat(jobNodes).hasSize(5);
    }

    @Test
    public void testQueryJobsFilterWithConjunctionsAndDisjunctions() {
        addJobData(10);

        String query = String.format(
                "{ jobs(%s:[{owner:\"%s\" " +
                        "priority:IDLE status:CANCELED},{projectName:\"projectName7\" status:KILLED}])" +
                        "{ edges { cursor node { id owner } } } }", ARGUMENT_NAME_FILTER, CONTEXT_LOGIN);

        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        assertThat(jobNodes).hasSize(6);
    }

    @Test
    public void testQueryTasks() {
        addJobDataWithTasks(10);

        Map<String, Object> queryResult = executeGraphqlQuery(
                "{ jobs { edges { cursor node { id tasks { edges { node { id } } } } } } }");

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        List<?> taskNodes = (List<?>) getField(jobNodes.get(0), "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(10);
    }

    @Test
    public void testQueryTasksPaginated() {
        addJobDataWithTasks(Constants.PAGINATION_DEFAULT_SIZE + 10);

        String query = "{ jobs { edges { cursor node { id tasks { edges { node { id } } " +
                "pageInfo { hasNextPage hasPreviousPage } } } } } } }";

        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");

        Object firstJobNode = jobNodes.get(0);
        List<?> taskNodes = (List<?>) getField(firstJobNode, "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(Constants.PAGINATION_DEFAULT_SIZE);
        assertThat(getField(firstJobNode, "node", "tasks", "pageInfo", "hasPreviousPage")).isEqualTo(false);
        assertThat(getField(firstJobNode, "node", "tasks", "pageInfo", "hasNextPage")).isEqualTo(true);
    }

    @Test
    public void testQueryTasksPaginatedWithVariable() {
        addJobDataWithTasks(Constants.PAGINATION_DEFAULT_SIZE + 10);

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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
    public void testQueryTasksFilterByIds() {
        addJobDataWithTasks(10);

        String query = String.format(
                "{ jobs { edges { cursor node { id tasks(%s:[{id:3} {id:5}]) " +
                        "{ edges { node { id } } } } } } }", ARGUMENT_NAME_FILTER);
        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        List<?> taskNodes = (List<?>) getField(jobNodes.get(0), "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(2);
        assertThat(getField(taskNodes.get(0), "node", "id")).isEqualTo("3");
        assertThat(getField(taskNodes.get(1), "node", "id")).isEqualTo("5");
    }

    @Test
    public void testQueryTasksFilterByName() {
        addJobDataWithTasks(10);

        String query = String.format(
                "{ jobs { edges { cursor node { id tasks(%s:[{%s:\"task4\"} {%s:\"task6\"}]) " +
                        "{ edges { node { id } } } } } } }",
                ARGUMENT_NAME_FILTER, NAME.value(), NAME.value());
        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        List<?> taskNodes = (List<?>) getField(jobNodes.get(0), "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(2);
        assertThat(getField(taskNodes.get(0), "node", "id")).isEqualTo("3");
        assertThat(getField(taskNodes.get(1), "node", "id")).isEqualTo("5");
    }

    @Test
    public void testQueryTasksFilterByStatus() {
        addJobDataWithTasks(10);

        String query = String.format("{ jobs { edges { cursor node { id tasks(%s:{status:IN_ERROR}) " +
                "{ edges { node { id } } } } } } }", ARGUMENT_NAME_FILTER);
        Map<String, Object> queryResult = executeGraphqlQuery(query);

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "jobs", "edges");
        List<?> taskNodes = (List<?>) getField(jobNodes.get(0), "node", "tasks", "edges");

        assertThat(taskNodes).hasSize(5);

        int last = 1;
        for (int i = 0; i < 5; i++) {
            assertThat(getField(taskNodes.get(i), "node", "id"))
                    .isEqualTo(Integer.toString(last));
            last += 2;
        }
    }

    private void addJobData(int nbJobs) {
        List<JobData> jobData = createJobData(nbJobs);
        jobData.forEach(job -> entityManager.persist(job));
    }

    private void addJobDataWithTasks(int nbTasks) {
        JobData jobData =
                createJobData(
                        "job" + UUID.randomUUID().toString(),
                        CONTEXT_LOGIN, JobPriority.HIGH,
                        "projectName", JobStatus.RUNNING);

        entityManager.persist(jobData);

        createTaskData(jobData, nbTasks).forEach(taskData -> entityManager.persist(taskData));
    }

    private List<JobData> createJobData(int count) {
        return IntStream.range(1, count + 1).mapToObj(index -> createJobData("job" + index,
                index % 2 == 0 ? CONTEXT_LOGIN : "owner" + index,
                index % 2 == 0 ? JobPriority.IDLE : JobPriority.HIGH, "projectName" + index,
                index % 2 == 0 ? JobStatus.CANCELED : JobStatus.KILLED)).collect(
                Collectors.toList());
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
        return IntStream.range(1, nbTasks + 1).mapToObj(
                index -> createTaskData(jobData, index - 1, "task" + index)).collect(Collectors.toList());
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

    @Test
    public void testInvalidQuery() {
        Map<String, Object> queryResult = executeGraphqlQuery("invalid query");
        assertThat(getField(queryResult, "errors")).isNotNull();
    }

    @Test
    public void testQueryVersion() {
        String query = "{ version }";
        Map<String, Object> queryResult = executeGraphqlQuery(query);

        assertThat(getField(queryResult, "data", "version")).isEqualTo(Constants.VERSION_API);
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

    @Test
    public void testQueryViewerJobs() {
        addJobData(10);

        Map<String, Object> queryResult = executeGraphqlQuery(
                "{ viewer { jobs { edges { node { id owner } } } } }");

        List<?> jobNodes = (List<?>) getField(queryResult, "data", "viewer", "jobs", "edges");
        assertThat(jobNodes).hasSize(5);

        jobNodes.forEach(jobNode -> assertThat(getField(jobNode, "node", "owner")).isEqualTo(CONTEXT_LOGIN));
    }

    private Object getField(Object object, String... fields) {
        for (String field : fields) {
            object = ((Map<String, Object>) object).get(field);
        }

        return object;
    }

    private Map<String, Object> executeGraphqlQuery(String query) {
        return executeGraphqlQuery(query, ImmutableMap.of());
    }

    private Map<String, Object> executeGraphqlQuery(String query, Map<String, Object> variables) {
        return graphqlService.executeQuery(query, null,
                new GraphqlService.GraphqlContext(CONTEXT_SESSION_ID, CONTEXT_LOGIN),
                variables);
    }

}
