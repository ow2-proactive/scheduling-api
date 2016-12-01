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

package org.ow2.proactive.scheduling.api.controllers;


import java.util.Map;

import org.ow2.proactive.scheduling.api.graphql.common.GraphqlContext;
import org.ow2.proactive.scheduling.api.graphql.service.AuthenticationService;
import org.ow2.proactive.scheduling.api.graphql.service.GraphqlService;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebAppConfiguration
public class GraphQLControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private GraphqlService graphqlService;

    @InjectMocks
    private GraphQLController graphQLController;

    private MockMvc mockMvc;

    private String query = "{ \"query\": \"{ jobs{ edges{ node{ id } } } } \" }";

    @Before
    public void setup() {
        initMocks(this);

        when(authenticationService.authenticate(any(String.class))).thenReturn("bobot");

        when(graphqlService.executeQuery(any(String.class), any(String.class),
                any(GraphqlContext.class), any(Map.class))).thenReturn(ImmutableMap.of());


        mockMvc = standaloneSetup(graphQLController).build();
    }

    @Test
    public void testControllerPostMethod() throws Exception {

        mockMvc.perform(post("/v1/graphql").header("sessionid", "sessionId").accept(
                MediaType.APPLICATION_JSON).contentType(
                MediaType.APPLICATION_JSON).content(query)).andExpect(status().isOk()).andExpect(
                content().contentType(MediaType.APPLICATION_JSON));

        verify(authenticationService, times(1)).authenticate(any(String.class));

        verify(graphqlService, times(1)).executeQuery(any(String.class), any(String.class),
                any(GraphqlContext.class), any(Map.class));
    }

    @Test
    public void testControllerGetMethod() throws Exception {

        mockMvc.perform(
                get("/v1/graphql").header("sessionid", "sessionId").param("query",
                        "{ jobs{ edges{ node{ id } } } }").accept(
                        MediaType.APPLICATION_JSON).contentType(
                        MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(
                content().contentType(MediaType.APPLICATION_JSON));

        verify(authenticationService, times(1)).authenticate(any(String.class));

        verify(graphqlService, times(1)).executeQuery(any(String.class), any(String.class),
                any(GraphqlContext.class), any(Map.class));
    }

}