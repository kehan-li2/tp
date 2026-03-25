package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class VisitContainsDatePredicateTest {
    @Test
    public void test_visitWithinRange_returnsTrue() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 1, 31);
        VisitContainsDatePredicate predicate = new VisitContainsDatePredicate(start, end);

        assertTrue(predicate.test(new PersonBuilder().withVisitDateTime("2026-01-15 10:00").build()));
        assertTrue(predicate.test(new PersonBuilder().withVisitDateTime("2026-01-01 00:00").build()));
    }

    @Test
    public void test_noVisit_returnsFalse() {
        LocalDate now = LocalDate.now();
        VisitContainsDatePredicate predicate = new VisitContainsDatePredicate(now, now);
        Person personWithNoVisit = new PersonBuilder().withName("No Visit").build();

        assertFalse(predicate.test(personWithNoVisit));
    }
}
