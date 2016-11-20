
package org.ow2.proactive.scheduling.api.beans.v2;

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
    "tasks",
    "totalNumberOfTasks"
})
public class JobNode
    extends Node
    implements Serializable
{

    /**
     * 
     */
    @JsonProperty("dataManagement")
    private DataManagement dataManagement;
    /**
     * 
     */
    @JsonProperty("numberOfFailedTasks")
    private int numberOfFailedTasks;
    /**
     * 
     */
    @JsonProperty("numberOfFaultyTasks")
    private int numberOfFaultyTasks;
    /**
     * 
     */
    @JsonProperty("numberOfFinishedTasks")
    private int numberOfFinishedTasks;
    /**
     * 
     */
    @JsonProperty("numberOfInErrorTasks")
    private int numberOfInErrorTasks;
    /**
     * 
     */
    @JsonProperty("numberOfPendingTasks")
    private int numberOfPendingTasks;
    /**
     * 
     */
    @JsonProperty("numberOfRunningTasks")
    private int numberOfRunningTasks;
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
    private long removedTime;
    /**
     * 
     */
    @JsonProperty("submittedTime")
    private long submittedTime;
    /**
     * 
     */
    @JsonProperty("tasks")
    private Tasks tasks;
    /**
     * 
     */
    @JsonProperty("totalNumberOfTasks")
    private int totalNumberOfTasks;
    private final static long serialVersionUID = -4206419167070447449L;

    /**
     * 
     * @return
     *     The dataManagement
     */
    @JsonProperty("dataManagement")
    public DataManagement getDataManagement() {
        return dataManagement;
    }

    /**
     * 
     * @param dataManagement
     *     The dataManagement
     */
    @JsonProperty("dataManagement")
    public void setDataManagement(DataManagement dataManagement) {
        this.dataManagement = dataManagement;
    }

    /**
     * 
     * @return
     *     The numberOfFailedTasks
     */
    @JsonProperty("numberOfFailedTasks")
    public int getNumberOfFailedTasks() {
        return numberOfFailedTasks;
    }

    /**
     * 
     * @param numberOfFailedTasks
     *     The numberOfFailedTasks
     */
    @JsonProperty("numberOfFailedTasks")
    public void setNumberOfFailedTasks(int numberOfFailedTasks) {
        this.numberOfFailedTasks = numberOfFailedTasks;
    }

    /**
     * 
     * @return
     *     The numberOfFaultyTasks
     */
    @JsonProperty("numberOfFaultyTasks")
    public int getNumberOfFaultyTasks() {
        return numberOfFaultyTasks;
    }

    /**
     * 
     * @param numberOfFaultyTasks
     *     The numberOfFaultyTasks
     */
    @JsonProperty("numberOfFaultyTasks")
    public void setNumberOfFaultyTasks(int numberOfFaultyTasks) {
        this.numberOfFaultyTasks = numberOfFaultyTasks;
    }

    /**
     * 
     * @return
     *     The numberOfFinishedTasks
     */
    @JsonProperty("numberOfFinishedTasks")
    public int getNumberOfFinishedTasks() {
        return numberOfFinishedTasks;
    }

    /**
     * 
     * @param numberOfFinishedTasks
     *     The numberOfFinishedTasks
     */
    @JsonProperty("numberOfFinishedTasks")
    public void setNumberOfFinishedTasks(int numberOfFinishedTasks) {
        this.numberOfFinishedTasks = numberOfFinishedTasks;
    }

    /**
     * 
     * @return
     *     The numberOfInErrorTasks
     */
    @JsonProperty("numberOfInErrorTasks")
    public int getNumberOfInErrorTasks() {
        return numberOfInErrorTasks;
    }

    /**
     * 
     * @param numberOfInErrorTasks
     *     The numberOfInErrorTasks
     */
    @JsonProperty("numberOfInErrorTasks")
    public void setNumberOfInErrorTasks(int numberOfInErrorTasks) {
        this.numberOfInErrorTasks = numberOfInErrorTasks;
    }

    /**
     * 
     * @return
     *     The numberOfPendingTasks
     */
    @JsonProperty("numberOfPendingTasks")
    public int getNumberOfPendingTasks() {
        return numberOfPendingTasks;
    }

    /**
     * 
     * @param numberOfPendingTasks
     *     The numberOfPendingTasks
     */
    @JsonProperty("numberOfPendingTasks")
    public void setNumberOfPendingTasks(int numberOfPendingTasks) {
        this.numberOfPendingTasks = numberOfPendingTasks;
    }

    /**
     * 
     * @return
     *     The numberOfRunningTasks
     */
    @JsonProperty("numberOfRunningTasks")
    public int getNumberOfRunningTasks() {
        return numberOfRunningTasks;
    }

    /**
     * 
     * @param numberOfRunningTasks
     *     The numberOfRunningTasks
     */
    @JsonProperty("numberOfRunningTasks")
    public void setNumberOfRunningTasks(int numberOfRunningTasks) {
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
    public long getRemovedTime() {
        return removedTime;
    }

    /**
     * 
     * @param removedTime
     *     The removedTime
     */
    @JsonProperty("removedTime")
    public void setRemovedTime(long removedTime) {
        this.removedTime = removedTime;
    }

    /**
     * 
     * @return
     *     The submittedTime
     */
    @JsonProperty("submittedTime")
    public long getSubmittedTime() {
        return submittedTime;
    }

    /**
     * 
     * @param submittedTime
     *     The submittedTime
     */
    @JsonProperty("submittedTime")
    public void setSubmittedTime(long submittedTime) {
        this.submittedTime = submittedTime;
    }

    /**
     * 
     * @return
     *     The tasks
     */
    @JsonProperty("tasks")
    public Tasks getTasks() {
        return tasks;
    }

    /**
     * 
     * @param tasks
     *     The tasks
     */
    @JsonProperty("tasks")
    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }

    /**
     * 
     * @return
     *     The totalNumberOfTasks
     */
    @JsonProperty("totalNumberOfTasks")
    public int getTotalNumberOfTasks() {
        return totalNumberOfTasks;
    }

    /**
     * 
     * @param totalNumberOfTasks
     *     The totalNumberOfTasks
     */
    @JsonProperty("totalNumberOfTasks")
    public void setTotalNumberOfTasks(int totalNumberOfTasks) {
        this.totalNumberOfTasks = totalNumberOfTasks;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
