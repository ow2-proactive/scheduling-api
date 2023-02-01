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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ow2.proactive.authentication.UserData;
import org.ow2.proactive.scheduler.common.job.JobPriority;
import org.ow2.proactive.scheduler.common.job.JobStatus;
import org.ow2.proactive.scheduler.core.db.JobData;
import org.ow2.proactive.scheduler.core.db.JobDataVariable;
import org.ow2.proactive.scheduling.api.graphql.common.GraphqlContext;
import org.ow2.proactive.scheduling.api.graphql.common.InputFields;
import org.ow2.proactive.scheduling.api.graphql.common.Types;
import org.ow2.proactive.scheduling.api.graphql.schema.type.User;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.ComparableIntegerInput;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.ComparableLongInput;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.JobInput;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.JobStatusInput;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.VariablesInput;

import com.google.common.base.Strings;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLType;


/**
 * @author ActiveEon Team
 */
public class JobInputConverter extends AbstractJobTaskInputConverter<JobData, JobInput> {

    public JobInputConverter(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected LinkedHashMap<String, Object> extraInputCheck(DataFetchingEnvironment environment) {
        // use the {@code user} parent field to create a new input filter on jobs connection,
        // so that the job list contains jobs belonging to the user only
        GraphQLType parentType = environment.getParentType();
        LinkedHashMap<String, Object> ownerInput = new LinkedHashMap<>();

        if (parentType != null && Types.USER.getName().equals(parentType.getName())) {
            User user = (User) environment.getSource();

            if (!Strings.isNullOrEmpty(user.getLogin())) {
                ownerInput.put(InputFields.OWNER.getName(), user.getLogin());
            } else {
                throw new IllegalStateException("Missing login name");
            }
        }
        return ownerInput;
    }

    @Override
    public List<Predicate[]> inputToPredicates(DataFetchingEnvironment environment, CriteriaBuilder criteriaBuilder,
            Root<JobData> root, List<JobInput> input) {

        return input.stream().map(i -> {
            List<Predicate> predicates = new ArrayList<>();

            // remove_time support has been discontinued, thus the following setting is ignored
            // boolean excludeRemoved = i.isExcludeRemoved();
            long jobId = i.getId();
            String jobName = i.getJobName();
            String owner = i.getOwner();
            String tenant = i.getTenant();
            String priority = i.getPriority();
            String projectName = i.getProjectName();
            JobStatusInput status = i.getJobStatus();
            VariablesInput variablesInput = i.getWithVariables();

            if (variablesInput != null) {
                variablesInput.getVariables().forEach(variable -> {
                    MapJoin<JobData, String, JobDataVariable> joinMap = root.joinMap("variables", JoinType.LEFT);
                    if (variable.containsKey("key")) {
                        predicates.add(criteriaBuilder.like(joinMap.get("name"), variable.get("key")));
                    }
                    if (variable.containsKey("value")) {
                        if (getDialect().toString().contains("PostgreSQL")) {
                            addVariableValuePredicateForPostgreSQL(criteriaBuilder, predicates, variable, joinMap);
                        } else {
                            predicates.add(criteriaBuilder.like(joinMap.get("value"), variable.get("value")));
                        }
                    }
                });
            }

            // This line makes sure that the first where clause contains the job id.
            // As the job id is used in the ORDER BY clause, we hint HSQLDB to use the job id as index.
            // Without this, HSQLDB would use the index of the removedTime column which does not improve the performance of ORDER BY
            // Index usage in queries is DB-dependant, and this "hack" applies only to HSQLDB
            predicates.add(criteriaBuilder.greaterThan(root.get("id"), 0L));

            // remove_time support has been discontinued, thus the following setting is ignored
            // if (excludeRemoved) {
            //    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("removedTime"), 0L));
            // }
            // Job ID based predicates
            if (jobId != -1L) {
                predicates.add(criteriaBuilder.equal(root.get("id"), jobId));
            }
            comparableLongPredicated(i.getComparableId(), "id", root, criteriaBuilder, predicates, false);

            // Job name based predicate
            if (!Strings.isNullOrEmpty(jobName)) {
                Predicate jobNamePredicate = WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                 root,
                                                                                 "jobName",
                                                                                 jobName);
                predicates.add(jobNamePredicate);
            }

            // Job owner based predicate
            if (!Strings.isNullOrEmpty(owner)) {
                Predicate ownerPredicate = WildCardInputPredicateBuilder.build(criteriaBuilder, root, "owner", owner);
                predicates.add(ownerPredicate);
            }

            // Tenant predicate, can be either set explicitly or defined automatically in the scheduler configuration
            GraphqlContext graphqlContext = (GraphqlContext) environment.getContext();
            UserData userData = graphqlContext.getUserData();
            boolean isExplicitTenantFilter = !Strings.isNullOrEmpty(tenant);

            if (userData.isFilterByTenant() && !userData.isAllTenantPermission()) {
                tenant = userData.getTenant();
            }

            if (isExplicitTenantFilter) {
                Predicate tenantPredicate = WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                root,
                                                                                "tenant",
                                                                                tenant);
                predicates.add(tenantPredicate);
            } else if (!Strings.isNullOrEmpty(tenant)) {
                Predicate tenantPredicate = criteriaBuilder.or(WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                                   root,
                                                                                                   "tenant",
                                                                                                   tenant),
                                                               criteriaBuilder.isNull(root.get("tenant")));
                predicates.add(tenantPredicate);
            }

            // Job priority based predicate
            if (!Strings.isNullOrEmpty(priority)) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), JobPriority.valueOf(priority)));
            }

            // Job project name based predicate
            if (!Strings.isNullOrEmpty(projectName)) {
                Predicate projectNamePredicate = WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                     root,
                                                                                     "projectName",
                                                                                     projectName);
                predicates.add(projectNamePredicate);
            }

            // Job status based predicate
            if (status != null && status.getStatus() != null && status.getStatus().size() > 0) {
                List<JobStatus> jobStatuses = status.getStatus()
                                                    .stream()
                                                    .map(JobStatus::valueOf)
                                                    .collect(Collectors.toList());
                predicates.add(root.get("status").in(jobStatuses));
            }

            // Job submitted time based predicate
            comparableLongPredicated(i.getSubmittedTime(), "submittedTime", root, criteriaBuilder, predicates, true);

            // Job last updated time based predicate
            comparableLongPredicated(i.getLastUpdatedTime(),
                                     "lastUpdatedTime",
                                     root,
                                     criteriaBuilder,
                                     predicates,
                                     true);

            comparableLongPredicated(i.getCumulatedCoreTime(),
                                     "cumulatedCoreTime",
                                     root,
                                     criteriaBuilder,
                                     predicates,
                                     true);

            comparableLongPredicated(i.getParentId(), "parentId", root, criteriaBuilder, predicates, true);

            // Job start time based predicate
            comparableLongPredicated(i.getStartedTime(), "startTime", root, criteriaBuilder, predicates, true);

            // Job start time based predicate
            comparableLongPredicated(i.getFinishedTime(), "finishedTime", root, criteriaBuilder, predicates, true);

            // Number of pending/running/etc task predicate
            comparableIntegerPredicated(i.getNumberOfPendingTasks(),
                                        "numberOfPendingTasks",
                                        root,
                                        criteriaBuilder,
                                        predicates);
            comparableIntegerPredicated(i.getNumberOfRunningTasks(),
                                        "numberOfRunningTasks",
                                        root,
                                        criteriaBuilder,
                                        predicates);
            comparableIntegerPredicated(i.getNumberOfFinishedTasks(),
                                        "numberOfFinishedTasks",
                                        root,
                                        criteriaBuilder,
                                        predicates);
            comparableIntegerPredicated(i.getNumberOfFaultyTasks(),
                                        "numberOfFaultyTasks",
                                        root,
                                        criteriaBuilder,
                                        predicates);
            comparableIntegerPredicated(i.getNumberOfFailedTasks(),
                                        "numberOfFailedTasks",
                                        root,
                                        criteriaBuilder,
                                        predicates);
            comparableIntegerPredicated(i.getNumberOfInErrorTasks(),
                                        "numberOfInErrorTasks",
                                        root,
                                        criteriaBuilder,
                                        predicates);

            comparableIntegerPredicated(i.getChildrenCount(), "childrenCount", root, criteriaBuilder, predicates);

            comparableIntegerPredicated(i.getNumberOfNodes(), "numberOfNodes", root, criteriaBuilder, predicates);

            return predicates.toArray(new Predicate[predicates.size()]);

        }).filter(array -> array.length > 0).collect(Collectors.toList());
    }

    private void addVariableValuePredicateForPostgreSQL(CriteriaBuilder criteriaBuilder, List<Predicate> predicates,
            Map<String, String> variable, MapJoin<JobData, String, JobDataVariable> joinMap) {
        predicates.add(criteriaBuilder.like(criteriaBuilder.function("convert_from",
                                                                     String.class,
                                                                     (criteriaBuilder.function("lo_get",
                                                                                               Object.class,
                                                                                               joinMap.get("value")
                                                                                                      .as(Long.class))),
                                                                     criteriaBuilder.literal("UTF8")),
                                            variable.get("value")));
    }

    private JobStatus stringToJobStatus(String stringJobStatus) {
        JobStatus jobStatus = null;
        switch (stringJobStatus) {
            case "CANCELED":
                jobStatus = JobStatus.CANCELED;
                break;
            case "FAILED":
                jobStatus = JobStatus.FAILED;
                break;
            case "FINISHED":
                jobStatus = JobStatus.FINISHED;
                break;
            case "IN_ERROR":
                jobStatus = JobStatus.IN_ERROR;
                break;
            case "KILLED":
                jobStatus = JobStatus.KILLED;
                break;
            case "PAUSED":
                jobStatus = JobStatus.PAUSED;
                break;
            case "PENDING":
                jobStatus = JobStatus.PENDING;
                break;
            case "RUNNING":
                jobStatus = JobStatus.RUNNING;
                break;
            case "STALLED":
                jobStatus = JobStatus.STALLED;
                break;
            default:

        }
        return jobStatus;
    }

    private void comparableLongPredicated(ComparableLongInput input, String name, Root<JobData> root,
            CriteriaBuilder criteriaBuilder, List<Predicate> predicates, boolean filterZeroLess) {
        long before = -1;
        long after = -1;
        if (input != null) {
            before = input.getBefore();
            after = input.getAfter();
        }
        if (before != -1L) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(name), before));
            if (filterZeroLess && after == -1L) {
                predicates.add(criteriaBuilder.greaterThan(root.get(name), 0L));
            }
        }
        if (after != -1L) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(name), after));
        }
    }

    private void comparableIntegerPredicated(ComparableIntegerInput input, String name, Root<JobData> root,
            CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        int before = -1;
        int after = -1;
        if (input != null) {
            before = input.getBefore();
            after = input.getAfter();
        }
        if (before != -1) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(name), before));
        }
        if (after != -1) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(name), after));
        }
    }

    @Override
    protected Function<Map<String, Object>, JobInput> mapFunction() {
        return JobInput::new;
    }

}
