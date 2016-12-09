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
package org.ow2.proactive.scheduling.api.graphql.fetchers.cursor;

import graphql.relay.Base64;


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

        String string = Base64.fromBase64(cursor);
        return toOffset(string.substring(DUMMY_CURSOR_PREFIX.length()));
    }

    @Override
    public String createCursor(F field) {
        return Base64.toBase64(DUMMY_CURSOR_PREFIX + toString(field));
    }

    abstract String toString(F field);

    abstract T toOffset(String cursor);

}
