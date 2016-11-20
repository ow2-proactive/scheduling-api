
package org.ow2.proactive.scheduling.api.beans.v2;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobs",
    "version",
    "viewer"
})
public class Data implements Serializable
{

    /**
     * 
     */
    @JsonProperty("jobs")
    private Jobs jobs;
    /**
     * 
     */
    @JsonProperty("version")
    private String version;
    /**
     * 
     */
    @JsonProperty("viewer")
    private Viewer viewer;
    private final static long serialVersionUID = 3661389819103725238L;

    /**
     * 
     * @return
     *     The jobs
     */
    @JsonProperty("jobs")
    public Jobs getJobs() {
        return jobs;
    }

    /**
     * 
     * @param jobs
     *     The jobs
     */
    @JsonProperty("jobs")
    public void setJobs(Jobs jobs) {
        this.jobs = jobs;
    }

    /**
     * 
     * @return
     *     The version
     */
    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    /**
     * 
     * @param version
     *     The version
     */
    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 
     * @return
     *     The viewer
     */
    @JsonProperty("viewer")
    public Viewer getViewer() {
        return viewer;
    }

    /**
     * 
     * @param viewer
     *     The viewer
     */
    @JsonProperty("viewer")
    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
