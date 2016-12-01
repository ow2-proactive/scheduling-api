/*
 * ProActive Parallel Suite(TM):
 * The Java(TM) library for Parallel, Distributed,
 * Multi-Core Computing for Enterprise Grids & Clouds
 *
 * Copyright (c) 2016 ActiveEon
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

package org.ow2.proactive.scheduling.api.services;

import org.ow2.proactive.scheduling.api.Application;
import org.ow2.proactive.scheduling.api.graphql.service.AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class AuthenticationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    @Autowired
    private AuthenticationService authenticationService;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void testAuthenticate() throws Exception {
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenReturn("bobot");
        authenticationService.authenticate("sessionId");
        // first time, call resttemplate to get the login name
        verify(restTemplate, times(1)).getForObject(any(String.class), eq(String.class));
        authenticationService.authenticate("sessionId");
        // second time, should not call resttemplate to get the login name again
        verify(restTemplate, times(1)).getForObject(any(String.class), eq(String.class));
    }

    @Test
    public void testAuthenticateMissingSessionId() {
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenReturn("");
        try {
            authenticationService.authenticate("sessionId");
        } catch (Exception e) {
            assertThat(e.getMessage().contains("Missing session ID"), is(true));
        }
    }

    @Test
    public void testAuthenticateInvalidSessionId() {
        HttpClientErrorException e = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenThrow(e);
        try {
            authenticationService.authenticate("sessionId");
        } catch (Exception ex) {
            assertThat(ex.getMessage().contains("session ID is invalid"), is(true));
        }
    }

}