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

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.ow2.proactive.scheduler.core.db.JobData;
import org.ow2.proactive.scheduling.api.graphql.fetchers.converter.JobInputConverter;
import org.ow2.proactive.scheduling.api.graphql.fetchers.converter.JobTaskFilterInputBiFunction;
import org.ow2.proactive.scheduling.api.graphql.fetchers.cursors.JobCursorMapper;
import org.ow2.proactive.scheduling.api.graphql.schema.type.DataManagement;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Job;
import org.ow2.proactive.utils.ObjectByteConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableMap;

import graphql.schema.DataFetchingEnvironment;


/**
 * @author ActiveEon Team
 */
@Component
@Transactional(readOnly = true)
public class JobDataFetcher extends DatabaseConnectionFetcher<JobData, Job> {

    @Override
    public Object get(DataFetchingEnvironment environment) {

        Function<Root<JobData>, Path<? extends Number>> entityId = root -> root.get("id");

        BiFunction<CriteriaBuilder, Root<JobData>, List<Predicate[]>> criteria = new JobTaskFilterInputBiFunction(new JobInputConverter(),
                                                                                                                  environment);

        return createPaginatedConnection(environment,
                                         JobData.class,
                                         entityId,
                                         Comparator.comparingLong(JobData::getId),
                                         criteria,
                                         new JobCursorMapper());
    }

    @Override
    protected Stream<Job> dataMapping(Stream<JobData> dataStream) {
        return dataStream.map(jobData -> Job.builder()
                                            .dataManagement(DataManagement.builder()
                                                                          .globalSpaceUrl(jobData.getGlobalSpace())
                                                                          .inputSpaceUrl(jobData.getInputSpace())
                                                                          .outputSpaceUrl(jobData.getOutputSpace())
                                                                          .userSpaceUrl(jobData.getUserSpace())
                                                                          .build())
                                            .description(jobData.getDescription())
                                            .finishedTime(jobData.getFinishedTime())
                                            .genericInformation(jobData.getGenericInformation())
                                            .id(jobData.getId())
                                            .inErrorTime(jobData.getInErrorTime())
                                            .lastUpdatedTime(jobData.getLastUpdatedTime())
                                            .maxNumberOfExecution(jobData.getMaxNumberOfExecution())
                                            .name(jobData.getJobName())
                                            .numberOfFailedTasks(jobData.getNumberOfFailedTasks())
                                            .numberOfFaultyTasks(jobData.getNumberOfFaultyTasks())
                                            .numberOfFinishedTasks(jobData.getNumberOfFinishedTasks())
                                            .numberOfInErrorTasks(jobData.getNumberOfInErrorTasks())
                                            .numberOfPendingTasks(jobData.getNumberOfPendingTasks())
                                            .numberOfRunningTasks(jobData.getNumberOfRunningTasks())
                                            .onTaskError(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE,
                                                                                   jobData.getOnTaskErrorString()))
                                            .owner(jobData.getOwner())
                                            .priority(jobData.getPriority().name())
                                            .projectName(jobData.getProjectName())
                                            .removedTime(jobData.getRemovedTime())
                                            .status(jobData.getStatus().name())
                                            .startTime(jobData.getStartTime())
                                            .submittedTime(jobData.getSubmittedTime())
                                            .totalNumberOfTasks(jobData.getTotalNumberOfTasks())
                                            // TODO Currently map the JobVariable object to a simple string (its value).
                                            // Need to map the whole object later
                                            .variables(getVariables(jobData))
                                            .resultMap(mapOfByteArrayToString(jobData.getResultMap()))
                                            .build());
    }

    /**
     * Transform Variables from a JobData into a simple Map.
     * @param jobData The jobData to retrieve the variables from
     * @return the Map of all variables in the form key -> value
     */
    protected Map<String, String> getVariables(JobData jobData) {
        if (jobData.getVariables() == null || jobData.getVariables().size() == 0) {
            return ImmutableMap.of();
        } else {
            return jobData.getVariables()
                          .entrySet()
                          .stream()
                          .filter(e -> e.getKey() != null && e.getValue() != null)
                          .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getValue()));
        }
    }

    /**
     * Transform a map of byte array to map of string.
     * This is used mainly for transforming resultMap values wich are stored as blobs in the database (byte[]) into Strings. 
     * @param input
     * @return
     */
    public static Map<String, String> mapOfByteArrayToString(Map<String, byte[]> input) {
        if (input == null) {
            return null;
        }

        Map<String, String> answer = new HashMap<>(input.size());
        input.forEach((key, value) -> answer.put(key, ObjectByteConverter.byteArrayToObject(value).toString()));
        return answer;
    }

}
