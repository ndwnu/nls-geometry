package nu.ndw.nls.geometry.bearing;

import static org.assertj.core.api.Assertions.assertThat;

import nu.ndw.nls.geometry.GeometryConfiguration;
import nu.ndw.nls.geometry.bearing.model.BearingFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GeometryConfiguration.class})
class BearingCalculatorIT {

    private static final BearingFilter BEARING_FILTER_WITH_WRAPAROUND = new BearingFilter(0, 10);

    @Autowired
    private BearingCalculator bearingCalculator;

    @Test
    void bearingIsInRange_true_bearingFilterNull() {
        assertThat(bearingCalculator.bearingIsInRange(10.0, null)).isTrue();
    }

    @Test
    void bearingIsInRange_true_inRange() {
        assertThat(bearingCalculator.bearingIsInRange(0, BEARING_FILTER_WITH_WRAPAROUND)).isTrue();
        // 360 is equivalent to 0.
        assertThat(bearingCalculator.bearingIsInRange(360, BEARING_FILTER_WITH_WRAPAROUND)).isTrue();
        // Range is inclusive.
        assertThat(bearingCalculator.bearingIsInRange(350, BEARING_FILTER_WITH_WRAPAROUND)).isTrue();
        assertThat(bearingCalculator.bearingIsInRange(10, BEARING_FILTER_WITH_WRAPAROUND)).isTrue();
    }

    @Test
    void bearingIsInRange_false_notInRange() {
        assertThat(bearingCalculator.bearingIsInRange(349.9, BEARING_FILTER_WITH_WRAPAROUND)).isFalse();
        assertThat(bearingCalculator.bearingIsInRange(10.1, BEARING_FILTER_WITH_WRAPAROUND)).isFalse();
    }

    @Test
    void bearingDelta_ok() {
        assertThat(bearingCalculator.bearingDelta(0, 360)).isEqualTo(0);
        assertThat(bearingCalculator.bearingDelta(360, 0)).isEqualTo(0);
        assertThat(bearingCalculator.bearingDelta(0, 181)).isEqualTo(179);
        assertThat(bearingCalculator.bearingDelta(1, 359)).isEqualTo(2);
    }

    @Test
    void calculateBearing_with_azimuth_minus_ok_should_return_positive() {
        var fromCoordinate = new Coordinate(0.0, 1.0);
        var toCoordinate = new Coordinate(-1.0, -2.0);

        assertThat(bearingCalculator.calculateBearing(fromCoordinate, toCoordinate)).isEqualTo(
                198.54804530050606);
    }

    @Test
    void calculateBearing_with_azimuth_40_should_return_40() {
        var fromCoordinate = new Coordinate(0.0, 1.0);
        var toCoordinate = new Coordinate(1.0, 2.0);

        assertThat(bearingCalculator.calculateBearing(fromCoordinate, toCoordinate)).isEqualTo(45.17047008417188);
    }
}
