/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2016 INRIA/University of
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

import com.google.common.annotations.VisibleForTesting;

import org.ow2.proactive.scheduling.api.fetchers.cursor.CursorMapper;
import org.ow2.proactive.scheduling.api.service.ApplicationContextProvider;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
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
import graphql.relay.Connection;
import graphql.relay.ConnectionCursor;
import graphql.relay.Edge;
import graphql.relay.PageInfo;
import graphql.schema.DataFetchingEnvironment;

/**
 * Manage pagination for entities stored in a relational database that have to be mapped to a
 * GraphQL field.
 *
 * @param <E> entity class type
 * @param <T> graphql class type
 */
public abstract class DatabaseConnectionFetcher<E, T> extends DispatchingConnection {

    /*
     * Arguments for forward pagination.
     */
    protected static final String RELAY_ARGUMENT_AFTER = "after";

    protected static final String RELAY_ARGUMENT_FIRST = "first";

    /*
     * Arguments for backward pagination.
     */
    protected static final String RELAY_ARGUMENT_BEFORE = "before";

    protected static final String RELAY_ARGUMENT_LAST = "last";

    protected DatabaseConnectionFetcher(Object o) {
        super(o);
    }

    /**
     * Maps entity objects to GraphQL schema objects.
     *
     * @param input a stream of entity objects.
     * @return a stream of GraphQL schema objects.
     */
    protected abstract Stream<T> dataMapping(Stream<E> input);

    /**
     * Adaptation of the algorithm defined in the GraphQL specification.
     * <p>
     * Please look at the following link for more details:
     * https://facebook.github.io/relay/graphql/connections.htm#sec-Pagination-algorithm
     * <p>
     * The opaque cursor that is returned to the client makes use of the entity ID internally.
     */
    protected Connection createPaginatedConnection(
            DataFetchingEnvironment environment, Class<E> entityClass,
            Function<Root<E>, Path<? extends Number>> entityId,
            Comparator<E> entityComparator,
            BiFunction<CriteriaBuilder, Root<E>, Predicate[]> criteria,
            CursorMapper<T, Integer> cursorMapper) {

        Integer first = environment.getArgument(RELAY_ARGUMENT_FIRST);
        Integer last = environment.getArgument(RELAY_ARGUMENT_LAST);

        Integer after =
                cursorMapper.getOffsetFromCursor(environment.getArgument(RELAY_ARGUMENT_AFTER));
        Integer before =
                cursorMapper.getOffsetFromCursor(environment.getArgument(RELAY_ARGUMENT_BEFORE));

        EntityManager entityManager = getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> entityRoot = criteriaQuery.from(entityClass);
        Path<? extends Number> entityIdPath = entityId.apply(entityRoot);

        Predicate cursorPredicate =
                createCursorPredicate(criteriaBuilder, entityIdPath, after, before);

        int maxResults = applySlicing(criteriaQuery, criteriaBuilder, entityIdPath, first, last);

        Predicate[] predicates = criteria.apply(criteriaBuilder, entityRoot);

        if (cursorPredicate != null) {
            predicates = concatenatePredicates(predicates, cursorPredicate);
        }

        CriteriaQuery<E> select = criteriaQuery.select(entityRoot);

        if (predicates.length > 0) {
            select.where(criteriaBuilder.and(predicates));
        }

        TypedQuery<E> query = entityManager.createQuery(select);

        if (maxResults > -1) {
            query.setMaxResults(maxResults);
        }

        Stream<E> dataStream = query.getResultList().stream();

        // if last is provided, reverse the stream
        // in order to get results sorted in ascending order based on entities ID
        if (last != null) {
            dataStream = dataStream.sorted(entityComparator);
        }

        Stream<T> data = dataMapping(dataStream);

        Connection connection =
                createRelayConnection(
                        entityManager, entityClass, criteriaBuilder,
                        predicates, cursorMapper, data, first, last);

        return connection;
    }

    protected EntityManager getEntityManager() {
        return ApplicationContextProvider.getApplicationContext().getBean(EntityManager.class);
    }

    protected int applySlicing(CriteriaQuery<E> criteriaQuery,
                               CriteriaBuilder criteriaBuilder,
                               Path<? extends Number> taskIdPath, Integer first, Integer last) {
        // apply slicing
        int maxResults = -1;

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
        return maxResults;
    }

    @VisibleForTesting
    Predicate[] concatenatePredicates(Predicate[] predicates, Predicate cursorPredicate) {
        Predicate[] tmp = new Predicate[predicates.length + 1];
        System.arraycopy(predicates, 0, tmp, 0, predicates.length);
        tmp[tmp.length - 1] = cursorPredicate;
        predicates = tmp;
        return predicates;
    }

    protected Predicate createCursorPredicate(CriteriaBuilder criteriaBuilder,
                                              Path<? extends Number> taskIdPath,
                                              Integer after, Integer before) {
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

        return cursorPredicate;
    }

    protected Connection createRelayConnection(
            EntityManager entityManager,
            Class<E> entityClass,
            CriteriaBuilder criteriaBuilder,
            Predicate[] predicates,
            CursorMapper<T, Integer> cursorMapper,
            Stream<T> data, Integer first, Integer last) {

        List<Edge> edges = buildEdges(data, cursorMapper);

        PageInfo pageInfo = new PageInfo();

        if (!edges.isEmpty()) {
            pageInfo.setStartCursor(edges.get(0).getCursor());
            pageInfo.setEndCursor(edges.get(edges.size() - 1).getCursor());
        }

        int nbEntriesBeforeSlicing =
                getNbEntriesBeforeSlicing(entityManager, entityClass, criteriaBuilder, predicates);

        pageInfo.setHasPreviousPage(hasPreviousPage(nbEntriesBeforeSlicing, last));
        pageInfo.setHasNextPage(hasNextPage(nbEntriesBeforeSlicing, first));

        Connection connection = new Connection();
        connection.setEdges(edges);
        connection.setPageInfo(pageInfo);

        return connection;
    }

    @VisibleForTesting
    int getNbEntriesBeforeSlicing(EntityManager entityManager,
                                  Class<E> entityClass,
                                  CriteriaBuilder criteriaBuilder,
                                  Predicate[] predicates) {

        CriteriaQuery<Long> counterQuery = criteriaBuilder.createQuery(Long.class);

        CriteriaQuery<Long> select =
                counterQuery.select(criteriaBuilder.count(counterQuery.from(entityClass)));

        if (predicates.length > 0) {
            select.where(predicates);
        }

        return entityManager.createQuery(counterQuery).getSingleResult().intValue();
    }

    @VisibleForTesting
    List<Edge> buildEdges(Stream<T> data, CursorMapper<T, Integer> cursorMapper) {
        return data.map(entry ->
                new Edge(entry, new ConnectionCursor(cursorMapper.createCursor(entry))))
                .collect(Collectors.toList());
    }

    /**
     * See https://facebook.github.io/relay/graphql/connections.htm#HasPreviousPage()
     *
     * @param nbEntriesBeforeSlicing The number of entries before slicing.
     * @param last                   the number of last entries requested.
     * @return {@code true} if entries have been sliced and another page is available, {@code false}
     * otherwise.
     */
    protected boolean hasPreviousPage(int nbEntriesBeforeSlicing, Integer last) {

        if (last == null) {
            return false;
        }

        if (nbEntriesBeforeSlicing > last) {
            return true;
        }

        return false;
    }

    /**
     * See https://facebook.github.io/relay/graphql/connections.htm#HasNextPage()
     *
     * @param nbEntriesBeforeSlicing The number of entries before slicing.
     * @param first                  the number of first entries requested.
     * @return {@code true} if entries have been sliced and another page is available, {@code false}
     * otherwise.
     */
    protected boolean hasNextPage(int nbEntriesBeforeSlicing, Integer first) {

        if (first == null) {
            return false;
        }

        if (nbEntriesBeforeSlicing > first) {
            return true;
        }

        return false;
    }

}
