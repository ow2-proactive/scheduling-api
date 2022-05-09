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
package org.ow2.proactive.scheduling.api.graphql.fetchers;

import java.util.stream.Collectors;

import org.ow2.proactive.authentication.UserData;
import org.ow2.proactive.scheduling.api.graphql.common.GraphqlContext;
import org.ow2.proactive.scheduling.api.graphql.schema.type.User;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;


/**
 * @author ActiveEon Team
 */
@Component
public class UserDataFetcher implements DataFetcher {

    @Override
    public Object get(DataFetchingEnvironment environment) {
        GraphqlContext context = (GraphqlContext) environment.getContext();
        UserData userData = context.getUserData();
        return User.builder()
                   .login(userData.getUserName())
                   .groups(userData.getGroups() != null ? userData.getGroups().stream().collect(Collectors.toList())
                                                        : null)
                   .tenant(userData.getTenant())
                   .filterByTenant(userData.isFilterByTenant())
                   .allTenantPermission(userData.isAllTenantPermission())
                   .sessionId(context.getSessionId())
                   .build();
    }

}
