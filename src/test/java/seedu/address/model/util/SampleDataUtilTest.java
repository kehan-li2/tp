package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_notEmpty() {
        assertNotNull(SampleDataUtil.getSamplePersons());
        assertTrue(SampleDataUtil.getSamplePersons().length > 0);
    }

    @Test
    public void getSampleAddressBook_notEmpty() {
        assertNotNull(SampleDataUtil.getSampleAddressBook());
    }
}

