package org.ow2.proactive.scheduling.api.controller;

import com.google.common.base.Strings;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.ow2.proactive.scheduling.api.service.GraphqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * This controller follows the basics from the GraphQL spec:
 *
 * http://graphql.org/learn/serving-over-http/
 *
 * @author ActiveEon Team
 */
@Controller
@RequestMapping(value = "/v2/graphql", produces = MediaType.APPLICATION_JSON_VALUE)
public class GraphQLController {

    private static final String DEFAULT_OPERATION_NAME = "operationName";

    private static final String DEFAULT_QUERY_KEY = "query";

    private static final String DEFAULT_VARIABLES_KEY = "variables";

    @Autowired
    private GraphqlService graphqlService;

    private ObjectMapper objectMapper = new ObjectMapper();

    /*
     * http://graphql.org/learn/serving-over-http/#get-request
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> executeOperation(
            @RequestParam(DEFAULT_QUERY_KEY) String query,
            @RequestParam(value = DEFAULT_OPERATION_NAME, required = false) String operationName,
            @RequestParam(value = DEFAULT_VARIABLES_KEY, required = false) String variables)
            throws IOException {

        return graphqlService.executeQuery(query, operationName, decodeIntoMap(variables));
    }

    /*
     * http://graphql.org/learn/serving-over-http/#post-request
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> executeOperation(@RequestBody Map<String, Object> body)
            throws IOException {

        String query = (String) body.get(DEFAULT_QUERY_KEY);
        String operationName = (String) body.get(DEFAULT_OPERATION_NAME);
        String variables = (String) body.get(DEFAULT_VARIABLES_KEY);

        return graphqlService.executeQuery(query, operationName, decodeIntoMap(variables));
    }

    private Map<String, Object> decodeIntoMap(final String variables) throws IOException {
        if (Strings.isNullOrEmpty(variables)) {
            return new HashMap<>();
        }

        return objectMapper.readValue(variables, Map.class);
    }

}
