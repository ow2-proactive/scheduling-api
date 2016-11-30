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
package org.ow2.proactive.scheduling.api.graphql.service;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.ow2.proactive.scheduling.api.graphql.service.exceptions.InvalidSessionIdException;
import org.ow2.proactive.scheduling.api.graphql.service.exceptions.MissingSessionIdException;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author ActiveEon Team
 */
@Service
public class AuthenticationService {

    @Value("${pa.scheduler.rest.url}")
    private String schedulerRestUrl;

    private String schedulerLoginFetchUrl;

    @Value("${pa.scheduling.api.session_cache.expire_after}")
    private int sessionCacheExpireAfter;

    @Value("${pa.scheduling.api.session_cache.max_size}")
    private int sessionCacheMaxSize;

    @Autowired
    private RestTemplate restTemplate;

    private LoadingCache<String, String> sessionCache;

    @PostConstruct
    protected void init() {

        schedulerLoginFetchUrl = createLoginFetchUrl(schedulerRestUrl);

        sessionCache = CacheBuilder.newBuilder()
                .maximumSize(sessionCacheMaxSize)
                .expireAfterWrite(sessionCacheExpireAfter, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String sessionId)
                            throws Exception {
                        return getLoginFromSessionId(sessionId);
                    }
                });
    }

    private String createLoginFetchUrl(String schedulerRestUrl) {
        String result = schedulerRestUrl;

        if (!schedulerRestUrl.endsWith("/")) {
            result += '/';
        }

        result += "scheduler/logins/sessionid/";

        return result;
    }

    public String authenticate(String sessionId) throws InvalidSessionIdException {
        return sessionCache.getUnchecked(sessionId);
    }

    private String getLoginFromSessionId(String sessionId) {
        try {
            String login = restTemplate.getForObject(schedulerLoginFetchUrl + sessionId, String.class);

            if (!Strings.isNullOrEmpty(login)) {
                return login;
            }

            throw new MissingSessionIdException();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new InvalidSessionIdException();
            } else {
                throw e;
            }
        }
    }

}
