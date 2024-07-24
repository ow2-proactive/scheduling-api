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
package org.ow2.proactive.scheduling.api.graphql.schema.type.inputs;

import static graphql.Scalars.GraphQLBoolean;
import static graphql.Scalars.GraphQLString;
import static graphql.scalars.java.JavaPrimitives.GraphQLLong;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.BUCKET_NAME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.CHILDREN_COUNT;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.CUMULATED_CORE_TIME;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.LABEL;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.NUMBER_OF_NODES;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.NUMBER_OF_NODES_IN_PARALLEL;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.PARENT_ID;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.SUBMISSION_MODE;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.TENANT;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VARIABLES;
import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.*;

import java.util.Map;

import org.ow2.proactive.scheduling.api.graphql.common.Inputs;
import org.ow2.proactive.scheduling.api.graphql.common.Types;
import org.ow2.proactive.scheduling.api.graphql.schema.type.JobPriority;
import org.ow2.proactive.scheduling.api.graphql.schema.type.JobStatus;
import org.ow2.proactive.scheduling.api.graphql.schema.type.TypeSingleton;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLList;
import lombok.Getter;


/**
 * @author ActiveEon team
 */
@Getter
public class JobInput extends JobTaskCommonAbstractInput {

    public final static TypeSingleton<GraphQLInputType> TYPE = new TypeSingleton<GraphQLInputType>() {
        @Override
        public GraphQLInputType buildType(DataFetcher... dataFetchers) {
            return newInputObject().name(Types.JOB_INPUT.getName())
                                   .description("Job input filter.")
                                   .field(newInputObjectField().name(EXCLUDE_REMOVED.getName())
                                                               .description("Exclude removed job, true by default.")
                                                               .type(GraphQLBoolean)
                                                               .build())
                                   .field(newInputObjectField().name(ID.getName())
                                                               .description("Job identifier.")
                                                               .type(GraphQLLong)
                                                               .build())
                                   .field(newInputObjectField().name(COMPARABLE_ID.getName())
                                                               .description("Job identifier.")
                                                               .type(ComparableIdInput.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(LAST_UPDATED_TIME.getName())
                                                               .description("Job last updated time.")
                                                               .type(LastUpdatedTimeInput.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(NAME.getName())
                                                               .description("Job name.")
                                                               .type(GraphQLString)
                                                               .build())
                                   .field(newInputObjectField().name(OWNER.getName())
                                                               .description("Job owner (i.e. the one who submitted the Job).")
                                                               .type(GraphQLString)
                                                               .build())
                                   .field(newInputObjectField().name(TENANT.getName())
                                                               .description("Job owner tenant (i.e. the tenant to which the job owner belongs).")
                                                               .type(GraphQLString)
                                                               .build())
                                   .field(newInputObjectField().name(PRIORITY.getName())
                                                               .description("Job priority.")
                                                               .type(JobPriority.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(PROJECT_NAME.getName())
                                                               .description("Project name which the job belongs to.")
                                                               .type(GraphQLString)
                                                               .build())
                                   .field(newInputObjectField().name(BUCKET_NAME.getName())
                                                               .description("Bucket name which the job belongs to.")
                                                               .type(GraphQLString)
                                                               .build())
                                   .field(newInputObjectField().name(SUBMISSION_MODE.getName())
                                                               .description("Submission mode of the job.")
                                                               .type(GraphQLString)
                                                               .build())
                                   .field(newInputObjectField().name(LABEL.getName())
                                                               .description("Label of the job.")
                                                               .type(GraphQLString)
                                                               .build())
                                   .field(newInputObjectField().name("status")
                                                               .description("Job status.")
                                                               .type(new GraphQLList(JobStatus.TYPE.getInstance()))
                                                               .build())
                                   .field(newInputObjectField().name(SUBMITTED_TIME.getName())
                                                               .description("Job submitted time.")
                                                               .type(SubmittedTimeInput.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(START_TIME.getName())
                                                               .description("Job start time.")
                                                               .type(StartTimeInput.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(START_AT.getName())
                                                               .description("Start at time defined in the generic information of the job.") // TODO clear desc
                                                               .type(StartAtInput.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(FINISHED_TIME.getName())
                                                               .description("Job finished time.")
                                                               .type(FinishedTimeInput.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(NUMBER_OF_PENDING_TASKS.getName())
                                                               .description("Number of pending tasks.")
                                                               .type(ComparableNumberOfPendingTasksInput.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(NUMBER_OF_RUNNING_TASKS.getName())
                                                               .description("Number of running tasks.")
                                                               .type(ComparableNumberOfRunningTasksInput.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(NUMBER_OF_FINISHED_TASKS.getName())
                                                               .description("Number of finished tasks.")
                                                               .type(ComparableNumberOfFinishedTasksInput.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(NUMBER_OF_FAULTY_TASKS.getName())
                                                               .description("Number of faulty tasks.")
                                                               .type(ComparableNumberOfFaultyTasksInput.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(NUMBER_OF_FAILED_TASKS.getName())
                                                               .description("Number of failed tasks.")
                                                               .type(ComparableNumberOfFailedTasksInput.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(NUMBER_OF_IN_ERROR_TASKS.getName())
                                                               .description("Number of in-error tasks.")
                                                               .type(ComparableNumberOfInErrorTasksInput.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(CUMULATED_CORE_TIME.getName())
                                                               .description("Cumulated core time consumed my all of the tasks of a job.")
                                                               .type(ComparableCumulatedCoreTime.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(PARENT_ID.getName())
                                                               .description("Parent id of the job.")
                                                               .type(ComparableParentId.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(CHILDREN_COUNT.getName())
                                                               .description("The number of children jobs.")
                                                               .type(ComparableChildrenCount.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(NUMBER_OF_NODES.getName())
                                                               .description("The total number of jobs used by the job.")
                                                               .type(ComparableNumberOfNodes.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(NUMBER_OF_NODES_IN_PARALLEL.getName())
                                                               .description("The total number of jobs in parallel used by the job.")
                                                               .type(ComparableNumberOfNodesInParallel.TYPE.getInstance())
                                                               .build())
                                   .field(newInputObjectField().name(VARIABLES.getName())
                                                               .description("workflow variables")
                                                               .type(new GraphQLList(KeyValueInput.TYPE.getInstance())))

                                   .build();
        }
    };

    private boolean excludeRemoved;

    private String jobName;

    private String owner;

    private String tenant;

    private String priority;

    private String projectName;

    private String bucketName;

    private String submissionMode;

    private String label;

    private ComparableCumulatedCoreTime cumulatedCoreTime;

    private ComparableParentId parentId;

    private ComparableChildrenCount childrenCount;

    private ComparableNumberOfNodes numberOfNodes;

    private ComparableNumberOfNodesInParallel numberOfNodesInParallel;

    private ComparableIdInput comparableId;

    private SubmittedTimeInput submittedTime;

    private StartAtInput startAt;

    private LastUpdatedTimeInput lastUpdatedTime;

    private StartTimeInput startedTime;

    private FinishedTimeInput finishedTime;

    private VariablesInput withVariables;

    private ComparableNumberOfPendingTasksInput numberOfPendingTasks;

    private ComparableNumberOfRunningTasksInput numberOfRunningTasks;

    private ComparableNumberOfFinishedTasksInput numberOfFinishedTasks;

    private ComparableNumberOfFaultyTasksInput numberOfFaultyTasks;

    private ComparableNumberOfFailedTasksInput numberOfFailedTasks;

    private ComparableNumberOfInErrorTasksInput numberOfInErrorTasks;

    public JobInput(Map<String, Object> input) {
        super(input);
        if (input != null) {
            comparableId = Inputs.getObject(input, COMPARABLE_ID.getName(), ComparableIdInput::new, null);
            excludeRemoved = Inputs.getValue(input, EXCLUDE_REMOVED.getName(), true);
            jobName = Inputs.getValue(input, NAME.getName(), null);
            owner = Inputs.getValue(input, OWNER.getName(), null);
            tenant = Inputs.getValue(input, TENANT.getName(), null);
            priority = Inputs.getValue(input, PRIORITY.getName(), null);
            projectName = Inputs.getValue(input, PROJECT_NAME.getName(), null);
            bucketName = Inputs.getValue(input, BUCKET_NAME.getName(), null);
            submissionMode = Inputs.getValue(input, SUBMISSION_MODE.getName(), null);
            label = Inputs.getValue(input, LABEL.getName(), null);
            submittedTime = Inputs.getObject(input, SUBMITTED_TIME.getName(), SubmittedTimeInput::new, null);
            lastUpdatedTime = Inputs.getObject(input, LAST_UPDATED_TIME.getName(), LastUpdatedTimeInput::new, null);
            startedTime = Inputs.getObject(input, START_TIME.getName(), StartTimeInput::new, null);
            finishedTime = Inputs.getObject(input, FINISHED_TIME.getName(), FinishedTimeInput::new, null);
            withVariables = Inputs.getListOfObjects(input, VARIABLES.getName(), VariablesInput::new, null);
            numberOfPendingTasks = Inputs.getObject(input,
                                                    NUMBER_OF_PENDING_TASKS.getName(),
                                                    ComparableNumberOfPendingTasksInput::new,
                                                    null);
            numberOfRunningTasks = Inputs.getObject(input,
                                                    NUMBER_OF_RUNNING_TASKS.getName(),
                                                    ComparableNumberOfRunningTasksInput::new,
                                                    null);
            numberOfFinishedTasks = Inputs.getObject(input,
                                                     NUMBER_OF_FINISHED_TASKS.getName(),
                                                     ComparableNumberOfFinishedTasksInput::new,
                                                     null);
            numberOfFaultyTasks = Inputs.getObject(input,
                                                   NUMBER_OF_FAULTY_TASKS.getName(),
                                                   ComparableNumberOfFaultyTasksInput::new,
                                                   null);
            numberOfFailedTasks = Inputs.getObject(input,
                                                   NUMBER_OF_FAILED_TASKS.getName(),
                                                   ComparableNumberOfFailedTasksInput::new,
                                                   null);
            numberOfInErrorTasks = Inputs.getObject(input,
                                                    NUMBER_OF_IN_ERROR_TASKS.getName(),
                                                    ComparableNumberOfInErrorTasksInput::new,
                                                    null);
            cumulatedCoreTime = Inputs.getObject(input,
                                                 CUMULATED_CORE_TIME.getName(),
                                                 ComparableCumulatedCoreTime::new,
                                                 null);
            parentId = Inputs.getObject(input, PARENT_ID.getName(), ComparableParentId::new, null);
            startAt = Inputs.getObject(input, START_AT.getName(), StartAtInput::new, null);
            childrenCount = Inputs.getObject(input, CHILDREN_COUNT.getName(), ComparableChildrenCount::new, null);
            numberOfNodes = Inputs.getObject(input, NUMBER_OF_NODES.getName(), ComparableNumberOfNodes::new, null);
            numberOfNodesInParallel = Inputs.getObject(input,
                                                       NUMBER_OF_NODES_IN_PARALLEL.getName(),
                                                       ComparableNumberOfNodesInParallel::new,
                                                       null);
        }
    }

}
