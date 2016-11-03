package org.ow2.proactive.scheduling.api.controller;

import java.util.Map;
import java.util.Optional;

import org.ow2.proactive.scheduling.api.service.GraphqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;


/**
 * @author ActiveEon Team
 */
@RestController
@RequestMapping(value = "/graphql", produces = MediaType.APPLICATION_JSON_VALUE)
public class GraphQLController {

    public static final String DEFAULT_QUERY_KEY = "query";

    public static final String DEFAULT_VARIABLES_KEY = "variables";

    @Autowired
    private GraphqlService graphqlService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> executeOperation(@RequestBody Map<String, Object> body) {
        String query = (String) body.get(DEFAULT_QUERY_KEY);

        Map<String, Object> variables = Optional
                .ofNullable((Map<String, Object>) body.get(DEFAULT_VARIABLES_KEY)).orElse(ImmutableMap.of());

        return graphqlService.executeQuery(query, variables);
    }

}
