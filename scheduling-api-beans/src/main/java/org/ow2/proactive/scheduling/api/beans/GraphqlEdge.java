
package org.ow2.proactive.scheduling.api.beans;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cursor",
    "node"
})
public class GraphqlEdge implements Serializable
{

    /**
     * 
     */
    @JsonProperty("cursor")
    private String cursor;
    /**
     * 
     * Corresponds to the "node" property.
     * 
     */
    @JsonProperty("node")
    private GraphqlNode node;
    private final static long serialVersionUID = -5228033786983334047L;

    /**
     * 
     * @return
     *     The cursor
     */
    @JsonProperty("cursor")
    public String getCursor() {
        return cursor;
    }

    /**
     * 
     * @param cursor
     *     The cursor
     */
    @JsonProperty("cursor")
    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    /**
     * 
     * Corresponds to the "node" property.
     * 
     * @return
     *     The node
     */
    @JsonProperty("node")
    public GraphqlNode getNode() {
        return node;
    }

    /**
     * 
     * Corresponds to the "node" property.
     * 
     * @param node
     *     The node
     */
    @JsonProperty("node")
    public void setNode(GraphqlNode node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
