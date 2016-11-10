/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2015 INRIA/University of
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
package org.ow2.proactive.scheduling.api.fetchers;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.ow2.proactive.scheduling.api.schema.type.User;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class UserDataFetcher implements DataFetcher {

    @Value("${pa.scheduler.rest.url}")
    private String schedulerRestUrl;

    @Value("${api.viewer.localcache.expire.min}")
    private Integer localCacheExpireMin;

    private RestTemplate restTemplate = new RestTemplate();

    private String loginFetchUrl;

    private LoadingCache<String, User> localCache ;

    @PostConstruct
    protected void init() {
        loginFetchUrl = schedulerRestUrl + "/scheduler/logins/sessionid/";

        localCache = CacheBuilder.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(localCacheExpireMin, TimeUnit.MINUTES)
                .build(new CacheLoader<String, User>() {
                    public User load(String sessionId)
                            throws Exception {
                        return getLoginFromSessionId(sessionId);
                    }

                });
    }

    @Override
    public Object get(DataFetchingEnvironment environment) {

        String sessionId = environment.getArgument("sessionId");

        try {
            return localCache.get(sessionId);
        } catch (Exception e) {
            return User.builder().build();
        }
    }

    private User getLoginFromSessionId(String sessionId) throws RuntimeException {

        String login = restTemplate.getForObject(loginFetchUrl + sessionId, String.class);

        if(StringUtils.isNotBlank(login)) {
            return User.builder().sessionId(sessionId).login(login).build();
        }
        throw new RuntimeException("session id does not exist");
    }

}
