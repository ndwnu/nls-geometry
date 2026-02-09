package nu.ndw.nls.geometry.bearing.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

class BearingValidatorTest {
    @Test
    void validate_ok_valid() {
        BearingValidator.validate(20, 10);
    }

    @Test
    void validate_failed_invalidBearingExceeded360() {
        IllegalArgumentException illegalArgumentException = assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> BearingValidator.validate(361, 10)).actual();
        assertThat(illegalArgumentException.getMessage()).isEqualTo("Bearing 361 exceeded valid range between 0 and 360");
    }

    @Test
    void validate_failed_invalidBearingLessThan0() {
        IllegalArgumentException illegalArgumentException = assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> BearingValidator.validate(-1, 10)).actual();
        assertThat(illegalArgumentException.getMessage()).isEqualTo("Bearing -1 exceeded valid range between 0 and 360");
    }

    @Test
    void validate_failed_invalidRangeExceeded180() {
        IllegalArgumentException illegalArgumentException = assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> BearingValidator.validate(1, 181)).actual();
        assertThat(illegalArgumentException.getMessage()).isEqualTo("Range 181 exceeded valid range between 0 and 180");
    }

    @Test
    void validate_failed_invalidRangeLessThan0() {
        IllegalArgumentException illegalArgumentException = assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> BearingValidator.validate(1, -1)).actual();
        assertThat(illegalArgumentException.getMessage()).isEqualTo("Range -1 exceeded valid range between 0 and 180");
    }

    @Test
    void validate_failed_invalidBearingAndRange() {
        IllegalArgumentException illegalArgumentException = assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> BearingValidator.validate(-1, -1)).actual();
        assertThat(illegalArgumentException.getMessage()).isEqualTo("Bearing -1 exceeded valid range between 0 and 360. Range -1 exceeded valid range between 0 and 180");
    }
}
