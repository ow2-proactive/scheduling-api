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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ow2.proactive.scheduling.api.graphql.common.GraphqlContext;
import org.ow2.proactive.scheduling.api.graphql.service.AuthenticationService;
import org.ow2.proactive.scheduling.api.graphql.service.GraphqlService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * This controller follows the basics from the GraphQL spec:
 * <p>
 * http://graphql.org/learn/serving-over-http/
 *
 * @author ActiveEon Team
 */
@Controller
@Log4j2
@RequestMapping(value = "/v1/graphql", produces = MediaType.APPLICATION_JSON_VALUE)
public class GraphQLController {

    private static final String DEFAULT_OPERATION_NAME = "operationName";

    private static final String DEFAULT_QUERY_KEY = "query";

    private static final String DEFAULT_VARIABLES_KEY = "variables";

    private static final String REQUEST_HEADER_NAME_SESSION_ID = "sessionid";

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private GraphqlService graphqlService;

    private ObjectMapper objectMapper = new ObjectMapper();

    /*
     * http://graphql.org/learn/serving-over-http/#get-request
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> executeOperation(
            @RequestHeader(value = REQUEST_HEADER_NAME_SESSION_ID) String sessionId,
            @RequestParam(DEFAULT_QUERY_KEY) String query,
            @RequestParam(value = DEFAULT_OPERATION_NAME, required = false) String operationName,
            @RequestParam(value = DEFAULT_VARIABLES_KEY, required = false) String variables) throws IOException {

        log.debug("sessionId={}", sessionId);

        String username = authenticationService.authenticate(sessionId);

        log.trace("username={}", username);
        log.debug("query={}, operationName={}, variables={}", query, operationName, variables);

        return graphqlService.executeQuery(query, operationName,
                new GraphqlContext(sessionId, username), decodeIntoMap(variables));
    }

    /*
     * http://graphql.org/learn/serving-over-http/#post-request
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> executeOperation(
            @RequestHeader(value = REQUEST_HEADER_NAME_SESSION_ID) String sessionId,
            @RequestBody Map<String, Object> body) throws IOException {

        log.debug("sessionId={}", sessionId);

        String username = authenticationService.authenticate(sessionId);

        log.trace("username={}", username);

        String query = (String) body.get(DEFAULT_QUERY_KEY);
        String operationName = (String) body.get(DEFAULT_OPERATION_NAME);
        Map<String, Object> variables = (Map<String, Object>) body.get(DEFAULT_VARIABLES_KEY);

        log.debug("query={}, operationName={}, variables={}", query, operationName, variables);

        return graphqlService.executeQuery(query, operationName,
                new GraphqlContext(sessionId, username), variables);
    }

    @RequestMapping(value = "/schema", method = RequestMethod.GET)
    @ResponseBody
    public String getSchema() throws JsonProcessingException {
        return graphqlService.generateJsonSchema();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> decodeIntoMap(final String variables) throws IOException {
        if (Strings.isNullOrEmpty(variables)) {
            return new HashMap<>();
        }

        TypeReference<HashMap<String, Object>> typeRef
                = new TypeReference<HashMap<String, Object>>() {
        };

        return objectMapper.readValue(variables, typeRef);
    }

}
