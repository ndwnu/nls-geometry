package nu.ndw.nls.geometry.distance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.stream.Stream;
import nu.ndw.nls.geometry.GeometryConfiguration;
import nu.ndw.nls.geometry.factories.GeometryFactoryRijksdriehoek;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GeometryConfiguration.class})
class DistanceCalculatorIT {

    @Autowired
    private DistanceCalculator distanceCalculator;

    @Autowired
    private GeometryFactoryWgs84 geometryFactoryWgs84;

    @Autowired
    private GeometryFactoryRijksdriehoek geometryFactoryRijksdriehoek;

    public static Stream<Arguments> calculateDistance_okArgumentsProvider() {
        return Stream.of(
                Arguments.of(4.904839, 52.366449, 0.0),
                Arguments.of(4.907883, 52.361309, 0.0),
                Arguments.of(4.908418, 52.363990, 135.92555008685923)
        );
    }

    @ParameterizedTest
    @MethodSource("calculateDistance_okArgumentsProvider")
    void calculateDistance_ok(double sourceX, double sourceY, double expectedDistance) {
        Point sourcePoint = geometryFactoryWgs84.createPoint(new Coordinate(sourceX, sourceY));

        Coordinate[] coordinates = {
                new Coordinate(4.904839, 52.366449),
                new Coordinate(4.907883, 52.361309)
        };
        LineString targetLineString = geometryFactoryWgs84.createLineString(coordinates);

        double result = distanceCalculator.calculateDistance(sourcePoint, targetLineString);
        assertThat(result).isEqualTo(expectedDistance);
    }

    @Test
    void calculateDistance_exception_notWgs84() {
        Point sourcePoint = geometryFactoryRijksdriehoek.createPoint(new Coordinate(4.908418, 52.363990));

        Coordinate[] coordinates = {
                new Coordinate(4.904839, 52.366449),
                new Coordinate(4.907883, 52.361309)
        };
        LineString targetLineString = geometryFactoryWgs84.createLineString(coordinates);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> distanceCalculator.calculateDistance(sourcePoint, targetLineString))
                .withMessage("SRID must be WGS84, but is RIJKSDRIEHOEK");
    }
}
