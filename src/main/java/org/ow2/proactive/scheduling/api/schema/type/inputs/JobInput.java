package org.ow2.proactive.scheduling.api.schema.type.inputs;

import java.util.HashMap;

import org.ow2.proactive.scheduling.api.schema.type.enums.JobPriority;

import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;
import graphql.schema.GraphQLInputType;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class JobInput implements GraphQLInputType {

    private static final String ID_FIELD_NAME = "id";
    private static final String JOB_NAME_FIELD_NAME = "name";
    private static final String PRIORITY_FIELD_NAME = "priority";
    private static final String OWNER_FIELD_NAME = "owner";
    private static final String PROJ_NAME_FIELD_NAME = "projectName";

    @GraphQLField
    @GraphQLName(ID_FIELD_NAME)
    private long id;

    @GraphQLField
    @GraphQLName(JOB_NAME_FIELD_NAME)
    private String jobName;

    @GraphQLField
    @GraphQLName(PRIORITY_FIELD_NAME)
    private JobPriority priority;

    @GraphQLField
    @GraphQLName(OWNER_FIELD_NAME)
    private String owner;

    @GraphQLField
    @GraphQLName(PROJ_NAME_FIELD_NAME)
    private String projectName;

    public JobInput(HashMap input) {
        if (input != null) {
            id = Long.valueOf((String) input.get(ID_FIELD_NAME));
            jobName = (String) input.get(JOB_NAME_FIELD_NAME);
            priority = JobPriority.valueOf((String) input.get(PRIORITY_FIELD_NAME));
            owner = (String) input.get(OWNER_FIELD_NAME);
            projectName = (String) input.get(PROJ_NAME_FIELD_NAME);
        }
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

}
