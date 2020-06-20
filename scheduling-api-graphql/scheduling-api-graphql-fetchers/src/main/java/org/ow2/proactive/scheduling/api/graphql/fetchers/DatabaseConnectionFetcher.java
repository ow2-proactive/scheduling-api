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
package org.ow2.proactive.scheduling.api.graphql.fetchers;

import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.AFTER;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.BEFORE;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.FIRST;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.LAST;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ow2.proactive.scheduling.api.graphql.fetchers.connection.ExtendedConnection;
import org.ow2.proactive.scheduling.api.graphql.fetchers.cursors.CursorMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.annotations.VisibleForTesting;

import graphql.relay.ConnectionCursor;
import graphql.relay.Edge;
import graphql.relay.PageInfo;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;


/**
 * Manage pagination for entities stored in a relational database that have to be mapped to a
 * GraphQL field.
 *
 * @param <E> entity class type
 * @param <T> graphql class type
 * @author ActiveEon Team
 */
@Repository
@Transactional(readOnly = true)
public abstract class DatabaseConnectionFetcher<E, T> implements DataFetcher {

    @PersistenceContext
    protected EntityManager entityManager;

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
    protected ExtendedConnection createPaginatedConnection(DataFetchingEnvironment environment, Class<E> entityClass,
            Function<Root<E>, Path<? extends Number>> entityId, Comparator<E> entityComparator,
            BiFunction<CriteriaBuilder, Root<E>, List<Predicate[]>> criteria, CursorMapper<T, Integer> cursorMapper) {

        CriteriaBuilder criteriaBuilderJobs = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQueryJobs = criteriaBuilderJobs.createQuery(entityClass);
        Root<E> entityRootJobs = criteriaQueryJobs.from(entityClass);
        Path<? extends Number> entityIdPathJobs = entityId.apply(entityRootJobs);

        Integer first = environment.getArgument(FIRST.getName());
        Integer last = environment.getArgument(LAST.getName());
        Integer after = cursorMapper.getOffsetFromCursor(environment.getArgument(AFTER.getName()));
        Integer before = cursorMapper.getOffsetFromCursor(environment.getArgument(BEFORE.getName()));

        Predicate cursorPredicateJobs = createCursorPredicate(criteriaBuilderJobs, entityIdPathJobs, after, before);
        int maxResultsJobs = applySlicing(criteriaQueryJobs, criteriaBuilderJobs, entityIdPathJobs, first, last);
        CriteriaQuery<E> selectJobs = criteriaQueryJobs.select(entityRootJobs);

        List<Predicate[]> predicatesJobs = criteria.apply(criteriaBuilderJobs, entityRootJobs);
        Predicate[] wherePredicateJobs = buildWherePredicate(predicatesJobs, cursorPredicateJobs, criteriaBuilderJobs);
        if (wherePredicateJobs.length > 0) {
            selectJobs.where(wherePredicateJobs);
        }

        TypedQuery<E> query = entityManager.createQuery(selectJobs);

        if (maxResultsJobs > -1) {
            query.setMaxResults(maxResultsJobs);
        }

        Stream<E> dataStream = query.getResultList().stream();

        // if last is provided, reverse the stream
        // in order to get results sorted in ascending order based on entities ID
        if (last != null) {
            dataStream = dataStream.sorted(entityComparator);
        }

        Stream<T> data = dataMapping(dataStream);
        return createRelayConnection(entityRootJobs,
                                     entityClass,
                                     criteria,
                                     cursorMapper,
                                     data,
                                     first,
                                     last,
                                     entityId,
                                     after,
                                     before);

    }

    /**
     * build final sql select where predicate
     *
     * @param predicates
     * @param cursorPredicate
     * @param criteriaBuilder
     * @return where predicate array of the sql
     */
    private Predicate[] buildWherePredicate(List<Predicate[]> predicates, Predicate cursorPredicate,
            CriteriaBuilder criteriaBuilder) {

        List<Predicate> concatenatePredicate = new ArrayList<>();

        // custom filter predicates
        if (!predicates.isEmpty()) {
            List<Predicate> andPredicates = predicates.stream()
                                                      .map(array -> criteriaBuilder.and(array))
                                                      .collect(Collectors.toList());

            concatenatePredicate.add(criteriaBuilder.or(andPredicates.toArray(new Predicate[andPredicates.size()])));
        }

        if (cursorPredicate != null) {
            concatenatePredicate.add(cursorPredicate);
        }

        // final where clause predicate list
        List<Predicate> wherePredicate = new ArrayList<>();

        if (concatenatePredicate.size() > 1) {
            wherePredicate.add(criteriaBuilder.and(concatenatePredicate.toArray(new Predicate[concatenatePredicate.size()])));
        } else if (concatenatePredicate.size() == 1) {
            wherePredicate.addAll(concatenatePredicate);
        } else {
            return new Predicate[] {};
        }

        return wherePredicate.toArray(new Predicate[wherePredicate.size()]);
    }

    protected int applySlicing(CriteriaQuery<E> criteriaQuery, CriteriaBuilder criteriaBuilder,
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

    protected Predicate createCursorPredicate(CriteriaBuilder criteriaBuilder, Path<? extends Number> taskIdPath,
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

    protected ExtendedConnection createRelayConnection(Root<E> entityRootJobs, Class<E> entityClass,
            BiFunction<CriteriaBuilder, Root<E>, List<Predicate[]>> criteria, CursorMapper<T, Integer> cursorMapper,
            Stream<T> data, Integer first, Integer last, Function<Root<E>, Path<? extends Number>> entityId,
            Integer after, Integer before) {

        List<Edge> edges = buildEdges(data, cursorMapper);

        PageInfo pageInfo = new PageInfo();

        if (!edges.isEmpty()) {
            pageInfo.setStartCursor(edges.get(0).getCursor());
            pageInfo.setEndCursor(edges.get(edges.size() - 1).getCursor());
        }

        int nbEntriesBeforeSlicing = getNbEntriesBeforeSlicing(entityRootJobs,
                                                               entityClass,
                                                               criteria,
                                                               entityId,
                                                               after,
                                                               before);

        pageInfo.setHasPreviousPage(hasPreviousPage(nbEntriesBeforeSlicing, last, after));
        pageInfo.setHasNextPage(hasNextPage(nbEntriesBeforeSlicing, first, before));

        ExtendedConnection connection = new ExtendedConnection();
        connection.setEdges(edges);
        connection.setPageInfo(pageInfo);
        connection.setTotalCount(nbEntriesBeforeSlicing);

        return connection;
    }

    @VisibleForTesting
    int getNbEntriesBeforeSlicing(Root<E> entityRootJobs, Class<E> entityClass,
            BiFunction<CriteriaBuilder, Root<E>, List<Predicate[]>> criteria,
            Function<Root<E>, Path<? extends Number>> entityId, Integer after, Integer before) {

        CriteriaBuilder criteriaBuilderCount = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQueryCount = criteriaBuilderCount.createQuery(entityClass);
        CriteriaQuery<Long> queryCount = criteriaBuilderCount.createQuery(Long.class);
        Root<E> entityRootCount = queryCount.from(criteriaQueryCount.getResultType());
        // Apply countDistinct when the query operates on join tables
        CriteriaQuery<Long> longCriteriaQuery;
        if (entityRootJobs.getJoins().size() > 0) {
            longCriteriaQuery = queryCount.select(criteriaBuilderCount.countDistinct(entityRootCount));
        } else {
            longCriteriaQuery = queryCount.select(criteriaBuilderCount.count(entityRootCount));
        }

        Path<? extends Number> entityIdPath = entityId.apply(entityRootCount);

        Predicate cursorPredicate = createCursorPredicate(criteriaBuilderCount, entityIdPath, after, before);

        List<Predicate[]> predicatesCount = criteria.apply(criteriaBuilderCount, entityRootCount);
        Predicate[] wherePredicateCount = buildWherePredicate(predicatesCount, cursorPredicate, criteriaBuilderCount);

        if (wherePredicateCount.length > 0) {
            longCriteriaQuery.where(wherePredicateCount);
        }

        return entityManager.createQuery(longCriteriaQuery).getSingleResult().intValue();

    }

    @VisibleForTesting
    List<Edge> buildEdges(Stream<T> data, CursorMapper<T, Integer> cursorMapper) {
        return data.map(entry -> new Edge(entry, new ConnectionCursor(cursorMapper.createCursor(entry))))
                   .collect(Collectors.toList());
    }

    /**
     * See https://facebook.github.io/relay/graphql/connections.htm#HasPreviousPage()
     *
     * @param nbEntriesBeforeSlicing The number of entries before slicing.
     * @param last                   the number of last entries requested.
     * @param after                  a cursor set (or not)
     * @return {@code true} if entries have been sliced and another page is available, {@code false}
     * otherwise.
     */
    protected boolean hasPreviousPage(int nbEntriesBeforeSlicing, Integer last, Integer after) {

        if (last == null && after == null) {
            return false;
        }

        if (after != null) {
            return true;
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
     * @param before                 a cursor set (or not)
     * @return {@code true} if entries have been sliced and another page is available, {@code false}
     * otherwise.
     */
    protected boolean hasNextPage(int nbEntriesBeforeSlicing, Integer first, Integer before) {

        if (first == null && before == null) {
            return false;
        }

        if (before != null) {
            return true;
        }

        if (nbEntriesBeforeSlicing > first) {
            return true;
        }

        return false;
    }

}
