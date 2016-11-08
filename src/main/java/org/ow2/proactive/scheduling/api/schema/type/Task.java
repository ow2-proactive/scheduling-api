/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2016 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 *  * $$ACTIVEEON_INITIAL_DEV$$
 */
package org.ow2.proactive.scheduling.api.schema.type;

import java.util.List;
import java.util.Map;

import org.ow2.proactive.scheduling.api.schema.type.enums.TaskStatus;
import org.ow2.proactive.scheduling.api.schema.type.inputs.GenericInformationInput;
import org.ow2.proactive.scheduling.api.schema.type.inputs.VariableInput;
import graphql.annotations.GraphQLConnection;
import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;
import graphql.annotations.GraphQLType;
import graphql.schema.DataFetchingEnvironment;
import lombok.Builder;
import lombok.Getter;

import static org.ow2.proactive.scheduling.api.util.KeyValues.filterKeyValue;


/**
 * @author ActiveEon Team
 */
@Builder
@Getter
@GraphQLType
public class Task {

    @GraphQLField
    private long id;

    @GraphQLField
    private long jobId;

    @GraphQLField
    private String name;

    @GraphQLField
    private String description;

    @GraphQLField
    private String tag;

    @GraphQLField
    private TaskStatus status;

    @GraphQLField
    private long executionDuration = -1;

    @GraphQLField
    private String executionHostName;

    @GraphQLField
    private long inErrorTime = -1;

    @GraphQLField
    private long finishedTime = -1;

    @GraphQLField
    private long scheduledTime = -1;

    @GraphQLField
    private int numberOfExecutionLeft = 1;

    @GraphQLField
    private int numberOfExecutionOnFailureLeft = 1;

    @GraphQLField
    private int progress;

    @GraphQLField
    private long startTime = -1;

    private Map<String, String> genericInformation;

    private Map<String, String> variables;

    @GraphQLField
    public List<GenericInformation> genericInformation(DataFetchingEnvironment dataFetchingEnvironment,
            @GraphQLName("input") GenericInformationInput input) {

        Task task = (Task) dataFetchingEnvironment.getSource();
        return filterKeyValue(task.getGenericInformation(), input, () -> new GenericInformation());
    }

    @GraphQLField
    public List<Variable> variables(DataFetchingEnvironment dataFetchingEnvironment,
            @GraphQLName("input") VariableInput input) {

        Task task = (Task) dataFetchingEnvironment.getSource();
        return filterKeyValue(task.getVariables(), input, () -> new Variable());
    }

}
