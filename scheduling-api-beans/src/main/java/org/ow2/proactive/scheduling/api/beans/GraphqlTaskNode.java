
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
    "additionalClasspath",
    "executionDuration",
    "executionHostName",
    "javaHome",
    "jobId",
    "jvmArguments",
    "numberOfExecutionLeft",
    "numberOfExecutionOnFailureLeft",
    "preciousLogs",
    "preciousResult",
    "restartMode",
    "resultPreview",
    "runAsMe",
    "scheduledTime",
    "tag",
    "walltime",
    "workingDir"
})
public class GraphqlTaskNode
    extends GraphqlNode
    implements Serializable
{

    /**
     * 
     */
    @JsonProperty("additionalClasspath")
    private List<String> additionalClasspath = new ArrayList<String>();
    /**
     * 
     */
    @JsonProperty("executionDuration")
    private double executionDuration;
    /**
     * 
     */
    @JsonProperty("executionHostName")
    private String executionHostName;
    /**
     * 
     */
    @JsonProperty("javaHome")
    private String javaHome;
    /**
     * 
     */
    @JsonProperty("jobId")
    private double jobId;
    /**
     * 
     */
    @JsonProperty("jvmArguments")
    private List<String> jvmArguments = new ArrayList<String>();
    /**
     * 
     */
    @JsonProperty("numberOfExecutionLeft")
    private double numberOfExecutionLeft;
    /**
     * 
     */
    @JsonProperty("numberOfExecutionOnFailureLeft")
    private double numberOfExecutionOnFailureLeft;
    /**
     * 
     */
    @JsonProperty("preciousLogs")
    private boolean preciousLogs;
    /**
     * 
     */
    @JsonProperty("preciousResult")
    private boolean preciousResult;
    /**
     * 
     */
    @JsonProperty("restartMode")
    private String restartMode;
    /**
     * 
     */
    @JsonProperty("resultPreview")
    private String resultPreview;
    /**
     * 
     */
    @JsonProperty("runAsMe")
    private boolean runAsMe;
    /**
     * 
     */
    @JsonProperty("scheduledTime")
    private double scheduledTime;
    /**
     * 
     */
    @JsonProperty("tag")
    private String tag;
    /**
     * 
     */
    @JsonProperty("walltime")
    private double walltime;
    /**
     * 
     */
    @JsonProperty("workingDir")
    private String workingDir;
    private final static long serialVersionUID = -1976232379918392788L;

    /**
     * 
     * @return
     *     The additionalClasspath
     */
    @JsonProperty("additionalClasspath")
    public List<String> getAdditionalClasspath() {
        return additionalClasspath;
    }

    /**
     * 
     * @param additionalClasspath
     *     The additionalClasspath
     */
    @JsonProperty("additionalClasspath")
    public void setAdditionalClasspath(List<String> additionalClasspath) {
        this.additionalClasspath = additionalClasspath;
    }

    /**
     * 
     * @return
     *     The executionDuration
     */
    @JsonProperty("executionDuration")
    public double getExecutionDuration() {
        return executionDuration;
    }

    /**
     * 
     * @param executionDuration
     *     The executionDuration
     */
    @JsonProperty("executionDuration")
    public void setExecutionDuration(double executionDuration) {
        this.executionDuration = executionDuration;
    }

    /**
     * 
     * @return
     *     The executionHostName
     */
    @JsonProperty("executionHostName")
    public String getExecutionHostName() {
        return executionHostName;
    }

    /**
     * 
     * @param executionHostName
     *     The executionHostName
     */
    @JsonProperty("executionHostName")
    public void setExecutionHostName(String executionHostName) {
        this.executionHostName = executionHostName;
    }

    /**
     * 
     * @return
     *     The javaHome
     */
    @JsonProperty("javaHome")
    public String getJavaHome() {
        return javaHome;
    }

    /**
     * 
     * @param javaHome
     *     The javaHome
     */
    @JsonProperty("javaHome")
    public void setJavaHome(String javaHome) {
        this.javaHome = javaHome;
    }

    /**
     * 
     * @return
     *     The jobId
     */
    @JsonProperty("jobId")
    public double getJobId() {
        return jobId;
    }

    /**
     * 
     * @param jobId
     *     The jobId
     */
    @JsonProperty("jobId")
    public void setJobId(double jobId) {
        this.jobId = jobId;
    }

    /**
     * 
     * @return
     *     The jvmArguments
     */
    @JsonProperty("jvmArguments")
    public List<String> getJvmArguments() {
        return jvmArguments;
    }

    /**
     * 
     * @param jvmArguments
     *     The jvmArguments
     */
    @JsonProperty("jvmArguments")
    public void setJvmArguments(List<String> jvmArguments) {
        this.jvmArguments = jvmArguments;
    }

    /**
     * 
     * @return
     *     The numberOfExecutionLeft
     */
    @JsonProperty("numberOfExecutionLeft")
    public double getNumberOfExecutionLeft() {
        return numberOfExecutionLeft;
    }

    /**
     * 
     * @param numberOfExecutionLeft
     *     The numberOfExecutionLeft
     */
    @JsonProperty("numberOfExecutionLeft")
    public void setNumberOfExecutionLeft(double numberOfExecutionLeft) {
        this.numberOfExecutionLeft = numberOfExecutionLeft;
    }

    /**
     * 
     * @return
     *     The numberOfExecutionOnFailureLeft
     */
    @JsonProperty("numberOfExecutionOnFailureLeft")
    public double getNumberOfExecutionOnFailureLeft() {
        return numberOfExecutionOnFailureLeft;
    }

    /**
     * 
     * @param numberOfExecutionOnFailureLeft
     *     The numberOfExecutionOnFailureLeft
     */
    @JsonProperty("numberOfExecutionOnFailureLeft")
    public void setNumberOfExecutionOnFailureLeft(double numberOfExecutionOnFailureLeft) {
        this.numberOfExecutionOnFailureLeft = numberOfExecutionOnFailureLeft;
    }

    /**
     * 
     * @return
     *     The preciousLogs
     */
    @JsonProperty("preciousLogs")
    public boolean isPreciousLogs() {
        return preciousLogs;
    }

    /**
     * 
     * @param preciousLogs
     *     The preciousLogs
     */
    @JsonProperty("preciousLogs")
    public void setPreciousLogs(boolean preciousLogs) {
        this.preciousLogs = preciousLogs;
    }

    /**
     * 
     * @return
     *     The preciousResult
     */
    @JsonProperty("preciousResult")
    public boolean isPreciousResult() {
        return preciousResult;
    }

    /**
     * 
     * @param preciousResult
     *     The preciousResult
     */
    @JsonProperty("preciousResult")
    public void setPreciousResult(boolean preciousResult) {
        this.preciousResult = preciousResult;
    }

    /**
     * 
     * @return
     *     The restartMode
     */
    @JsonProperty("restartMode")
    public String getRestartMode() {
        return restartMode;
    }

    /**
     * 
     * @param restartMode
     *     The restartMode
     */
    @JsonProperty("restartMode")
    public void setRestartMode(String restartMode) {
        this.restartMode = restartMode;
    }

    /**
     * 
     * @return
     *     The resultPreview
     */
    @JsonProperty("resultPreview")
    public String getResultPreview() {
        return resultPreview;
    }

    /**
     * 
     * @param resultPreview
     *     The resultPreview
     */
    @JsonProperty("resultPreview")
    public void setResultPreview(String resultPreview) {
        this.resultPreview = resultPreview;
    }

    /**
     * 
     * @return
     *     The runAsMe
     */
    @JsonProperty("runAsMe")
    public boolean isRunAsMe() {
        return runAsMe;
    }

    /**
     * 
     * @param runAsMe
     *     The runAsMe
     */
    @JsonProperty("runAsMe")
    public void setRunAsMe(boolean runAsMe) {
        this.runAsMe = runAsMe;
    }

    /**
     * 
     * @return
     *     The scheduledTime
     */
    @JsonProperty("scheduledTime")
    public double getScheduledTime() {
        return scheduledTime;
    }

    /**
     * 
     * @param scheduledTime
     *     The scheduledTime
     */
    @JsonProperty("scheduledTime")
    public void setScheduledTime(double scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    /**
     * 
     * @return
     *     The tag
     */
    @JsonProperty("tag")
    public String getTag() {
        return tag;
    }

    /**
     * 
     * @param tag
     *     The tag
     */
    @JsonProperty("tag")
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * 
     * @return
     *     The walltime
     */
    @JsonProperty("walltime")
    public double getWalltime() {
        return walltime;
    }

    /**
     * 
     * @param walltime
     *     The walltime
     */
    @JsonProperty("walltime")
    public void setWalltime(double walltime) {
        this.walltime = walltime;
    }

    /**
     * 
     * @return
     *     The workingDir
     */
    @JsonProperty("workingDir")
    public String getWorkingDir() {
        return workingDir;
    }

    /**
     * 
     * @param workingDir
     *     The workingDir
     */
    @JsonProperty("workingDir")
    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
