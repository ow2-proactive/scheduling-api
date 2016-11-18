
package org.ow2.proactive.scheduling.api.beans;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "data"
})
public class GraphqlApiResponse implements Serializable
{

    /**
     * 
     */
    @JsonProperty("data")
    private GraphqlData data;
    private final static long serialVersionUID = 4316831535520427823L;

    /**
     * 
     * @return
     *     The data
     */
    @JsonProperty("data")
    public GraphqlData getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    @JsonProperty("data")
    public void setData(GraphqlData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
