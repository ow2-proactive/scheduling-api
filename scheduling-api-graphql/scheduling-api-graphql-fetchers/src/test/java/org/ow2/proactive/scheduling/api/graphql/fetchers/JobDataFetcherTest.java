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
package org.ow2.proactive.scheduling.api.graphql.fetchers;

import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.ow2.proactive.scheduler.common.job.JobPriority;
import org.ow2.proactive.scheduler.common.job.JobStatus;
import org.ow2.proactive.scheduler.common.task.OnTaskError;
import org.ow2.proactive.scheduler.core.db.JobContent;
import org.ow2.proactive.scheduler.core.db.JobData;
import org.ow2.proactive.scheduling.api.graphql.schema.type.DataManagement;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Job;
import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.mockito.Mockito;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author ActiveEon Team
 */
public class JobDataFetcherTest {

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
        jobData.setVariables(ImmutableMap.of("vk1", "vv1"));

        Stream<Job> jobStream =
                new JobDataFetcher(() -> Mockito.mock(EntityManager.class)).dataMapping(
                        ImmutableList.of(jobData).stream());

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

        assertThat(job.getOnTaskError()).isEqualTo(
                CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, jobData.getOnTaskErrorString()));

        assertThat(job.getOwner()).isEqualTo(jobData.getOwner());
        assertThat(job.getPriority()).isEqualTo(jobData.getPriority().name());
        assertThat(job.getProjectName()).isEqualTo(jobData.getProjectName());
        assertThat(job.getRemovedTime()).isEqualTo(jobData.getRemovedTime());
        assertThat(job.getStartTime()).isEqualTo(jobData.getStartTime());
        assertThat(job.getSubmittedTime()).isEqualTo(jobData.getSubmittedTime());
        assertThat(job.getTotalNumberOfTasks()).isEqualTo(jobData.getTotalNumberOfTasks());
        assertThat(job.getVariables()).isEqualTo(jobData.getVariables());
    }

}