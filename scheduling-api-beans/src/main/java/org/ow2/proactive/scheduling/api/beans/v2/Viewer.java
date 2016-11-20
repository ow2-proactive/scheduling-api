
package org.ow2.proactive.scheduling.api.beans.v2;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobs",
    "login",
    "sessionId"
})
public class Viewer implements Serializable
{

    /**
     * 
     */
    @JsonProperty("jobs")
    private Jobs jobs;
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
    private final static long serialVersionUID = 6256695915038554438L;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
