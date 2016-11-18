
package org.ow2.proactive.scheduling.api.beans;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "dataManagement",
    "numberOfFailedTasks",
    "numberOfFaultyTasks",
    "numberOfFinishedTasks",
    "numberOfInErrorTasks",
    "numberOfPendingTasks",
    "numberOfRunningTasks",
    "owner",
    "priority",
    "projectName",
    "removedTime",
    "submittedTime",
    "totalNumberOfTasks",
    "tasks"
})
public class GraphqlJobNode
    extends GraphqlNode
    implements Serializable
{

    /**
     * 
     */
    @JsonProperty("dataManagement")
    private GraphqlDataManagement dataManagement;
    /**
     * 
     */
    @JsonProperty("numberOfFailedTasks")
    private double numberOfFailedTasks;
    /**
     * 
     */
    @JsonProperty("numberOfFaultyTasks")
    private double numberOfFaultyTasks;
    /**
     * 
     */
    @JsonProperty("numberOfFinishedTasks")
    private double numberOfFinishedTasks;
    /**
     * 
     */
    @JsonProperty("numberOfInErrorTasks")
    private double numberOfInErrorTasks;
    /**
     * 
     */
    @JsonProperty("numberOfPendingTasks")
    private double numberOfPendingTasks;
    /**
     * 
     */
    @JsonProperty("numberOfRunningTasks")
    private double numberOfRunningTasks;
    /**
     * 
     */
    @JsonProperty("owner")
    private String owner;
    /**
     * 
     */
    @JsonProperty("priority")
    private String priority;
    /**
     * 
     */
    @JsonProperty("projectName")
    private String projectName;
    /**
     * 
     */
    @JsonProperty("removedTime")
    private double removedTime;
    /**
     * 
     */
    @JsonProperty("submittedTime")
    private double submittedTime;
    /**
     * 
     */
    @JsonProperty("totalNumberOfTasks")
    private double totalNumberOfTasks;
    /**
     * 
     */
    @JsonProperty("tasks")
    private GraphqlTasks tasks;
    private final static long serialVersionUID = -6807943921286212691L;

    /**
     * 
     * @return
     *     The dataManagement
     */
    @JsonProperty("dataManagement")
    public GraphqlDataManagement getDataManagement() {
        return dataManagement;
    }

    /**
     * 
     * @param dataManagement
     *     The dataManagement
     */
    @JsonProperty("dataManagement")
    public void setDataManagement(GraphqlDataManagement dataManagement) {
        this.dataManagement = dataManagement;
    }

    /**
     * 
     * @return
     *     The numberOfFailedTasks
     */
    @JsonProperty("numberOfFailedTasks")
    public double getNumberOfFailedTasks() {
        return numberOfFailedTasks;
    }

    /**
     * 
     * @param numberOfFailedTasks
     *     The numberOfFailedTasks
     */
    @JsonProperty("numberOfFailedTasks")
    public void setNumberOfFailedTasks(double numberOfFailedTasks) {
        this.numberOfFailedTasks = numberOfFailedTasks;
    }

    /**
     * 
     * @return
     *     The numberOfFaultyTasks
     */
    @JsonProperty("numberOfFaultyTasks")
    public double getNumberOfFaultyTasks() {
        return numberOfFaultyTasks;
    }

    /**
     * 
     * @param numberOfFaultyTasks
     *     The numberOfFaultyTasks
     */
    @JsonProperty("numberOfFaultyTasks")
    public void setNumberOfFaultyTasks(double numberOfFaultyTasks) {
        this.numberOfFaultyTasks = numberOfFaultyTasks;
    }

    /**
     * 
     * @return
     *     The numberOfFinishedTasks
     */
    @JsonProperty("numberOfFinishedTasks")
    public double getNumberOfFinishedTasks() {
        return numberOfFinishedTasks;
    }

    /**
     * 
     * @param numberOfFinishedTasks
     *     The numberOfFinishedTasks
     */
    @JsonProperty("numberOfFinishedTasks")
    public void setNumberOfFinishedTasks(double numberOfFinishedTasks) {
        this.numberOfFinishedTasks = numberOfFinishedTasks;
    }

    /**
     * 
     * @return
     *     The numberOfInErrorTasks
     */
    @JsonProperty("numberOfInErrorTasks")
    public double getNumberOfInErrorTasks() {
        return numberOfInErrorTasks;
    }

    /**
     * 
     * @param numberOfInErrorTasks
     *     The numberOfInErrorTasks
     */
    @JsonProperty("numberOfInErrorTasks")
    public void setNumberOfInErrorTasks(double numberOfInErrorTasks) {
        this.numberOfInErrorTasks = numberOfInErrorTasks;
    }

    /**
     * 
     * @return
     *     The numberOfPendingTasks
     */
    @JsonProperty("numberOfPendingTasks")
    public double getNumberOfPendingTasks() {
        return numberOfPendingTasks;
    }

    /**
     * 
     * @param numberOfPendingTasks
     *     The numberOfPendingTasks
     */
    @JsonProperty("numberOfPendingTasks")
    public void setNumberOfPendingTasks(double numberOfPendingTasks) {
        this.numberOfPendingTasks = numberOfPendingTasks;
    }

    /**
     * 
     * @return
     *     The numberOfRunningTasks
     */
    @JsonProperty("numberOfRunningTasks")
    public double getNumberOfRunningTasks() {
        return numberOfRunningTasks;
    }

    /**
     * 
     * @param numberOfRunningTasks
     *     The numberOfRunningTasks
     */
    @JsonProperty("numberOfRunningTasks")
    public void setNumberOfRunningTasks(double numberOfRunningTasks) {
        this.numberOfRunningTasks = numberOfRunningTasks;
    }

    /**
     * 
     * @return
     *     The owner
     */
    @JsonProperty("owner")
    public String getOwner() {
        return owner;
    }

    /**
     * 
     * @param owner
     *     The owner
     */
    @JsonProperty("owner")
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * 
     * @return
     *     The priority
     */
    @JsonProperty("priority")
    public String getPriority() {
        return priority;
    }

    /**
     * 
     * @param priority
     *     The priority
     */
    @JsonProperty("priority")
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * 
     * @return
     *     The projectName
     */
    @JsonProperty("projectName")
    public String getProjectName() {
        return projectName;
    }

    /**
     * 
     * @param projectName
     *     The projectName
     */
    @JsonProperty("projectName")
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * 
     * @return
     *     The removedTime
     */
    @JsonProperty("removedTime")
    public double getRemovedTime() {
        return removedTime;
    }

    /**
     * 
     * @param removedTime
     *     The removedTime
     */
    @JsonProperty("removedTime")
    public void setRemovedTime(double removedTime) {
        this.removedTime = removedTime;
    }

    /**
     * 
     * @return
     *     The submittedTime
     */
    @JsonProperty("submittedTime")
    public double getSubmittedTime() {
        return submittedTime;
    }

    /**
     * 
     * @param submittedTime
     *     The submittedTime
     */
    @JsonProperty("submittedTime")
    public void setSubmittedTime(double submittedTime) {
        this.submittedTime = submittedTime;
    }

    /**
     * 
     * @return
     *     The totalNumberOfTasks
     */
    @JsonProperty("totalNumberOfTasks")
    public double getTotalNumberOfTasks() {
        return totalNumberOfTasks;
    }

    /**
     * 
     * @param totalNumberOfTasks
     *     The totalNumberOfTasks
     */
    @JsonProperty("totalNumberOfTasks")
    public void setTotalNumberOfTasks(double totalNumberOfTasks) {
        this.totalNumberOfTasks = totalNumberOfTasks;
    }

    /**
     * 
     * @return
     *     The tasks
     */
    @JsonProperty("tasks")
    public GraphqlTasks getTasks() {
        return tasks;
    }

    /**
     * 
     * @param tasks
     *     The tasks
     */
    @JsonProperty("tasks")
    public void setTasks(GraphqlTasks tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
