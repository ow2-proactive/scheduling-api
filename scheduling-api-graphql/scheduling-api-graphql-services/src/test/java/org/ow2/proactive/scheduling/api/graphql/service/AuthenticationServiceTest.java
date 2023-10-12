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
package org.ow2.proactive.scheduling.api.graphql.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ow2.proactive.authentication.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


/**
 * @author ActiveEon Team
 * @since 31/05/2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AuthenticationServiceTestConfig.class, initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles("test")
@PropertySource("classpath:application-test.properties")
public class AuthenticationServiceTest {

    private static final UserData CONTEXT_USER_DATA = new UserData();

    static {
        CONTEXT_USER_DATA.setUserName("bobot");
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    public void testAuthenticate() throws Exception {
        when(restTemplate.getForObject(any(String.class), eq(UserData.class))).thenReturn(CONTEXT_USER_DATA);
        authenticationService.authenticate("sessionId");
        // first time, call resttemplate to get the login name
        verify(restTemplate, times(1)).getForObject(any(String.class), eq(UserData.class));
        authenticationService.authenticate("sessionId");
        // second time, should not call resttemplate to get the login name again
        verify(restTemplate, times(1)).getForObject(any(String.class), eq(UserData.class));
    }

    @Test
    public void testAuthenticateMissingSessionId() {
        when(restTemplate.getForObject(any(String.class), eq(UserData.class))).thenReturn(null);
        try {
            authenticationService.authenticate("sessionId");
        } catch (Exception e) {
            assertThat(e.getMessage().contains("Missing session ID"), is(true));
        }
    }

    @Test
    public void testAuthenticateInvalidSessionId() {
        HttpClientErrorException e = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        when(restTemplate.getForObject(any(String.class), eq(UserData.class))).thenThrow(e);
        try {
            authenticationService.authenticate("sessionId");
        } catch (Exception ex) {
            assertThat(ex.getMessage().contains("session ID is invalid"), is(true));
        }
    }

}
