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
package org.ow2.proactive.scheduling.api.graphql.fetchers.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ow2.proactive.authentication.UserData;
import org.ow2.proactive.scheduler.common.task.TaskStatus;
import org.ow2.proactive.scheduler.core.db.TaskData;
import org.ow2.proactive.scheduling.api.graphql.common.GraphqlContext;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Job;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.TaskInput;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.TaskStatusInput;

import com.google.common.base.Strings;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.log4j.Log4j2;


/**
 * @author ActiveEon Team
 */
@Log4j2
public class TaskInputConverter extends AbstractJobTaskInputConverter<TaskData, TaskInput> {

    public TaskInputConverter(EntityManager manager) {
        super(manager);
    }

    @Override
    public List<Predicate[]> inputToPredicates(DataFetchingEnvironment environment, CriteriaBuilder criteriaBuilder,
            Root<TaskData> root, List<TaskInput> input) {
        Job job = (Job) environment.getSource();
        GraphqlContext graphqlContext = (GraphqlContext) environment.getContext();
        UserData userData = graphqlContext.getUserData();

        List<Predicate[]> filters = input.stream().map(i -> {
            List<Predicate> predicates = new ArrayList<>();

            long taskId = i.getId();
            TaskStatusInput status = i.getTaskStatus();
            String taskName = i.getTaskName();

            predicates.add(criteriaBuilder.equal(root.get("id").get("jobId"), job.getId()));

            if (userData.isHandleOnlyMyJobsPermission()) {
                predicates.add(criteriaBuilder.equal(root.get("owner"), userData.getUserName()));
            }

            if (taskId != -1L) {
                predicates.add(criteriaBuilder.equal(root.get("id").get("taskId"), taskId));
            }
            if (status != null && status.getStatus() != null && status.getStatus().size() > 0) {
                List<TaskStatus> taskStatuses = status.getStatus()
                                                      .stream()
                                                      .map(TaskStatus::valueOf)
                                                      .collect(Collectors.toList());
                predicates.add(root.get("taskStatus").in(taskStatuses));
            }
            if (!Strings.isNullOrEmpty(taskName)) {
                Predicate predicate = WildCardInputPredicateBuilder.build(criteriaBuilder, root, "taskName", taskName);
                predicates.add(predicate);
            }

            return predicates.toArray(new Predicate[predicates.size()]);

        }).filter(array -> array.length > 1).collect(Collectors.toList());

        if (filters.isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            if (userData.isHandleOnlyMyJobsPermission()) {
                predicates.add(criteriaBuilder.equal(root.get("owner"), userData.getUserName()));
            }
            predicates.add(criteriaBuilder.equal(root.get("id").get("jobId"), job.getId()));
            filters = Collections.singletonList(predicates.toArray(new Predicate[predicates.size()]));
        }

        return filters;

    }

    private TaskStatus stringToTaskStatus(String stringTaskStatus) {
        TaskStatus taskStatus = null;
        switch (stringTaskStatus) {
            case "SUBMITTED":
                taskStatus = TaskStatus.SUBMITTED;
                break;
            case "PENDING":
                taskStatus = TaskStatus.PENDING;
                break;
            case "PAUSED":
                taskStatus = TaskStatus.PAUSED;
                break;
            case "RUNNING":
                taskStatus = TaskStatus.RUNNING;
                break;
            case "WAITING_ON_ERROR":
                taskStatus = TaskStatus.WAITING_ON_ERROR;
                break;
            case "WAITING_ON_FAILURE":
                taskStatus = TaskStatus.WAITING_ON_FAILURE;
                break;
            case "FAILED":
                taskStatus = TaskStatus.FAILED;
                break;
            case "NOT_STARTED":
                taskStatus = TaskStatus.NOT_STARTED;
                break;
            case "NOT_RESTARTED":
                taskStatus = TaskStatus.NOT_RESTARTED;
                break;
            case "ABORTED":
                taskStatus = TaskStatus.ABORTED;
                break;
            case "FAULTY":
                taskStatus = TaskStatus.FAULTY;
                break;
            case "FINISHED":
                taskStatus = TaskStatus.FINISHED;
                break;
            case "SKIPPED":
                taskStatus = TaskStatus.SKIPPED;
                break;
            case "IN_ERROR":
                taskStatus = TaskStatus.IN_ERROR;
                break;
            default:
                log.error("Unkown task status: " + stringTaskStatus);
        }
        return taskStatus;
    }

    @Override
    protected Function<Map<String, Object>, TaskInput> mapFunction() {
        return TaskInput::new;
    }

}
