package org.ow2.proactive.scheduling.api.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.ow2.proactive.scheduling.api.Application;
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
import static org.mockito.Mockito.*;
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
    public void testMissingSessionAuthenticate() {
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenReturn("");
        try {
            authenticationService.authenticate("sessionId");
        } catch (Exception e) {
            assertThat(e.getMessage().contains("Missiong session ID"), is(true));
        }
    }

    @Test
    public void testInvalidSessionIdAuthenticate() {
        HttpClientErrorException e = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenThrow(e);
        try {
            authenticationService.authenticate("sessionId");
        } catch (Exception ex) {
            assertThat(ex.getMessage().contains("session ID is invalid"), is(true));
        }
    }

}