
package org.ow2.proactive.scheduling.api.beans.v2;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

})
public class Tasks
    extends Connection
    implements Serializable
{

    private final static long serialVersionUID = 6589288260920833630L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
