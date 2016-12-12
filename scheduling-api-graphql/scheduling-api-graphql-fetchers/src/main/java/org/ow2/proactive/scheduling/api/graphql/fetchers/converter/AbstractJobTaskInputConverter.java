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
package org.ow2.proactive.scheduling.api.graphql.fetchers.converter;

import com.google.common.collect.ImmutableList;

import graphql.schema.DataFetchingEnvironment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ow2.proactive.scheduling.api.graphql.common.Arguments;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.JobTaskCommonAbstractInput;


/**
 * @param <T> {@code JobData} or {@code TaskData} scheduler Job, Task entity object
 * @param <I> {@code JobInput} or {@code TaskInput} graphql input type
 * @author ActiveEon team
 */
public abstract class AbstractJobTaskInputConverter<T, I extends JobTaskCommonAbstractInput>
        implements JobTaskInputPredicatesConverter<T, I> {

    protected LinkedHashMap<String, Object> extraInputCheck(DataFetchingEnvironment environment) {
        return new LinkedHashMap<>();
    }

    protected abstract Function<Map<String, Object>, I> mapFunction();

    @Override
    public List<I> mapToInput(DataFetchingEnvironment environment) {
        List<I> input = new ArrayList<>();

        LinkedHashMap<String, Object> extraInput = extraInputCheck(environment);

        Object filterArgument = environment.getArgument(Arguments.FILTER.getName());
        List<LinkedHashMap<String, Object>> filterInputs;

        if (filterArgument == null) {
            if (!extraInput.isEmpty()) {
                filterInputs = ImmutableList.of(extraInput);
                input.addAll(filterInputs.stream().map(mapFunction()).collect(Collectors.toList()));
            }
        } else {
            filterInputs = (List<LinkedHashMap<String, Object>>) filterArgument;
            if (extraInput.isEmpty()) {
                input.addAll(filterInputs.stream().map(mapFunction()).collect(Collectors.toList()));
            } else {
                input.addAll(filterInputs.stream().map(arg -> {
                    arg.putAll(extraInput);
                    return arg;
                }).map(mapFunction()).collect(Collectors.toList()));
            }
        }

        return input;
    }

    public static class WildCardInputPredicateBuilder {
        private static final String START_WITH = ".*\\*$";

        private static final String END_WITH = "^\\*.*";

        private static final String CONTAINS = "^\\*.*\\*$";

        public static <T> Predicate build(CriteriaBuilder criteriaBuilder, Root<T> root, String entityFieldName,
                String fieldInputValue) {
            if (Pattern.matches(START_WITH, fieldInputValue)) {
                return criteriaBuilder.like(root.get(entityFieldName), fieldInputValue.replace("*", "%"));
            } else if (Pattern.matches(END_WITH, fieldInputValue)) {
                return criteriaBuilder.like(root.get(entityFieldName), fieldInputValue.replace("*", "%"));
            } else if (Pattern.matches(CONTAINS, fieldInputValue)) {
                return criteriaBuilder.like(root.get(entityFieldName), fieldInputValue.replace("*", "%"));
            }

            return criteriaBuilder.equal(root.get(entityFieldName), fieldInputValue);

        }
    }

}
