package org.ow2.proactive.scheduling.api.schema.type.interfaces;

import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLEnumType.newEnum;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;

import java.util.Map;

import org.ow2.proactive.scheduling.api.fetchers.GenericInformationDataFetcher;
import org.ow2.proactive.scheduling.api.fetchers.VariablesDataFetcher;
import org.ow2.proactive.scheduling.api.schema.type.GenericInformation;
import org.ow2.proactive.scheduling.api.schema.type.Job;
import org.ow2.proactive.scheduling.api.schema.type.Task;
import org.ow2.proactive.scheduling.api.schema.type.Variable;
import org.ow2.proactive.scheduling.api.schema.type.inputs.KeyValueInput;

import graphql.schema.GraphQLEnumType;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author ActiveEon team
 */
@NoArgsConstructor
@Data
public abstract class JobTaskCommon {

    private String description;

    private long finishedTime = -1;

    private Map<String, String> genericInformation;

    private long id;

    private long inErrorTime = -1;

    private String name;

    private String onTaskError;

    private long startTime = -1;

    private Map<String, String> variables;

    public final static GraphQLEnumType ON_TASK_ERROR = newEnum().name("OnTaskError")
            .description("Defines the behaviour that is applied on Tasks when an error occurs.")
            .value("CANCEL_JOB", "CANCEL_JOB", "Cancel job after all execution attempts")
            .value("CONTINUE_JOB_EXECUTION", "CONTINUE_JOB_EXECUTION",
                    "Continue job execution (try all execution attempts)")
            .value("NONE", "NONE", "None")
            .value("PAUSE_JOB", "PAUSE_JOB", "Suspend task after first and pause job")
            .value("PAUSE_TASK", "PAUSE_TASK", "Suspend task after first error and continue others")
            .build();

    public static final GraphQLInterfaceType TYPE = GraphQLInterfaceType.newInterface()
            .name("JobTaskCommon")
            .field(newFieldDefinition().name("description")
                    .description("description")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("finishedTime")
                    .description("Finished time")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("genericInformation")
                    .description("Generic information list, empty if there is none")
                    .type(new GraphQLList(GenericInformation.TYPE))
                    .argument(newArgument().name("input")
                            .description("Generic information input filter")
                            .type(KeyValueInput.TYPE)
                            .build())
                    .dataFetcher(new GenericInformationDataFetcher()))
            .field(newFieldDefinition().name("id")
                    .description("Unique identifier")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("inErrorTime")
                    .description("In error time")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("name")
                    .description("Name")
                    .type(GraphQLString))
            .field(newFieldDefinition().name("onTaskError")
                    .description("The behaviour applied on Tasks when an error occurs")
                    .type(ON_TASK_ERROR))
            .field(newFieldDefinition().name("startTime")
                    .description("Start time")
                    .type(GraphQLLong))
            .field(newFieldDefinition().name("variables")
                    .description("Variable list, empty if there is none")
                    .type(new GraphQLList(Variable.TYPE))
                    .argument(newArgument().name("input")
                            .description("Variables input filter")
                            .type(KeyValueInput.TYPE)
                            .build())
                    .dataFetcher(new VariablesDataFetcher()))
            .typeResolver(new TypeResolver() {

                @Override
                public GraphQLObjectType getType(
                        Object object) {
                    if (object instanceof Job) {
                        return Job.TYPE;
                    }
                    return Task.TYPE;
                }

            })
            .build();

    public JobTaskCommon(String description, long finishedTime, Map<String, String> genericInformation,
            long id, long inErrorTime, String name, String onTaskError, long startTime,
            Map<String, String> variables) {

        this.description = description;
        this.finishedTime = finishedTime;
        this.genericInformation = genericInformation;
        this.id = id;
        this.inErrorTime = inErrorTime;
        this.name = name;
        this.onTaskError = onTaskError;
        this.startTime = startTime;
        this.variables = variables;
    }
    
}
