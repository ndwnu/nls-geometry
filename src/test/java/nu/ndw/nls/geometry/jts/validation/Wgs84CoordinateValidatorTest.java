package nu.ndw.nls.geometry.jts.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.locationtech.jts.geom.Coordinate;

class Wgs84CoordinateValidatorTest {

    @ParameterizedTest
    @CsvSource(textBlock = """
            180,90,true
            -180,-90,true
            180.1,90,false
            180.1,-90,false
            -180.1,90,false
            -180.1,-90,false
            -180,-90.1,false
            -180,90.1,false
            """)
    void isValidWgs84Coordinate_ok(double longitude, double latitude, boolean expectedResult) {
        assertThat(Wgs84CoordinateValidator.isValidWgs84Coordinate(new Coordinate(longitude, latitude))).isEqualTo(expectedResult);
    }
}
