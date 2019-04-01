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
package org.ow2.proactive.scheduling.api.graphql.fetchers.converter;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Before;
import org.junit.Test;


/**
 * @author ActiveEon Team
 * @since 12/12/16
 */
public class AbstractJobTaskInputConverterTest {

    private static final String START_WITH = "name%";

    private static final String END_WITH = "%name";

    private static final String CONTAINS = "%name%";

    private static final String NOT_CONTAIN = "!%name%";

    private static final String NOT_EQUAL = "!name";

    private static final String NONE = "name";

    private CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);

    private Root root = mock(Root.class);

    private Path path = mock(Path.class);

    private Predicate startWith = mock(Predicate.class);

    private Predicate endWith = mock(Predicate.class);

    private Predicate contains = mock(Predicate.class);

    private Predicate notContain = mock(Predicate.class);

    private Predicate notEqual = mock(Predicate.class);

    private Predicate none = mock(Predicate.class);

    @Before
    public void setup() {
        when(root.get(any(String.class))).thenReturn(path);
        when(criteriaBuilder.like(any(Path.class), matches(START_WITH))).thenReturn(startWith);
        when(criteriaBuilder.like(any(Path.class), matches(END_WITH))).thenReturn(endWith);
        when(criteriaBuilder.like(any(Path.class), matches(CONTAINS))).thenReturn(contains);
        when(criteriaBuilder.notLike(any(Path.class),
                                     matches(NOT_CONTAIN.replaceFirst("!*", "*").replace("*",
                                                                                         "%")))).thenReturn(notContain);
        when(criteriaBuilder.notEqual(any(Path.class), matches(NOT_EQUAL.replaceFirst("!", "")))).thenReturn(notEqual);
        when(criteriaBuilder.equal(any(Path.class), matches(NONE))).thenReturn(none);
    }

    @Test
    public void testStartWithPredicateBuilder() {
        Predicate predicate = AbstractJobTaskInputConverter.WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                                root,
                                                                                                "name",
                                                                                                "name*");
        assertThat(predicate).isEqualTo(startWith);
    }

    @Test
    public void testEndWithPredicateBuilder() {
        Predicate predicate = AbstractJobTaskInputConverter.WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                                root,
                                                                                                "name",
                                                                                                "*name");
        assertThat(predicate).isEqualTo(endWith);
    }

    @Test
    public void testContainsPredicateBuilder() {
        Predicate predicate = AbstractJobTaskInputConverter.WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                                root,
                                                                                                "name",
                                                                                                "*name*");
        assertThat(predicate).isEqualTo(contains);
    }

    @Test
    public void testNotContainPredicateBuilder() {
        Predicate predicate = AbstractJobTaskInputConverter.WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                                root,
                                                                                                "name",
                                                                                                "!*name*");
        assertThat(predicate).isEqualTo(notContain);
    }

    @Test
    public void testNotEqualPredicateBuilder() {
        Predicate predicate = AbstractJobTaskInputConverter.WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                                root,
                                                                                                "name",
                                                                                                "!name");
        assertThat(predicate).isEqualTo(notEqual);
    }

    @Test
    public void testNonePredicateBuilder() {
        Predicate predicate = AbstractJobTaskInputConverter.WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                                root,
                                                                                                "name",
                                                                                                "name");
        assertThat(predicate).isEqualTo(none);

    }

}
