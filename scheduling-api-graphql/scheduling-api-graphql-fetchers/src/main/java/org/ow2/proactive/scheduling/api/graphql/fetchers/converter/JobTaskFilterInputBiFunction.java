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

import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.function.BiFunction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.AllArgsConstructor;

import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.JobTaskCommonAbstractInput;


/**
 * @author ActiveEon Team
 */
@AllArgsConstructor
public class JobTaskFilterInputBiFunction<T, I extends JobTaskCommonAbstractInput>
        implements BiFunction<CriteriaBuilder, Root<T>, List<Predicate[]>> {

    private JobTaskInputPredicatesConverter<T, I> converter;

    protected DataFetchingEnvironment environment;

    @Override
    public List<Predicate[]> apply(CriteriaBuilder criteriaBuilder, Root<T> root) {

        List<I> input = converter.mapToInput(environment);

        return converter.inputToPredicates(environment, criteriaBuilder, root, input);
    }

}
