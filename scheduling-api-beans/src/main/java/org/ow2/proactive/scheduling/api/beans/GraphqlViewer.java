
package org.ow2.proactive.scheduling.api.beans;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "login",
    "sessionId",
    "jobs"
})
public class GraphqlViewer implements Serializable
{

    /**
     * 
     */
    @JsonProperty("login")
    private String login;
    /**
     * 
     */
    @JsonProperty("sessionId")
    private String sessionId;
    /**
     * 
     */
    @JsonProperty("jobs")
    private GraphqlJobs jobs;
    private final static long serialVersionUID = 3535245030002134725L;

    /**
     * 
     * @return
     *     The login
     */
    @JsonProperty("login")
    public String getLogin() {
        return login;
    }

    /**
     * 
     * @param login
     *     The login
     */
    @JsonProperty("login")
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * 
     * @return
     *     The sessionId
     */
    @JsonProperty("sessionId")
    public String getSessionId() {
        return sessionId;
    }

    /**
     * 
     * @param sessionId
     *     The sessionId
     */
    @JsonProperty("sessionId")
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

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
