package org.ow2.proactive.scheduling.api.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ow2.proactive.scheduling.api.service.GraphqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;


/**
 * This controller follows the basics from the GraphQL spec:
 *
 * http://graphql.org/learn/serving-over-http/
 *
 * @author ActiveEon Team
 */
@Controller
@RequestMapping(value = "/graphql", produces = MediaType.APPLICATION_JSON_VALUE)
public class GraphQLController {

    public static final String DEFAULT_OPERATION_NAME = "operationName";

    public static final String DEFAULT_QUERY_KEY = "query";

    public static final String DEFAULT_VARIABLES_KEY = "variables";

    @Autowired
    private GraphqlService graphqlService;

    private ObjectMapper objectMapper = new ObjectMapper();

    /*
     * http://graphql.org/learn/serving-over-http/#get-request
     */
    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> executeOperation(@RequestParam(DEFAULT_QUERY_KEY) String query,
            @RequestParam(value = DEFAULT_OPERATION_NAME, required = false) String operationName,
            @RequestParam(value = DEFAULT_VARIABLES_KEY, required = false) String variables)
            throws IOException {

        return graphqlService.executeQuery(query, operationName, decodeIntoMap(variables));
    }

    private Map<String, Object> decodeIntoMap(final String variables) throws IOException {
        if (variables == null) {
            return new HashMap<>();
        }

        return objectMapper.readValue(variables, Map.class);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> executeOperation(@RequestBody Map<String, Object> body) {
        String query = (String) body.get(DEFAULT_QUERY_KEY);

        return graphqlService.executeQuery(query, query, Maps.newHashMap());
    }

    @RequestMapping(method = RequestMethod.GET)
    public String executeOperation() {
        return "/index.html";
    }

}
