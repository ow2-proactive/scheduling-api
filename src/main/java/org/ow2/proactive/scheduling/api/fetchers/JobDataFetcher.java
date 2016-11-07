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
package org.ow2.proactive.scheduling.api.fetchers;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.ow2.proactive.scheduler.core.db.JobData;
import org.ow2.proactive.scheduling.api.repository.JobRepository;
import org.ow2.proactive.scheduling.api.schema.type.Job;
import org.ow2.proactive.scheduling.api.schema.type.enums.JobPriority;
import org.ow2.proactive.scheduling.api.service.ApplicationContextProvider;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;


public class JobDataFetcher implements DataFetcher {

    private JobRepository jobRepository;

    public JobDataFetcher() {
        this.jobRepository = ApplicationContextProvider.getApplicationContext().getBean(JobRepository.class);
    }

    @Override
    public Object get(DataFetchingEnvironment environment) {
        Iterable<JobData> jobs = jobRepository.findAll();

        return StreamSupport.stream(jobs.spliterator(), false).map(jobData ->
            Job.builder()
                    .id(jobData.getId())
                    .name(jobData.getJobName())
                    .description(jobData.getDescription())
                    .priority(JobPriority.valueOf(jobData.getPriority().name()))
                    .owner(jobData.getOwner())
                    .projectName(jobData.getProjectName())
                    .startTime(jobData.getStartTime())
                    .inErrorTime(jobData.getInErrorTime())
                    .finishedTime(jobData.getFinishedTime())
                    .submittedTime(jobData.getSubmittedTime())
                    .removedTime(jobData.getRemovedTime())
                    .totalNumberOfTasks(jobData.getTotalNumberOfTasks())
                    .numberOfPendingTasks(jobData.getNumberOfPendingTasks())
                    .numberOfRunningTasks(jobData.getNumberOfRunningTasks())
                    .numberOfFinishedTasks(jobData.getNumberOfFinishedTasks())
                    .numberOfFailedTasks(jobData.getNumberOfFailedTasks())
                    .numberOfFaultyTasks(jobData.getNumberOfFaultyTasks())
                    .numberOfInErrorTasks(jobData.getNumberOfInErrorTasks())
                    .genericInformation(jobData.getGenericInformation())
                    .variables(jobData.getVariables())
                    .build()
        ).collect(Collectors.toList());
    }

}
