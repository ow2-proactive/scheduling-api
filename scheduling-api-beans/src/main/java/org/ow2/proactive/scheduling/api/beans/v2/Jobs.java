
package org.ow2.proactive.scheduling.api.beans.v2;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

})
public class Jobs
    extends Connection
    implements Serializable
{

    private final static long serialVersionUID = -4399345069645005472L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
