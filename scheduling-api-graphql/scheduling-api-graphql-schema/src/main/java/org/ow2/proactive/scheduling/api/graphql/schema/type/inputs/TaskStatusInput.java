/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package org.ow2.proactive.scheduling.api.graphql.schema.type.inputs;

import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.STATUS;

import java.util.List;

import org.ow2.proactive.scheduling.api.graphql.schema.type.TypeSingleton;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLList;
import lombok.Data;
import lombok.Getter;


/**
 * @author ActiveEon Team
 * @since 2019-01-24
 */
@Data
@Getter
public class TaskStatusInput {

    public final static GraphQLEnumType TaskStatusEnum = GraphQLEnumType.newEnum()
                                                                        .name(STATUS.getName())
                                                                        .description("Status")
                                                                        .value("ABORTED")
                                                                        .value("FAILED")
                                                                        .value("FAULTY")
                                                                        .value("FINISHED")
                                                                        .value("IN_ERROR")
                                                                        .value("NOT_RESTARTED")
                                                                        .value("NOT_STARTED")
                                                                        .value("PAUSED")
                                                                        .value("PENDING")
                                                                        .value("RUNNING")
                                                                        .value("SKIPPED")
                                                                        .value("SUBMITTED")
                                                                        .value("WAITING_ON_ERROR")
                                                                        .value("WAITING_ON_FAILURE")
                                                                        .build();

    public final static TypeSingleton<GraphQLInputType> TYPE = new TypeSingleton<GraphQLInputType>() {
        @Override
        public GraphQLInputType buildType(DataFetcher... dataFetchers) {
            return newInputObject().name(STATUS.getName())
                                   .description("Status")
                                   .field(newInputObjectField().name(STATUS.getName())
                                                               .description("Status")
                                                               .type(new GraphQLList(TaskStatusEnum))
                                                               .build())
                                   .build();
        }
    };

    private final List<String> status;

    public TaskStatusInput(List<String> input) {
        status = input;
    }

}
