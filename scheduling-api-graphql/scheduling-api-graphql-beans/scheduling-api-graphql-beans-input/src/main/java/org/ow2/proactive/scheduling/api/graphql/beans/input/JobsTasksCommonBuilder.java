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
package org.ow2.proactive.scheduling.api.graphql.beans.input;

import java.util.List;

import org.ow2.proactive.scheduling.api.graphql.common.Fields;


/**
 * @author ActiveEon Team
 */
abstract class JobsTasksCommonBuilder {

    protected String after = null;

    protected String before = null;

    protected boolean cursor = true;

    protected boolean description = true;

    protected boolean finishedTime = true;

    protected Integer first = null;

    protected GenericInformation genericInformation = new GenericInformation.Builder().build();

    protected boolean id = true;

    protected boolean inErrorTime = true;

    protected Integer last = null;

    protected boolean maxNumberOfExecution = true;

    protected boolean name = true;

    protected boolean onTaskError = true;

    protected PageInfo pageInfo = new PageInfo.Builder().build();

    protected boolean startTime = true;

    protected boolean status = true;

    protected Variables variables = new Variables.Builder().build();

    protected StringBuilder sb = new StringBuilder();

    public JobsTasksCommonBuilder after(String after) {
        this.after = after;
        return this;
    }

    public JobsTasksCommonBuilder before(String before) {
        this.before = before;
        return this;
    }

    protected JobsTasksCommonBuilder excludeCursor() {
        this.cursor = false;
        return this;
    }

    protected JobsTasksCommonBuilder excludeDescription() {
        this.description = false;
        return this;
    }

    protected JobsTasksCommonBuilder excludeFinishedTime() {
        this.finishedTime = false;
        return this;
    }

    protected JobsTasksCommonBuilder excludeGenericInformation() {
        this.genericInformation = null;
        return this;
    }

    protected JobsTasksCommonBuilder excludeId() {
        this.id = false;
        return this;
    }

    protected JobsTasksCommonBuilder excludeInErrorTime() {
        this.inErrorTime = false;
        return this;
    }

    protected JobsTasksCommonBuilder excludeMaxNumberOfExecution() {
        this.maxNumberOfExecution = false;
        return this;
    }

    protected JobsTasksCommonBuilder excludeName() {
        this.name = false;
        return this;
    }

    protected JobsTasksCommonBuilder excludeOnTaskError() {
        this.onTaskError = false;
        return this;
    }

    protected JobsTasksCommonBuilder excludePageInfo() {
        this.pageInfo = null;
        return this;
    }

    protected JobsTasksCommonBuilder excludeStartTime() {
        this.startTime = false;
        return this;
    }

    protected JobsTasksCommonBuilder excludeStatus() {
        this.status = false;
        return this;
    }

    protected JobsTasksCommonBuilder excludeVariables() {
        this.variables = null;
        return this;
    }

    public JobsTasksCommonBuilder first(Integer first) {
        this.first = first;
        return this;
    }

    protected JobsTasksCommonBuilder genericInformation(GenericInformation genericInformation) {
        this.genericInformation = genericInformation;
        return this;
    }

    public JobsTasksCommonBuilder last(Integer last) {
        this.last = last;
        return this;
    }

    protected JobsTasksCommonBuilder pageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return this;
    }

    protected JobsTasksCommonBuilder variables(Variables variables) {
        this.variables = variables;
        return this;
    }

    protected void build(String title, List<? extends ApiType> input) {
        sb.append(title);
        // start input building
        sb.append(Inputs.buildQueryString(after, before, first, last, input));
        // end input building

        // start query body building
        sb.append("{").append(Constants.RETURN);
        if (pageInfo != null) {
            sb.append(pageInfo.getQueryString());
        }
        sb.append(Fields.EDGES.getName());
        sb.append('{');
        sb.append(Constants.RETURN);

        if (cursor) {
            sb.append(Fields.CURSOR.getName()).append(Constants.RETURN);
        }

        sb.append(Fields.NODE.getName());
        sb.append("{");
        sb.append(Constants.RETURN);

        if (description) {
            sb.append(Fields.DESCRIPTION.getName()).append(Constants.RETURN);
        }
        if (finishedTime) {
            sb.append(Fields.FINISHED_TIME.getName()).append(Constants.RETURN);
        }
        if (genericInformation != null) {
            sb.append(genericInformation.getQueryString());
        }
        if (id) {
            sb.append(Fields.ID.getName()).append(Constants.RETURN);
        }
        if (inErrorTime) {
            sb.append(Fields.IN_ERROR_TIME.getName()).append(Constants.RETURN);
        }
        if (maxNumberOfExecution) {
            sb.append(Fields.MAX_NUMBER_OF_EXECUTION.getName()).append(Constants.RETURN);
        }
        if (name) {
            sb.append(Fields.NAME.getName()).append(Constants.RETURN);
        }
        if (onTaskError) {
            sb.append(Fields.ON_TASK_ERROR.getName()).append(Constants.RETURN);
        }
        if (startTime) {
            sb.append(Fields.START_TIME.getName()).append(Constants.RETURN);
        }
        if (status) {
            sb.append(Fields.STATUS.getName()).append(Constants.RETURN);
        }
        if (variables != null) {
            sb.append(variables.getQueryString());
        }
    }

}
