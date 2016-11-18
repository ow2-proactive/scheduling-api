
package org.ow2.proactive.scheduling.api.beans;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobs"
})
public class GraphqlData implements Serializable
{

    /**
     * 
     */
    @JsonProperty("jobs")
    private GraphqlJobs jobs;
    private final static long serialVersionUID = 999066096856793538L;

    /**
     * 
     * @return
     *     The jobs
     */
    @JsonProperty("jobs")
    public GraphqlJobs getJobs() {
        return jobs;
    }

    /**
     * 
     * @param jobs
     *     The jobs
     */
    @JsonProperty("jobs")
    public void setJobs(GraphqlJobs jobs) {
        this.jobs = jobs;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
