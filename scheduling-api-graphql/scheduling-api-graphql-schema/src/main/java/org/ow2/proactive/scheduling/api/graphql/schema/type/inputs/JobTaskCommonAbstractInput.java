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

import static org.ow2.proactive.scheduling.api.graphql.common.InputFields.ID;

import java.util.Map;

import org.ow2.proactive.scheduling.api.graphql.common.Fields;
import org.ow2.proactive.scheduling.api.graphql.common.Inputs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * @author ActiveEon Team
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class JobTaskCommonAbstractInput {

    protected long id;

    protected JobStatusInput jobStatus;

    public JobTaskCommonAbstractInput(Map<String, Object> input) {
        if (input != null) {
            id = Inputs.getValue(input, ID.getName(), -1L);
            jobStatus = new JobStatusInput(Inputs.getValue(input, Fields.STATUS.getName(), null));
        } else {
            id = -1L;
            jobStatus = null;
        }
    }

}
