package org.ow2.proactive.scheduling.api.schema.type.inputs;

import java.util.HashMap;

import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;
import graphql.schema.GraphQLInputType;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class KeyValueInput implements GraphQLInputType {

    private static final String KEY_FIELD_NAME = "key";

    private static final String VALUE_FIELD_NAME = "value";

    @GraphQLName(KEY_FIELD_NAME)
    @GraphQLField
    private String key;

    @GraphQLName(VALUE_FIELD_NAME)
    @GraphQLField
    private String value;

    public KeyValueInput(HashMap input) {
        if (input != null) {
            key = (String) input.get(KEY_FIELD_NAME);
            value = (String) input.get(VALUE_FIELD_NAME);
        }
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

}
