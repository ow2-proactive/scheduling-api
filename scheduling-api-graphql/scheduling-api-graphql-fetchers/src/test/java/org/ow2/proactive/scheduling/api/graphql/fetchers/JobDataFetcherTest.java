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

import static com.google.common.truth.Truth.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.ow2.proactive.scheduler.common.job.JobPriority;
import org.ow2.proactive.scheduler.common.job.JobStatus;
import org.ow2.proactive.scheduler.common.task.OnTaskError;
import org.ow2.proactive.scheduler.core.db.JobContent;
import org.ow2.proactive.scheduler.core.db.JobData;
import org.ow2.proactive.scheduler.core.db.JobDataVariable;
import org.ow2.proactive.scheduling.api.graphql.schema.type.DataManagement;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Job;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;


/**
 * @author ActiveEon Team
 */
@RunWith(MockitoJUnitRunner.class)
public class JobDataFetcherTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private JobDataFetcher jobDataFetcher;

    @Test
    public void testDataMapping() throws Exception {

        JobData jobData = new JobData();

        jobData.setCredentials(null); // currently, not in GraphQL data model
        jobData.setDescription("description");
        jobData.setFinishedTime(1);
        jobData.setGenericInformation(ImmutableMap.of("gk1", "gv1"));
        jobData.setGlobalSpace("globalSpace");
        jobData.setId(42L);
        jobData.setInErrorTime(2);
        jobData.setInputSpace("inputSpace");
        jobData.setJobContent(ImmutableList.of(new JobContent())); // currently, not in GraphQL data model
        jobData.setJobName("name");
        jobData.setMaxNumberOfExecution(3);
        jobData.setNumberOfFailedTasks(4);
        jobData.setNumberOfFaultyTasks(5);
        jobData.setNumberOfFinishedTasks(6);
        jobData.setNumberOfInErrorTasks(7);
        jobData.setNumberOfPendingTasks(8);
        jobData.setNumberOfRunningTasks(9);
        jobData.setOnTaskErrorString(OnTaskError.NONE);
        jobData.setOutputSpace("outputSpace");
        jobData.setOwner("owner");
        jobData.setPriority(JobPriority.LOW);
        jobData.setProjectName("projectName");
        jobData.setRemovedTime(10);
        jobData.setStartTime(11);
        jobData.setStatus(JobStatus.RUNNING);
        jobData.setSubmittedTime(12);
        jobData.setTasks(null); // currently, not in GraphQL data model
        jobData.setToBeRemoved(false); // currently, not in GraphQL data model
        jobData.setTotalNumberOfTasks(13);
        jobData.setUserSpace("userSpace");

        JobDataVariable variable1 = new JobDataVariable();
        variable1.setName("vk1");
        variable1.setValue("vv1");

        jobData.setVariables(ImmutableMap.of("vk1", variable1));

        List<JobData> jobs = Collections.singletonList(jobData);
        Stream<Job> jobStream = jobDataFetcher.dataMapping(jobs.stream());

        Optional<Job> firstElement = jobStream.findFirst();

        assertThat(firstElement.isPresent()).isTrue();

        Job job = firstElement.get();

        DataManagement dataManagement = job.getDataManagement();

        assertThat(dataManagement).isNotNull();
        assertThat(dataManagement.getGlobalSpaceUrl()).isEqualTo(jobData.getGlobalSpace());
        assertThat(dataManagement.getInputSpaceUrl()).isEqualTo(jobData.getInputSpace());
        assertThat(dataManagement.getOutputSpaceUrl()).isEqualTo(jobData.getOutputSpace());
        assertThat(dataManagement.getUserSpaceUrl()).isEqualTo(jobData.getUserSpace());

        assertThat(job.getDescription()).isEqualTo(jobData.getDescription());
        assertThat(job.getFinishedTime()).isEqualTo(jobData.getFinishedTime());
        assertThat(job.getGenericInformation()).isEqualTo(jobData.getGenericInformation());
        assertThat(job.getId()).isEqualTo(jobData.getId());
        assertThat(job.getInErrorTime()).isEqualTo(jobData.getInErrorTime());
        assertThat(job.getMaxNumberOfExecution()).isEqualTo(jobData.getMaxNumberOfExecution());
        assertThat(job.getName()).isEqualTo(jobData.getJobName());
        assertThat(job.getNumberOfFailedTasks()).isEqualTo(jobData.getNumberOfFailedTasks());
        assertThat(job.getNumberOfFaultyTasks()).isEqualTo(jobData.getNumberOfFaultyTasks());
        assertThat(job.getNumberOfFinishedTasks()).isEqualTo(jobData.getNumberOfFinishedTasks());
        assertThat(job.getNumberOfInErrorTasks()).isEqualTo(jobData.getNumberOfInErrorTasks());
        assertThat(job.getNumberOfPendingTasks()).isEqualTo(jobData.getNumberOfPendingTasks());
        assertThat(job.getNumberOfRunningTasks()).isEqualTo(jobData.getNumberOfRunningTasks());

        assertThat(job.getOnTaskError()).isEqualTo(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE,
                                                                             jobData.getOnTaskErrorString()));

        assertThat(job.getOwner()).isEqualTo(jobData.getOwner());
        assertThat(job.getPriority()).isEqualTo(jobData.getPriority().name());
        assertThat(job.getProjectName()).isEqualTo(jobData.getProjectName());
        assertThat(job.getRemovedTime()).isEqualTo(jobData.getRemovedTime());
        assertThat(job.getStartTime()).isEqualTo(jobData.getStartTime());
        assertThat(job.getSubmittedTime()).isEqualTo(jobData.getSubmittedTime());
        assertThat(job.getTotalNumberOfTasks()).isEqualTo(jobData.getTotalNumberOfTasks());
        assertThat(job.getVariables().size()).isEqualTo(jobData.getVariables().size());
        assertThat(job.getVariables().get("vk1")).isEqualTo(jobData.getVariables().get("vk1").getValue());
    }

}
