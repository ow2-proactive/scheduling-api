
package org.ow2.proactive.scheduling.api.beans;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "globalSpaceUrl",
    "inputSpaceUrl",
    "outputSpaceUrl",
    "userSpaceUrl"
})
public class GraphqlDataManagement implements Serializable
{

    /**
     * 
     */
    @JsonProperty("globalSpaceUrl")
    private String globalSpaceUrl;
    /**
     * 
     */
    @JsonProperty("inputSpaceUrl")
    private String inputSpaceUrl;
    /**
     * 
     */
    @JsonProperty("outputSpaceUrl")
    private String outputSpaceUrl;
    /**
     * 
     */
    @JsonProperty("userSpaceUrl")
    private String userSpaceUrl;
    private final static long serialVersionUID = -2502715117140713034L;

    /**
     * 
     * @return
     *     The globalSpaceUrl
     */
    @JsonProperty("globalSpaceUrl")
    public String getGlobalSpaceUrl() {
        return globalSpaceUrl;
    }

    /**
     * 
     * @param globalSpaceUrl
     *     The globalSpaceUrl
     */
    @JsonProperty("globalSpaceUrl")
    public void setGlobalSpaceUrl(String globalSpaceUrl) {
        this.globalSpaceUrl = globalSpaceUrl;
    }

    /**
     * 
     * @return
     *     The inputSpaceUrl
     */
    @JsonProperty("inputSpaceUrl")
    public String getInputSpaceUrl() {
        return inputSpaceUrl;
    }

    /**
     * 
     * @param inputSpaceUrl
     *     The inputSpaceUrl
     */
    @JsonProperty("inputSpaceUrl")
    public void setInputSpaceUrl(String inputSpaceUrl) {
        this.inputSpaceUrl = inputSpaceUrl;
    }

    /**
     * 
     * @return
     *     The outputSpaceUrl
     */
    @JsonProperty("outputSpaceUrl")
    public String getOutputSpaceUrl() {
        return outputSpaceUrl;
    }

    /**
     * 
     * @param outputSpaceUrl
     *     The outputSpaceUrl
     */
    @JsonProperty("outputSpaceUrl")
    public void setOutputSpaceUrl(String outputSpaceUrl) {
        this.outputSpaceUrl = outputSpaceUrl;
    }

    /**
     * 
     * @return
     *     The userSpaceUrl
     */
    @JsonProperty("userSpaceUrl")
    public String getUserSpaceUrl() {
        return userSpaceUrl;
    }

    /**
     * 
     * @param userSpaceUrl
     *     The userSpaceUrl
     */
    @JsonProperty("userSpaceUrl")
    public void setUserSpaceUrl(String userSpaceUrl) {
        this.userSpaceUrl = userSpaceUrl;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
