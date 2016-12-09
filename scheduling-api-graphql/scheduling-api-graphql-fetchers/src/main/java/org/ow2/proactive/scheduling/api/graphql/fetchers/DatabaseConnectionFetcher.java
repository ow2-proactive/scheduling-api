/*
 * ProActive Parallel Suite(TM):
 * The Java(TM) library for Parallel, Distributed,
 * Multi-Core Computing for Enterprise Grids & Clouds
 *
 * Copyright (c) 2016 ActiveEon
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

import com.google.common.annotations.VisibleForTesting;

import graphql.relay.Connection;
import graphql.relay.ConnectionCursor;
import graphql.relay.Edge;
import graphql.relay.PageInfo;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ow2.proactive.scheduling.api.graphql.fetchers.cursor.CursorMapper;


/**
 * Manage pagination for entities stored in a relational database that have to be mapped to a
 * GraphQL field.
 *
 * @param <E> entity class type
 * @param <T> graphql class type
 * @author ActiveEon Team
 */
public abstract class DatabaseConnectionFetcher<E, T> implements DataFetcher {

    protected Supplier<EntityManager> entityManager;

    public DatabaseConnectionFetcher(Supplier<EntityManager> entityManager) {
        this.entityManager = entityManager;
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
    protected Connection createPaginatedConnection(DataFetchingEnvironment environment, Class<E> entityClass,
            Function<Root<E>, Path<? extends Number>> entityId, Comparator<E> entityComparator,
            BiFunction<CriteriaBuilder, Root<E>, List<Predicate[]>> criteria, CursorMapper<T, Integer> cursorMapper) {

        Integer first = environment.getArgument(FIRST.getName());
        Integer last = environment.getArgument(LAST.getName());

        Integer after = cursorMapper.getOffsetFromCursor(environment.getArgument(AFTER.getName()));
        Integer before = cursorMapper.getOffsetFromCursor(environment.getArgument(BEFORE.getName()));

        EntityManager entityManager = getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> entityRoot = criteriaQuery.from(entityClass);
        Path<? extends Number> entityIdPath = entityId.apply(entityRoot);

        Predicate cursorPredicate = createCursorPredicate(criteriaBuilder, entityIdPath, after, before);

        int maxResults = applySlicing(criteriaQuery, criteriaBuilder, entityIdPath, first, last);

        CriteriaQuery<E> select = criteriaQuery.select(entityRoot);

        List<Predicate[]> predicates = criteria.apply(criteriaBuilder, entityRoot);

        Predicate[] wherePredicate = buildWherePredicate(predicates, cursorPredicate, criteriaBuilder);

        if (wherePredicate.length > 0) {
            select.where(wherePredicate);
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

        Connection connection = createRelayConnection(entityManager,
                                                      entityClass,
                                                      criteriaBuilder,
                                                      wherePredicate,
                                                      cursorMapper,
                                                      data,
                                                      first,
                                                      last);

        return connection;
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

    protected EntityManager getEntityManager() {
        return entityManager.get();
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

    protected Connection createRelayConnection(EntityManager entityManager, Class<E> entityClass,
            CriteriaBuilder criteriaBuilder, Predicate[] predicates, CursorMapper<T, Integer> cursorMapper,
            Stream<T> data, Integer first, Integer last) {

        List<Edge> edges = buildEdges(data, cursorMapper);

        PageInfo pageInfo = new PageInfo();

        if (!edges.isEmpty()) {
            pageInfo.setStartCursor(edges.get(0).getCursor());
            pageInfo.setEndCursor(edges.get(edges.size() - 1).getCursor());
        }

        int nbEntriesBeforeSlicing = getNbEntriesBeforeSlicing(entityManager, entityClass, criteriaBuilder, predicates);

        pageInfo.setHasPreviousPage(hasPreviousPage(nbEntriesBeforeSlicing, last));
        pageInfo.setHasNextPage(hasNextPage(nbEntriesBeforeSlicing, first));

        Connection connection = new Connection();
        connection.setEdges(edges);
        connection.setPageInfo(pageInfo);

        return connection;
    }

    @VisibleForTesting
    int getNbEntriesBeforeSlicing(EntityManager entityManager, Class<E> entityClass, CriteriaBuilder criteriaBuilder,
            Predicate[] predicates) {

        CriteriaQuery<Long> counterQuery = criteriaBuilder.createQuery(Long.class);

        CriteriaQuery<Long> select = counterQuery.select(criteriaBuilder.count(counterQuery.from(entityClass)));

        if (predicates.length > 0) {
            select.where(predicates);
        }

        return entityManager.createQuery(counterQuery).getSingleResult().intValue();
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
