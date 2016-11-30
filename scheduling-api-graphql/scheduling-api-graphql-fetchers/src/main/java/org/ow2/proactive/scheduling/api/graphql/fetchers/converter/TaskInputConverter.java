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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ow2.proactive.scheduler.common.task.TaskStatus;
import org.ow2.proactive.scheduler.core.db.TaskData;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Job;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.TaskInput;
import com.google.common.base.Strings;
import graphql.schema.DataFetchingEnvironment;

/**
 * @author ActiveEon Team
 */
public class TaskInputConverter extends AbstractJobTaskInputConverter<TaskData, TaskInput> {

    @Override
    public List<Predicate[]> inputToPredicates(DataFetchingEnvironment environment,
            CriteriaBuilder criteriaBuilder, Root<TaskData> root, List<TaskInput> input) {
        Job job = (Job) environment.getSource();

        List<Predicate[]> filters = input.stream().map(i -> {
            List<Predicate> predicates = new ArrayList<>();

            long taskId = i.getId();
            String status = i.getStatus();
            String taskName = i.getTaskName();

            predicates.add(criteriaBuilder.equal(root.get("id").get("jobId"), job.getId()));

            if (taskId != -1L) {
                predicates.add(criteriaBuilder.equal(root.get("id").get("taskId"), taskId));
            }
            if (!Strings.isNullOrEmpty(status)) {
                predicates.add(
                        criteriaBuilder.equal(root.get("taskStatus"), TaskStatus.valueOf(status)));
            }
            if (!Strings.isNullOrEmpty(taskName)) {
                predicates.add(criteriaBuilder.like(root.get("taskName"), "%" + taskName + "%"));
            }

            return predicates.toArray(new Predicate[predicates.size()]);

        }).filter(array -> array.length > 1).collect(Collectors.toList());

        if (filters.isEmpty()) {
            filters = Collections.singletonList(
                    new Predicate[] { criteriaBuilder.equal(root.get("id").get("jobId"), job.getId()) });
        }

        return filters;

    }

    @Override
    protected Function<Map<String, Object>, TaskInput> mapFunction() {
        return TaskInput::new;
    }

}
