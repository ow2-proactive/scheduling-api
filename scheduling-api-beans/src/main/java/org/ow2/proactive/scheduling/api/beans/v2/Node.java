
package org.ow2.proactive.scheduling.api.beans.v2;

import java.io.Serializable;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "description",
    "finishedTime",
    "genericInformation",
    "id",
    "inErrorTime",
    "maxNumberOfExecution",
    "name",
    "onTaskError",
    "startTime",
    "status",
    "variables"
})
public class Node implements Serializable
{

    /**
     * 
     */
    @JsonProperty("description")
    private java.lang.String description;
    /**
     * 
     */
    @JsonProperty("finishedTime")
    private long finishedTime;
    @JsonProperty("genericInformation")
    private Map<String, String> genericInformation;
    /**
     * 
     */
    @JsonProperty("id")
    private long id;
    /**
     * 
     */
    @JsonProperty("inErrorTime")
    private long inErrorTime;
    /**
     * 
     */
    @JsonProperty("maxNumberOfExecution")
    private int maxNumberOfExecution;
    /**
     * 
     */
    @JsonProperty("name")
    private java.lang.String name;
    /**
     * 
     */
    @JsonProperty("onTaskError")
    private java.lang.String onTaskError;
    /**
     * 
     */
    @JsonProperty("startTime")
    private long startTime;
    /**
     * 
     */
    @JsonProperty("status")
    private java.lang.String status;
    @JsonProperty("variables")
    private Map<String, String> variables;
    private final static long serialVersionUID = -3258910408444668894L;

    /**
     * 
     * @return
     *     The description
     */
    @JsonProperty("description")
    public java.lang.String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The finishedTime
     */
    @JsonProperty("finishedTime")
    public long getFinishedTime() {
        return finishedTime;
    }

    /**
     * 
     * @param finishedTime
     *     The finishedTime
     */
    @JsonProperty("finishedTime")
    public void setFinishedTime(long finishedTime) {
        this.finishedTime = finishedTime;
    }

    /**
     * 
     * @return
     *     The genericInformation
     */
    @JsonProperty("genericInformation")
    public Map<String, String> getGenericInformation() {
        return genericInformation;
    }

    /**
     * 
     * @param genericInformation
     *     The genericInformation
     */
    @JsonProperty("genericInformation")
    public void setGenericInformation(Map<String, String> genericInformation) {
        this.genericInformation = genericInformation;
    }

    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public long getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(long id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The inErrorTime
     */
    @JsonProperty("inErrorTime")
    public long getInErrorTime() {
        return inErrorTime;
    }

    /**
     * 
     * @param inErrorTime
     *     The inErrorTime
     */
    @JsonProperty("inErrorTime")
    public void setInErrorTime(long inErrorTime) {
        this.inErrorTime = inErrorTime;
    }

    /**
     * 
     * @return
     *     The maxNumberOfExecution
     */
    @JsonProperty("maxNumberOfExecution")
    public int getMaxNumberOfExecution() {
        return maxNumberOfExecution;
    }

    /**
     * 
     * @param maxNumberOfExecution
     *     The maxNumberOfExecution
     */
    @JsonProperty("maxNumberOfExecution")
    public void setMaxNumberOfExecution(int maxNumberOfExecution) {
        this.maxNumberOfExecution = maxNumberOfExecution;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public java.lang.String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The onTaskError
     */
    @JsonProperty("onTaskError")
    public java.lang.String getOnTaskError() {
        return onTaskError;
    }

    /**
     * 
     * @param onTaskError
     *     The onTaskError
     */
    @JsonProperty("onTaskError")
    public void setOnTaskError(java.lang.String onTaskError) {
        this.onTaskError = onTaskError;
    }

    /**
     * 
     * @return
     *     The startTime
     */
    @JsonProperty("startTime")
    public long getStartTime() {
        return startTime;
    }

    /**
     * 
     * @param startTime
     *     The startTime
     */
    @JsonProperty("startTime")
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * 
     * @return
     *     The status
     */
    @JsonProperty("status")
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The variables
     */
    @JsonProperty("variables")
    public Map<String, String> getVariables() {
        return variables;
    }

    /**
     * 
     * @param variables
     *     The variables
     */
    @JsonProperty("variables")
    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    @Override
    public java.lang.String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
