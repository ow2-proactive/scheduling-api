
package org.ow2.proactive.scheduling.api.beans.v2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "edges",
    "pageInfo"
})
public class Connection implements Serializable
{

    /**
     * 
     */
    @JsonProperty("edges")
    private List<Edge> edges = new ArrayList<Edge>();
    /**
     * 
     */
    @JsonProperty("pageInfo")
    private PageInfo pageInfo;
    private final static long serialVersionUID = -5393374317136984371L;

    /**
     * 
     * @return
     *     The edges
     */
    @JsonProperty("edges")
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * 
     * @param edges
     *     The edges
     */
    @JsonProperty("edges")
    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    /**
     * 
     * @return
     *     The pageInfo
     */
    @JsonProperty("pageInfo")
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    /**
     * 
     * @param pageInfo
     *     The pageInfo
     */
    @JsonProperty("pageInfo")
    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
