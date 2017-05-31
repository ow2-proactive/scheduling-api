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

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.ow2.proactive.scheduling.api.graphql.common.GraphqlContext;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableMap;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import lombok.extern.log4j.Log4j2;


/**
 * @author ActiveEon Team
 */
@Log4j2
@Service
public class GraphqlService {

    private static final String INTROSPECTION_QUERY = "\n  query IntrospectionQuery {\n    __schema {\n      queryType { name }\n      mutationType { name }\n      types {\n        ...FullType\n      }\n      directives {\n        name\n        description\n        locations\n        args {\n          ...InputValue\n        }\n      }\n    }\n  }\n\n  fragment FullType on __Type {\n    kind\n    name\n    description\n    fields(includeDeprecated: true) {\n      name\n      description\n      args {\n        ...InputValue\n      }\n      type {\n        ...TypeRef\n      }\n      isDeprecated\n      deprecationReason\n    }\n    inputFields {\n      ...InputValue\n    }\n    interfaces {\n      ...TypeRef\n    }\n    enumValues(includeDeprecated: true) {\n      name\n      description\n      isDeprecated\n      deprecationReason\n    }\n    possibleTypes {\n      ...TypeRef\n    }\n  }\n\n  fragment InputValue on __InputValue {\n    name\n    description\n    type { ...TypeRef }\n    defaultValue\n  }\n\n  fragment TypeRef on __Type {\n    kind\n    name\n    ofType {\n      kind\n      name\n      ofType {\n        kind\n        name\n        ofType {\n          kind\n          name\n          ofType {\n            kind\n            name\n            ofType {\n              kind\n              name\n              ofType {\n                kind\n                name\n                ofType {\n                  kind\n                  name\n                }\n              }\n            }\n          }\n        }\n      }\n    }\n  }\n";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private GraphQL graphql;

    @Autowired
    @Qualifier("genericInformationDataFetcher")
    private DataFetcher genericInformationDataFetcher;

    @Autowired
    @Qualifier("jobDataFetcher")
    private DataFetcher jobDataFetcher;

    @Autowired
    @Qualifier("taskDataFetcher")
    private DataFetcher taskDataFetcher;

    @Autowired
    @Qualifier("userDataFetcher")
    private DataFetcher userDataFetcher;

    @Autowired
    @Qualifier("variablesDataFetcher")
    private DataFetcher variablesDataFetcher;

    @PostConstruct
    public void init() {
        graphql = new GraphQL(GraphQLSchema.newSchema()
                                           .query(Query.TYPE.getInstance(genericInformationDataFetcher,
                                                                         jobDataFetcher,
                                                                         taskDataFetcher,
                                                                         userDataFetcher,
                                                                         variablesDataFetcher,
                                                                         userDataFetcher))
                                           .build());
    }

    public Map<String, Object> executeQuery(String query, String operationName, GraphqlContext graphqlContext,
            Map<String, Object> variables) {

        if (variables == null) {
            variables = ImmutableMap.of();
        }

        ExecutionResult executionResult = graphql.execute(query, operationName, graphqlContext, variables);

        Map<String, Object> result = new LinkedHashMap<>();

        if (!executionResult.getErrors().isEmpty()) {
            result.put("errors", executionResult.getErrors());
            log.error("Errors: {}", executionResult.getErrors());
        }

        result.put("data", executionResult.getData());

        return result;
    }

    public String generateJsonSchema() throws JsonProcessingException {
        ExecutionResult result = graphql.execute(INTROSPECTION_QUERY);
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        return MAPPER.writeValueAsString(result);
    }

}
