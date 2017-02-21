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
package org.ow2.proactive.scheduling.api.graphql.client.beans;

import static com.google.common.truth.Truth.assertThat;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.GENERIC_INFORMATION;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.KEY;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VALUE;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VARIABLES;

import org.junit.Test;
import org.ow2.proactive.scheduling.api.graphql.beans.input.GenericInformation;
import org.ow2.proactive.scheduling.api.graphql.beans.input.Variables;


public class KeyValueTest {

    private static final String GENERIC_INFO = GENERIC_INFORMATION.getName() + "{\n" + KEY.getName() + "\n" +
                                               VALUE.getName() + "\n}\n";

    private static final String GENERIC_INFO_PARTIAL = GENERIC_INFORMATION.getName() + "{\n" + KEY.getName() + "\n}\n";

    private static final String VAR = VARIABLES.getName() + "{\n" + KEY.getName() + "\n" + VALUE.getName() + "\n}\n";

    private static final String VAR_PARTIAL = VARIABLES.getName() + "{\n" + KEY.getName() + "\n}\n";

    @Test
    public void getGenericInformationQueryString() throws Exception {
        GenericInformation genericInformation = new GenericInformation.Builder().build();
        assertThat(genericInformation.getQueryString()).isEqualTo(GENERIC_INFO);
    }

    @Test
    public void getGenericInformationPartialQueryString() throws Exception {
        GenericInformation genericInformation = new GenericInformation.Builder().excludeValue().build();
        assertThat(genericInformation.getQueryString()).isEqualTo(GENERIC_INFO_PARTIAL);
    }

    @Test
    public void getVariablesQueryString() throws Exception {
        Variables variables = new Variables.Builder().build();
        assertThat(variables.getQueryString()).isEqualTo(VAR);
    }

    @Test
    public void getVariablesPartialQueryString() throws Exception {
        Variables variables = new Variables.Builder().excludeValue().build();
        assertThat(variables.getQueryString()).isEqualTo(VAR_PARTIAL);
    }

}
