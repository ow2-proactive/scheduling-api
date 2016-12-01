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

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ow2.proactive.scheduler.core.db.JobData;
import org.ow2.proactive.scheduling.api.graphql.fetchers.converter.JobInputConverter;
import org.ow2.proactive.scheduling.api.graphql.fetchers.converter.JobTaskFilterInputBiFunction;
import org.ow2.proactive.scheduling.api.graphql.fetchers.cursor.JobCursorMapper;
import org.ow2.proactive.scheduling.api.graphql.schema.type.DataManagement;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Job;
import org.ow2.proactive.scheduling.api.graphql.schema.type.User;
import org.ow2.proactive.scheduling.api.graphql.schema.type.inputs.JobInput;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import graphql.schema.DataFetchingEnvironment;

import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.OWNER;

/**
 * @author ActiveEon Team
 */
public class JobDataFetcher extends DatabaseConnectionFetcher<JobData, Job> {

    public JobDataFetcher(Supplier<EntityManager> entityManager) {
        super(entityManager);
    }

    @Override
    public Object get(DataFetchingEnvironment environment) {

        Function<Root<JobData>, Path<? extends Number>> entityId = root -> root.get("id");

        BiFunction<CriteriaBuilder, Root<JobData>, List<Predicate[]>> criteria = new JobTaskFilterInputBiFunction(
                environment, new JobInputConverter());

        return createPaginatedConnection(environment,
                JobData.class,
                entityId,
                Comparator.comparingLong(JobData::getId),
                criteria,
                new JobCursorMapper());
    }

    private void filterOnUser(DataFetchingEnvironment environment, List<JobInput> input) {
        User user = (User) environment.getSource();
        LinkedHashMap<String, Object> ownerInput = new LinkedHashMap<>();
        if (!Strings.isNullOrEmpty(user.getLogin())) {
            ownerInput.put(OWNER.getName(), user.getLogin());
        } else {
            throw new IllegalStateException("Missing login name");
        }
        input.add(new JobInput(ownerInput));
    }

    @Override
    protected Stream<Job> dataMapping(Stream<JobData> taskStream) {
        return taskStream.parallel()
                .map(jobData -> Job.builder()
                        .dataManagement(
                                DataManagement.builder()
                                        .globalSpaceUrl(jobData.getGlobalSpace())
                                        .inputSpaceUrl(jobData.getInputSpace())
                                        .outputSpaceUrl(jobData.getOutputSpace())
                                        .userSpaceUrl(jobData.getUserSpace()).build()
                        )
                        .description(jobData.getDescription())
                        .finishedTime(jobData.getFinishedTime())
                        .genericInformation(jobData.getGenericInformation())
                        .id(jobData.getId())
                        .inErrorTime(jobData.getInErrorTime())
                        .maxNumberOfExecution(jobData.getMaxNumberOfExecution())
                        .name(jobData.getJobName())
                        .numberOfFailedTasks(jobData.getNumberOfFailedTasks())
                        .numberOfFaultyTasks(jobData.getNumberOfFaultyTasks())
                        .numberOfFinishedTasks(jobData.getNumberOfFinishedTasks())
                        .numberOfInErrorTasks(jobData.getNumberOfInErrorTasks())
                        .numberOfPendingTasks(jobData.getNumberOfPendingTasks())
                        .numberOfRunningTasks(jobData.getNumberOfRunningTasks())
                        .onTaskError(
                                CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE,
                                        jobData.getOnTaskErrorString()))
                        .owner(jobData.getOwner())
                        .priority(jobData.getPriority().name())
                        .projectName(jobData.getProjectName())
                        .removedTime(jobData.getRemovedTime())
                        .status(jobData.getStatus().name())
                        .startTime(jobData.getStartTime())
                        .submittedTime(jobData.getSubmittedTime())
                        .totalNumberOfTasks(jobData.getTotalNumberOfTasks())
                        .variables(jobData.getVariables())
                        .build());
    }

}
