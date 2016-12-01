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

package org.ow2.proactive.scheduling.api.graphql.client.beans;

import java.util.ArrayList;
import java.util.List;

import org.ow2.proactive.scheduling.api.graphql.common.Arguments;
import org.ow2.proactive.scheduling.api.graphql.common.Fields;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.ow2.proactive.scheduling.api.graphql.common.Arguments.FILTER;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.GENERIC_INFORMATION;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.KEY;
import static org.ow2.proactive.scheduling.api.graphql.common.Fields.VALUE;

public class GenericInformationTest {

    private static final String ALL = String.format(
            "%s( %s : [{ %s : \"START_AT\" },{ %s : \"blabla\" %s : \"value\" }] ){\n%s\n%s\n}\n",
            GENERIC_INFORMATION.getName(), FILTER.getName(), KEY.getName(), KEY.getName(), VALUE.getName(),
            KEY.getName(), VALUE.getName());

    @Test
    public void getQueryString() throws Exception {
        List<KeyValueInput> input = new ArrayList<>();
        input.add(new KeyValueInput.Builder().key("START_AT").build());
        input.add(new KeyValueInput.Builder().key("blabla").value("value").build());
        GenericInformation genericInformation = new GenericInformation.Builder().input(input).build();
        System.out.print(genericInformation.getQueryString());
        assertThat(genericInformation.getQueryString(), is(ALL));
    }

}