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
package org.ow2.proactive.scheduling.api.fetchers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import org.ow2.proactive.scheduler.core.db.TaskData;
import org.ow2.proactive.scheduling.api.schema.type.Job;
import org.ow2.proactive.scheduling.api.schema.type.Task;
import org.ow2.proactive.scheduling.api.schema.type.enums.TaskStatus;
import org.ow2.proactive.scheduling.api.service.ApplicationContextProvider;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import graphql.annotations.DispatchingConnection;
import graphql.relay.Base64;
import graphql.relay.Connection;
import graphql.relay.ConnectionCursor;
import graphql.relay.Edge;
import graphql.relay.PageInfo;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;


public class TaskDataFetcher extends DispatchingConnection {

    private static final String DUMMY_CURSOR_PREFIX = "graphql-cursor";

    public TaskDataFetcher(DataFetcher dataFetcher) {
        super(ImmutableList.of());
    }

    // TODO(lpellegr) generalize for Jobs

    // Adaptation of the algorithm available at:
    // https://facebook.github.io/relay/graphql/connections.htm#sec-Pagination-algorithm
    // The opaque cursor that is returned to the client makes use of the task ID internally
    @Override
    public Object get(DataFetchingEnvironment environment) {

        Job job = (Job) environment.getSource();

        Integer first = environment.getArgument("first");
        Integer last = environment.getArgument("last");

        Integer after = getOffsetFromCursor(environment.getArgument("after"));
        Integer before = getOffsetFromCursor(environment.getArgument("before"));

        int maxResults = -1;

        EntityManager entityManager = ApplicationContextProvider.getApplicationContext()
                .getBean(EntityManager.class);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskData> criteriaQuery = criteriaBuilder.createQuery(TaskData.class);
        Root<TaskData> root = criteriaQuery.from(TaskData.class);

        Path<? extends Number> taskIdPath = root.get("id").get("taskId");

        // apply cursors to tasks
        Predicate cursorPredicate = null;

        // after is set
        if (after != null) {
            // remove all elements of tasks before and including afterTask, where
            // afterTask is the task whose cursor is equal to the after argument
            cursorPredicate = criteriaBuilder.gt(taskIdPath, after);
        }

        // before is set
        if (before != null) {
            // remove all elements of tasks after and including beforeTask, where
            // beforeTask is the task whose cursor is equal to the before argument
            cursorPredicate = criteriaBuilder.lt(taskIdPath, before);
        }

        // apply slicing

        // first is set
        if (first != null) {
            if (first < 0) {
                throw new IllegalArgumentException("Argument 'first' must be equal or greater than 0");
            }

            criteriaQuery.orderBy(criteriaBuilder.asc(taskIdPath));
            maxResults = first;
        }

        // last is set
        if (last != null) {
            if (last < 0) {
                throw new IllegalArgumentException("Argument 'last' must be equal or greater than 0");
            }

            criteriaQuery.orderBy(criteriaBuilder.desc(taskIdPath));
            maxResults = last;
        }

        if (first == null && last == null) {
            criteriaQuery.orderBy(criteriaBuilder.asc(taskIdPath));
        }

        Predicate filterByJobId = criteriaBuilder.equal(root.get("id").get("jobId"), job.getId());
        Predicate[] predicates;

        if (cursorPredicate != null) {
            predicates = new Predicate[]{filterByJobId, cursorPredicate};
        } else {
            predicates = new Predicate[]{filterByJobId};
        }

        Predicate whereExpression = criteriaBuilder.and(predicates);
        TypedQuery<TaskData> query = entityManager
                .createQuery(criteriaQuery.select(root).where(whereExpression));

        if (maxResults > -1) {
            query.setMaxResults(maxResults);
        }

        List<TaskData> filteredTaskData = query.getResultList();

        Stream<TaskData> taskStream = filteredTaskData.stream();

        // if last is provided, reverse the results
        // in order to get results always sort in ascending order based on their Task ID
        if (last != null) {
            taskStream =
                    taskStream.sorted(
                            (t1, t2) ->
                                    Long.compare(t1.getId().getTaskId(), t2.getId().getTaskId()));
        }

        Stream<Task> tasks = dataMapping(taskStream);

        Connection connection =
                createRelayConnection(
                        first, last, entityManager, criteriaBuilder, predicates, tasks);

        return connection;
    }

    private Connection createRelayConnection(Integer first, Integer last, EntityManager entityManager,
                                             CriteriaBuilder criteriaBuilder, Predicate[] predicates, Stream<Task> tasks) {

        List<Edge> edges = buildEdges(tasks);

        PageInfo pageInfo = new PageInfo();

        if (!edges.isEmpty()) {
            pageInfo.setStartCursor(edges.get(0).getCursor());
            pageInfo.setEndCursor(edges.get(edges.size() - 1).getCursor());
        }

        int nbEntriesBeforeSlicing = getNbEntriesBeforeSlicing(entityManager, criteriaBuilder, predicates);

        pageInfo.setHasPreviousPage(hasPreviousPage(nbEntriesBeforeSlicing, last));
        pageInfo.setHasNextPage(hasNextPage(nbEntriesBeforeSlicing, first));

        Connection connection = new Connection();
        connection.setEdges(edges);
        connection.setPageInfo(pageInfo);

        return connection;
    }

    private int getNbEntriesBeforeSlicing(EntityManager entityManager, CriteriaBuilder criteriaBuilder,
                                          Predicate[] predicates) {
        CriteriaQuery<Long> counterQuery = criteriaBuilder.createQuery(Long.class);
        counterQuery.select(criteriaBuilder.count(counterQuery.from(TaskData.class))).where(predicates);
        return entityManager.createQuery(counterQuery).getSingleResult().intValue();
    }

    private Stream<Task> dataMapping(Stream<TaskData> taskStream) {
        // TODO Task progress not accessible from DB, needs to establish connection with SchedulerFrontEnd
        // Active Object to get the value that is in Scheduler memory
        // TODO Variables for tasks
        return taskStream.map(taskData -> Task.builder()
                .id(taskData.getId().getTaskId())
                .description(taskData.getDescription())
                .executionDuration(taskData.getExecutionDuration())
                .executionHostName(taskData.getExecutionHostName())
                .finishedTime(taskData.getFinishedTime())
                .genericInformation(taskData.getGenericInformation())
                .inErrorTime(taskData.getInErrorTime())
                .jobId(taskData.getId().getJobId())
                .name(taskData.getTaskName())
                .numberOfExecutionLeft(taskData.getNumberOfExecutionLeft())
                .numberOfExecutionOnFailureLeft(taskData.getNumberOfExecutionOnFailureLeft())
                .scheduledTime(taskData.getScheduledTime())
                .startTime(taskData.getStartTime()).progress(-1)
                .status(TaskStatus.valueOf(taskData.getTaskStatus().name()))
                .tag(taskData.getTag())
                .variables(Maps.newHashMap())
                .build());
    }

    /*
     * See https://facebook.github.io/relay/graphql/connections.htm#sec-undefined.PageInfo.Fields
     */
    private boolean hasPreviousPage(int nbTasksBeforeSlicing, Integer last) {

        if (last == null) {
            return false;
        }

        if (nbTasksBeforeSlicing > last) {
            return true;
        }

        return false;
    }

    private boolean hasNextPage(int nbTasksBeforeSlicing, Integer first) {

        if (first == null) {
            return false;
        }

        if (nbTasksBeforeSlicing > first) {
            return true;
        }

        return false;
    }

    private List<Edge> buildEdges(Stream<Task> tasks) {

        return tasks.map(task ->
                new Edge(task, new ConnectionCursor(createCursor((int) task.getId()))))
                .collect(Collectors.toList());
    }

    private Integer getOffsetFromCursor(String cursor) {
        if (cursor == null) {
            return null;
        }

        String string = Base64.fromBase64(cursor);
        return Integer.parseInt(string.substring(DUMMY_CURSOR_PREFIX.length()));
    }

    private String createCursor(int offset) {
        return Base64.toBase64(DUMMY_CURSOR_PREFIX + Integer.toString(offset));
    }

}
