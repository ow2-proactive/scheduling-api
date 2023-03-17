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
package org.ow2.proactive.scheduling.api.graphql.fetchers.cursors;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


/**
 * @author ActiveEon Team
 */
public abstract class AbstractCursorMapper<F, T> implements CursorMapper<F, T> {

    protected static final String DUMMY_CURSOR_PREFIX = "graphql-cursor";

    @Override
    public T getOffsetFromCursor(String cursor) {

        if (cursor == null) {
            return null;
        }

        String string = new String(Base64.getDecoder().decode(cursor));
        return toOffset(string.substring(DUMMY_CURSOR_PREFIX.length()));
    }

    @Override
    public String createCursor(F field) {
        return Base64.getEncoder()
                     .encodeToString((DUMMY_CURSOR_PREFIX + toString(field)).getBytes(StandardCharsets.UTF_8));
    }

    abstract String toString(F field);

    abstract T toOffset(String cursor);

}
