package nu.ndw.nls.geometry.bearing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertTrue(bearingCalculator.bearingIsInRange(10.0, null));
    }

    @Test
    void bearingIsInRange_true_inRange() {
        assertTrue(bearingCalculator.bearingIsInRange(0, BEARING_FILTER_WITH_WRAPAROUND));
        // 360 is equivalent to 0.
        assertTrue(bearingCalculator.bearingIsInRange(360, BEARING_FILTER_WITH_WRAPAROUND));
        // Range is inclusive.
        assertTrue(bearingCalculator.bearingIsInRange(350, BEARING_FILTER_WITH_WRAPAROUND));
        assertTrue(bearingCalculator.bearingIsInRange(10, BEARING_FILTER_WITH_WRAPAROUND));
    }

    @Test
    void bearingIsInRange_false_notInRange() {
        assertFalse(bearingCalculator.bearingIsInRange(349.9, BEARING_FILTER_WITH_WRAPAROUND));
        assertFalse(bearingCalculator.bearingIsInRange(10.1, BEARING_FILTER_WITH_WRAPAROUND));
    }

    @Test
    void bearingDelta_ok() {
        assertEquals(0, bearingCalculator.bearingDelta(0, 360));
        assertEquals(0, bearingCalculator.bearingDelta(360, 0));
        assertEquals(179, bearingCalculator.bearingDelta(0, 181));
        assertEquals(2, bearingCalculator.bearingDelta(1, 359));
    }

    @Test
    void calculateBearing_with_azimuth_minus_ok_should_return_positive() {
        var fromCoordinate = new Coordinate(0.0, 1.0);
        var toCoordinate = new Coordinate(-1.0, -2.0);

        assertThat(bearingCalculator.calculateBearing(fromCoordinate, toCoordinate, null)).isEqualTo(
                198.54804530050606);
    }

    @Test
    void calculateBearing_with_azimuth_40_should_return_40() {
        var fromCoordinate = new Coordinate(0.0, 1.0);
        var toCoordinate = new Coordinate(1.0, 2.0);

        assertThat(bearingCalculator.calculateBearing(fromCoordinate, toCoordinate, null)).isEqualTo(45.17047008417188);
    }
}
