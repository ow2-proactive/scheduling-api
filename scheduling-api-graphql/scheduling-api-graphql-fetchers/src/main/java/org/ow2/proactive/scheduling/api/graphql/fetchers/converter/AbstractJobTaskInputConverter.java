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
package org.ow2.proactive.scheduling.api.graphql.fetchers.converter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.ow2.proactive.scheduling.api.graphql.common.Arguments;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.JobTaskCommonAbstractInput;
import graphql.schema.DataFetchingEnvironment;

/**
 * @param <T> {@code JobData} or {@code TaskData} scheduler Job, Task entity object
 * @param <I> {@code JobInput} or {@code TaskInput} graphql input type
 * @author ActiveEon team
 */
public abstract class AbstractJobTaskInputConverter<T, I extends JobTaskCommonAbstractInput> implements JobTaskInputPredicatesConverter<T, I> {

    protected void extraInputCheck(DataFetchingEnvironment environment, List<I> input) {
        // nothing to do here by default
    }

    protected abstract Function<Map<String, Object>, I> mapFunction();

    @Override
    public List<I> mapToInput(DataFetchingEnvironment environment) {
        List<I> input = new ArrayList<>();

        extraInputCheck(environment, input);

        Object filterArgument = environment.getArgument(Arguments.FILTER.getName());

        if (filterArgument != null) {
            List<LinkedHashMap<String, Object>> args = (List<LinkedHashMap<String, Object>>) filterArgument;
            input.addAll(args.stream().map(mapFunction()).collect(Collectors.toList()));
        }
        return input;
    }


}
