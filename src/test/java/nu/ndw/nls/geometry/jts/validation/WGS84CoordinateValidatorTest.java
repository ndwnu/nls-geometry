package nu.ndw.nls.geometry.jts.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class WGS84CoordinateValidatorTest {

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
    void isValidWgs84Coordinates(double longitude, double latitude, boolean expectedResult) {
        assertThat(WGS84CoordinateValidator.isValidWgs84Coordinates(List.of(longitude, latitude))).isEqualTo(expectedResult);
    }
}
