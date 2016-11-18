
package org.ow2.proactive.scheduling.api.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class GraphqlNode implements Serializable
{

    /**
     * 
     */
    @JsonProperty("description")
    private String description;
    /**
     * 
     */
    @JsonProperty("finishedTime")
    private double finishedTime;
    /**
     * 
     */
    @JsonProperty("genericInformation")
    private List<GraphqlGenericInformation> genericInformation = new ArrayList<GraphqlGenericInformation>();
    /**
     * 
     */
    @JsonProperty("id")
    private String id;
    /**
     * 
     */
    @JsonProperty("inErrorTime")
    private double inErrorTime;
    /**
     * 
     */
    @JsonProperty("maxNumberOfExecution")
    private double maxNumberOfExecution;
    /**
     * 
     */
    @JsonProperty("name")
    private String name;
    /**
     * 
     */
    @JsonProperty("onTaskError")
    private String onTaskError;
    /**
     * 
     */
    @JsonProperty("startTime")
    private double startTime;
    /**
     * 
     */
    @JsonProperty("status")
    private String status;
    /**
     * 
     */
    @JsonProperty("variables")
    private List<GraphqlVariable> variables = new ArrayList<GraphqlVariable>();
    private final static long serialVersionUID = -6577666967600310871L;

    /**
     * 
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The finishedTime
     */
    @JsonProperty("finishedTime")
    public double getFinishedTime() {
        return finishedTime;
    }

    /**
     * 
     * @param finishedTime
     *     The finishedTime
     */
    @JsonProperty("finishedTime")
    public void setFinishedTime(double finishedTime) {
        this.finishedTime = finishedTime;
    }

    /**
     * 
     * @return
     *     The genericInformation
     */
    @JsonProperty("genericInformation")
    public List<GraphqlGenericInformation> getGenericInformation() {
        return genericInformation;
    }

    /**
     * 
     * @param genericInformation
     *     The genericInformation
     */
    @JsonProperty("genericInformation")
    public void setGenericInformation(List<GraphqlGenericInformation> genericInformation) {
        this.genericInformation = genericInformation;
    }

    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The inErrorTime
     */
    @JsonProperty("inErrorTime")
    public double getInErrorTime() {
        return inErrorTime;
    }

    /**
     * 
     * @param inErrorTime
     *     The inErrorTime
     */
    @JsonProperty("inErrorTime")
    public void setInErrorTime(double inErrorTime) {
        this.inErrorTime = inErrorTime;
    }

    /**
     * 
     * @return
     *     The maxNumberOfExecution
     */
    @JsonProperty("maxNumberOfExecution")
    public double getMaxNumberOfExecution() {
        return maxNumberOfExecution;
    }

    /**
     * 
     * @param maxNumberOfExecution
     *     The maxNumberOfExecution
     */
    @JsonProperty("maxNumberOfExecution")
    public void setMaxNumberOfExecution(double maxNumberOfExecution) {
        this.maxNumberOfExecution = maxNumberOfExecution;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The onTaskError
     */
    @JsonProperty("onTaskError")
    public String getOnTaskError() {
        return onTaskError;
    }

    /**
     * 
     * @param onTaskError
     *     The onTaskError
     */
    @JsonProperty("onTaskError")
    public void setOnTaskError(String onTaskError) {
        this.onTaskError = onTaskError;
    }

    /**
     * 
     * @return
     *     The startTime
     */
    @JsonProperty("startTime")
    public double getStartTime() {
        return startTime;
    }

    /**
     * 
     * @param startTime
     *     The startTime
     */
    @JsonProperty("startTime")
    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    /**
     * 
     * @return
     *     The status
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The variables
     */
    @JsonProperty("variables")
    public List<GraphqlVariable> getVariables() {
        return variables;
    }

    /**
     * 
     * @param variables
     *     The variables
     */
    @JsonProperty("variables")
    public void setVariables(List<GraphqlVariable> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
