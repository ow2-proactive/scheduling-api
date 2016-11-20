
package org.ow2.proactive.scheduling.api.beans.v2;

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
public class Edge implements Serializable
{

    /**
     * 
     */
    @JsonProperty("cursor")
    private String cursor;
    /**
     * 
     */
    @JsonProperty("node")
    private Node node;
    private final static long serialVersionUID = -3819066736083018792L;

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
     * @return
     *     The node
     */
    @JsonProperty("node")
    public Node getNode() {
        return node;
    }

    /**
     * 
     * @param node
     *     The node
     */
    @JsonProperty("node")
    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
