
package org.ow2.proactive.scheduling.api.beans;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "endCursor",
    "hasNextPage",
    "hasPreviousPage",
    "startCursor"
})
public class GraphqlPageInfo implements Serializable
{

    /**
     * 
     */
    @JsonProperty("endCursor")
    private String endCursor;
    /**
     * 
     */
    @JsonProperty("hasNextPage")
    private boolean hasNextPage;
    /**
     * 
     */
    @JsonProperty("hasPreviousPage")
    private boolean hasPreviousPage;
    /**
     * 
     */
    @JsonProperty("startCursor")
    private String startCursor;
    private final static long serialVersionUID = 4275821745396689389L;

    /**
     * 
     * @return
     *     The endCursor
     */
    @JsonProperty("endCursor")
    public String getEndCursor() {
        return endCursor;
    }

    /**
     * 
     * @param endCursor
     *     The endCursor
     */
    @JsonProperty("endCursor")
    public void setEndCursor(String endCursor) {
        this.endCursor = endCursor;
    }

    /**
     * 
     * @return
     *     The hasNextPage
     */
    @JsonProperty("hasNextPage")
    public boolean isHasNextPage() {
        return hasNextPage;
    }

    /**
     * 
     * @param hasNextPage
     *     The hasNextPage
     */
    @JsonProperty("hasNextPage")
    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    /**
     * 
     * @return
     *     The hasPreviousPage
     */
    @JsonProperty("hasPreviousPage")
    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    /**
     * 
     * @param hasPreviousPage
     *     The hasPreviousPage
     */
    @JsonProperty("hasPreviousPage")
    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    /**
     * 
     * @return
     *     The startCursor
     */
    @JsonProperty("startCursor")
    public String getStartCursor() {
        return startCursor;
    }

    /**
     * 
     * @param startCursor
     *     The startCursor
     */
    @JsonProperty("startCursor")
    public void setStartCursor(String startCursor) {
        this.startCursor = startCursor;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
