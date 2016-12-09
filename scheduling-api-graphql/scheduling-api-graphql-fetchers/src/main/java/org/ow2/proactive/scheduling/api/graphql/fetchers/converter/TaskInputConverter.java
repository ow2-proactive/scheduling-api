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

import com.google.common.base.Strings;

import graphql.schema.DataFetchingEnvironment;

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


/**
 * @author ActiveEon Team
 */
public class TaskInputConverter extends AbstractJobTaskInputConverter<TaskData, TaskInput> {

    @Override
    public List<Predicate[]> inputToPredicates(DataFetchingEnvironment environment, CriteriaBuilder criteriaBuilder,
            Root<TaskData> root, List<TaskInput> input) {
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
                predicates.add(criteriaBuilder.equal(root.get("taskStatus"), TaskStatus.valueOf(status)));
            }
            if (!Strings.isNullOrEmpty(taskName)) {
                predicates.add(criteriaBuilder.like(root.get("taskName"), "%" + taskName + "%"));
            }

            return predicates.toArray(new Predicate[predicates.size()]);

        }).filter(array -> array.length > 1).collect(Collectors.toList());

        if (filters.isEmpty()) {
            filters = Collections.singletonList(new Predicate[] { criteriaBuilder.equal(root.get("id").get("jobId"),
                                                                                        job.getId()) });
        }

        return filters;

    }

    @Override
    protected Function<Map<String, Object>, TaskInput> mapFunction() {
        return TaskInput::new;
    }

}
