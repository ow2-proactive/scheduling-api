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
import java.util.stream.Collectors;

import org.ow2.proactive.scheduling.api.fetchers.GenericInformationDataFetcher;
import org.ow2.proactive.scheduling.api.fetchers.VariableDataFetcher;
import org.ow2.proactive.scheduling.api.schema.type.enums.JobPriority;
import org.ow2.proactive.scheduling.api.schema.type.inputs.GenericInformationInput;
import org.ow2.proactive.scheduling.api.schema.type.inputs.VariableInput;
import org.ow2.proactive.scheduling.api.schema.type.interfaces.KeyValue;

import com.google.common.collect.ImmutableList;

import graphql.annotations.GraphQLConnection;
import graphql.annotations.GraphQLDataFetcher;
import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;
import graphql.annotations.GraphQLNonNull;
import graphql.annotations.GraphQLType;
import graphql.schema.DataFetchingEnvironment;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


/**
 * @author ActiveEon Team
 */
@Builder
@Getter
@ToString
@GraphQLType
public class Job {

    @GraphQLField
    @GraphQLNonNull
    private long id;

    @GraphQLField
    private String name;

    @GraphQLField
    private JobPriority priority;

    @GraphQLField
    private String owner;

    @GraphQLField
    private String projectName;

    @GraphQLField
    @GraphQLConnection
    private List<Task> tasks;

    @GraphQLField
    private long startTime;

    @GraphQLField
    private long inErrorTime;

    @GraphQLField
    private long finishedTime;

    @GraphQLField
    private long submittedTime;

    @GraphQLField
    private long removedTime;

    @GraphQLField
    private int totalNumberOfTasks;

    @GraphQLField
    private int numberOfPendingTasks;

    @GraphQLField
    private int numberOfRunningTasks;

    @GraphQLField
    private int numberOfFinishedTasks;

    @GraphQLField
    private int numberOfFailedTasks;

    @GraphQLField
    private int numberOfFaultyTasks;

    @GraphQLField
    private int numberOfInErrorTasks;

    @GraphQLField
    public List<GenericInformation> genericInformation(DataFetchingEnvironment dataFetchingEnvironment,
            @GraphQLName("input") GenericInformationInput input) {
        return (List<GenericInformation>) new GenericInformationDataFetcher().get(dataFetchingEnvironment);
    }

    @GraphQLField
    public List<Variable> variables(DataFetchingEnvironment dataFetchingEnvironment,
            @GraphQLName("input") VariableInput input) {
        return (List<Variable>) new VariableDataFetcher().get(dataFetchingEnvironment);
    }

    private <T extends KeyValue> List<T> filterKeyValue(List<T> entries, @GraphQLName("key") String key,
            @GraphQLName("value") String value) {
        if (entries == null) {
            return ImmutableList.of();
        }

        if (key == null && value == null) {
            return entries;
        } else if (key == null && value != null) {
            return entries.stream().filter(v -> value.equals(v.getValue())).collect(Collectors.toList());
        } else if (key != null && value == null) {
            return entries.stream().filter(v -> key.equals(v.getKey())).collect(Collectors.toList());
        } else {
            return entries.stream().filter(v -> key.equals(v.getKey()) && value.equals(v.getValue()))
                    .collect(Collectors.toList());
        }
    }

}
