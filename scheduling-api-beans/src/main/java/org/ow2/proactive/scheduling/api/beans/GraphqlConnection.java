
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
    "edges",
    "pageInfo"
})
public class GraphqlConnection implements Serializable
{

    /**
     * 
     */
    @JsonProperty("edges")
    private List<GraphqlEdge> edges = new ArrayList<GraphqlEdge>();
    /**
     * 
     */
    @JsonProperty("pageInfo")
    private GraphqlPageInfo pageInfo;
    private final static long serialVersionUID = 233678541247033895L;

    /**
     * 
     * @return
     *     The edges
     */
    @JsonProperty("edges")
    public List<GraphqlEdge> getEdges() {
        return edges;
    }

    /**
     * 
     * @param edges
     *     The edges
     */
    @JsonProperty("edges")
    public void setEdges(List<GraphqlEdge> edges) {
        this.edges = edges;
    }

    /**
     * 
     * @return
     *     The pageInfo
     */
    @JsonProperty("pageInfo")
    public GraphqlPageInfo getPageInfo() {
        return pageInfo;
    }

    /**
     * 
     * @param pageInfo
     *     The pageInfo
     */
    @JsonProperty("pageInfo")
    public void setPageInfo(GraphqlPageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
