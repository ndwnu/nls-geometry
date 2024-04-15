package nu.ndw.nls.geometry.bearing.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class BearingValidatorTest {
    @Test
    void validate_ok_valid() {
        BearingValidator.validate(20, 10);
    }

    @Test
    void validate_failed_invalidBearingExceeded360() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> BearingValidator.validate(361, 10));
        assertEquals("Bearing 361 exceeded valid range between 0 and 360", illegalArgumentException.getMessage());
    }

    @Test
    void validate_failed_invalidBearingLessThan0() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> BearingValidator.validate(-1, 10));
        assertEquals("Bearing -1 exceeded valid range between 0 and 360", illegalArgumentException.getMessage());
    }

    @Test
    void validate_failed_invalidRangeExceeded180() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> BearingValidator.validate(1, 181));
        assertEquals("Range 181 exceeded valid range between 0 and 180", illegalArgumentException.getMessage());
    }

    @Test
    void validate_failed_invalidRangeLessThan0() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> BearingValidator.validate(1, -1));
        assertEquals("Range -1 exceeded valid range between 0 and 180", illegalArgumentException.getMessage());
    }

    @Test
    void validate_failed_invalidBearingAndRange() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> BearingValidator.validate(-1, -1));
        assertEquals("Bearing -1 exceeded valid range between 0 and 360. Range -1 exceeded valid range between 0 and 180",
                illegalArgumentException.getMessage());
    }
}
