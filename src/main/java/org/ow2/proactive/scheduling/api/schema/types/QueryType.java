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
package org.ow2.proactive.scheduling.api.schema.types;

import java.util.List;

import com.google.common.collect.ImmutableList;

import graphql.annotations.GraphQLConnection;
import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLType;


/**
 * @author ActiveEon Team
 */
@GraphQLType
public class QueryType {

    @GraphQLField
    @GraphQLConnection
    public List<JobType> jobs() {
        JobType job1 = JobType.builder().id(1).name("Super").build();
        JobType job2 = JobType.builder().id(2).name("Cool").build();
        return ImmutableList.of(job1, job2);
    }

    @GraphQLField
    public String version() {
        return "42";
    }

}
