
package org.ow2.proactive.scheduling.api.beans;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({

})
public class GraphqlTasks
    extends GraphqlConnection
    implements Serializable
{

    private final static long serialVersionUID = -7419180983096433260L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
