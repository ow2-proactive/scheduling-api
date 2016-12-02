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
package org.ow2.proactive.scheduling.api.graphql.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ow2.proactive.scheduling.api.graphql.common.GraphqlContext;
import org.ow2.proactive.scheduling.api.graphql.fetchers.GenericInformationDataFetcher;
import org.ow2.proactive.scheduling.api.graphql.fetchers.JobDataFetcher;
import org.ow2.proactive.scheduling.api.graphql.fetchers.TaskDataFetcher;
import org.ow2.proactive.scheduling.api.graphql.fetchers.UserDataFetcher;
import org.ow2.proactive.scheduling.api.graphql.fetchers.VariablesDataFetcher;
import org.ow2.proactive.scheduling.api.graphql.schema.type.Query;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableMap;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


/**
 * @author ActiveEon Team
 */
@Log4j2
@Service
public class GraphqlService {

    private static final String INTROSPECTION_QUERY = "\n  query IntrospectionQuery {\n    __schema {\n      queryType { name }\n      mutationType { name }\n      types {\n        ...FullType\n      }\n      directives {\n        name\n        description\n        locations\n        args {\n          ...InputValue\n        }\n      }\n    }\n  }\n\n  fragment FullType on __Type {\n    kind\n    name\n    description\n    fields(includeDeprecated: true) {\n      name\n      description\n      args {\n        ...InputValue\n      }\n      type {\n        ...TypeRef\n      }\n      isDeprecated\n      deprecationReason\n    }\n    inputFields {\n      ...InputValue\n    }\n    interfaces {\n      ...TypeRef\n    }\n    enumValues(includeDeprecated: true) {\n      name\n      description\n      isDeprecated\n      deprecationReason\n    }\n    possibleTypes {\n      ...TypeRef\n    }\n  }\n\n  fragment InputValue on __InputValue {\n    name\n    description\n    type { ...TypeRef }\n    defaultValue\n  }\n\n  fragment TypeRef on __Type {\n    kind\n    name\n    ofType {\n      kind\n      name\n      ofType {\n        kind\n        name\n        ofType {\n          kind\n          name\n          ofType {\n            kind\n            name\n            ofType {\n              kind\n              name\n              ofType {\n                kind\n                name\n                ofType {\n                  kind\n                  name\n                }\n              }\n            }\n          }\n        }\n      }\n    }\n  }\n";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private GraphQL graphql;

    public GraphqlService() throws IllegalAccessException, NoSuchMethodException, InstantiationException {
    }

    @PostConstruct
    public void init() {
        Supplier<EntityManager> entityManagerSupplier =
                () -> ApplicationContextProvider.getApplicationContext().getBean(EntityManager.class);

        DataFetcher genericInformationDataFetcher = new GenericInformationDataFetcher();
        DataFetcher jobDataFetcher = new JobDataFetcher(entityManagerSupplier);
        DataFetcher taskDataFetcher = new TaskDataFetcher(entityManagerSupplier);
        DataFetcher userDataFetcher = new UserDataFetcher();
        DataFetcher variableDataFetcher = new VariablesDataFetcher();

        graphql = new GraphQL(
                GraphQLSchema.newSchema().query(
                        Query.TYPE.getInstance(genericInformationDataFetcher, jobDataFetcher, taskDataFetcher,
                                userDataFetcher, variableDataFetcher, userDataFetcher)).build());
    }

    public Map<String, Object> executeQuery(String query, String operationName,
            GraphqlContext graphqlContext, Map<String, Object> variables) {

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
