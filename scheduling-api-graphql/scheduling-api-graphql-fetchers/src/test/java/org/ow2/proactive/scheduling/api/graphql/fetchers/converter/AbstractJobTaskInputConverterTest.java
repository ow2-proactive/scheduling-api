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

    private static final String NONE = "name";

    private CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);

    private Root root = mock(Root.class);

    private Path path = mock(Path.class);

    private Predicate startWith = mock(Predicate.class);

    private Predicate endWith = mock(Predicate.class);

    private Predicate contains = mock(Predicate.class);

    private Predicate none = mock(Predicate.class);

    @Before
    public void setup() {
        when(root.get(any(String.class))).thenReturn(path);
        when(criteriaBuilder.like(any(Path.class), matches(START_WITH))).thenReturn(startWith);
        when(criteriaBuilder.like(any(Path.class), matches(END_WITH))).thenReturn(endWith);
        when(criteriaBuilder.like(any(Path.class), matches(CONTAINS))).thenReturn(contains);
        when(criteriaBuilder.equal(any(Path.class), matches(NONE))).thenReturn(none);
    }

    @Test
    public void testWildCardInputPredicateBuilder() {
        Predicate predicate = AbstractJobTaskInputConverter.WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                                root,
                                                                                                "name",
                                                                                                "name*");
        assertThat(predicate).isEqualTo(startWith);

        predicate = AbstractJobTaskInputConverter.WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                      root,
                                                                                      "name",
                                                                                      "*name");
        assertThat(predicate).isEqualTo(endWith);

        predicate = AbstractJobTaskInputConverter.WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                      root,
                                                                                      "name",
                                                                                      "*name*");
        assertThat(predicate).isEqualTo(contains);

        predicate = AbstractJobTaskInputConverter.WildCardInputPredicateBuilder.build(criteriaBuilder,
                                                                                      root,
                                                                                      "name",
                                                                                      "name");
        assertThat(predicate).isEqualTo(none);

    }

}
