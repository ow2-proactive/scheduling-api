package org.ow2.proactive.scheduling.api.graphql.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class SchedulingApiResponseTest {

    @Test
    public void testSchedulingApiResponse() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        URL url = Resources.getResource("jobs.json");
        File file = new File(url.toURI());
        SchedulingApiResponse response = mapper.readValue(file, SchedulingApiResponse.class);
        assertThat(response.getData().getViewer().getJobs().getEdges(), is(notNullValue()));
        assertThat(response.getData().getViewer().getJobs().getPageInfo(), is(notNullValue()));
        assertThat(response.getData().getViewer().getJobs().getEdges().size(), is(4));
        JobNode job = response.getData().getViewer().getJobs().getEdges().get(0).getJobNode();
        assertThat(job, is(notNullValue()));
        assertThat(job.getId(), is(1L));
    }

}