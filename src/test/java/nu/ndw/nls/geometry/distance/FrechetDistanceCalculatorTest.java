package nu.ndw.nls.geometry.distance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import lombok.SneakyThrows;
import nu.ndw.nls.geometry.factories.GeodeticCalculatorFactory;
import nu.ndw.nls.geometry.factories.GeometryFactoryRijksdriehoek;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.WKTFileReader;
import org.locationtech.jts.io.WKTReader;

class FrechetDistanceCalculatorTest {

    private static final double DELTA_NEAREST_MILLIMETRE = 0.0005;

    private final FrechetDistanceCalculator frechetDistanceCalculator = new FrechetDistanceCalculator(new GeodeticCalculatorFactory());
    private final WKTReader wktReader = new WKTReader(new GeometryFactoryWgs84());

    @Test
    void calculateFrechetDistanceInMetresFromWgs84_ok_normalDistance() {
        LineString lineShape = readWktFromFile("/test-data/siterecord_218928_line_shape.txt");
        LineString fcdGeometry = readWktFromFile("/test-data/siterecord_218928_fcd_geometry.txt");
        double frechetDistance = frechetDistanceCalculator.calculateFrechetDistanceInMetresFromWgs84(lineShape, fcdGeometry);
        assertThat(frechetDistance).isCloseTo(7.978, within(DELTA_NEAREST_MILLIMETRE));
    }

    @Test
    void calculateFrechetDistanceInMetresFromWgs84_ok_largeDistanceDueToLoop() {
        LineString lineShape = readWktFromFile("/test-data/siterecord_200732_line_shape.txt");
        LineString fcdGeometry = readWktFromFile("/test-data/siterecord_200732_fcd_geometry.txt");
        double frechetDistance = frechetDistanceCalculator.calculateFrechetDistanceInMetresFromWgs84(lineShape, fcdGeometry);
        assertThat(frechetDistance).isCloseTo(2153.04, within(DELTA_NEAREST_MILLIMETRE));
    }

    @Test
    void calculateFrechetDistanceInMetresFromWgs84_ok_onlyZeroWithDensify() {
        GeometryFactoryWgs84 geometryFactoryWgs84 = new GeometryFactoryWgs84();
        LineString p = geometryFactoryWgs84.createLineString(new Coordinate[]{new Coordinate(0.0, 0.0), new Coordinate(0.01, 0.0)});
        LineString q = geometryFactoryWgs84.createLineString(new Coordinate[]{new Coordinate(0.0, 0.0), new Coordinate(0.005, 0.0),
                new Coordinate(0.01, 0.0)});
        double frechetDistance = frechetDistanceCalculator.calculateFrechetDistanceInMetresFromWgs84(p, q);
        assertThat(frechetDistance).isZero();
    }

    @Test
    void calculateFrechetDistanceInMetresFromWgs84_exception_notWgs84() {
        LineString lineStringRd = new GeometryFactoryRijksdriehoek().createLineString();
        IllegalArgumentException exception = assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> frechetDistanceCalculator.calculateFrechetDistanceInMetresFromWgs84(lineStringRd, lineStringRd)).actual();
        assertThat(exception.getMessage()).isEqualTo("SRID must be WGS84, but is RIJKSDRIEHOEK");
    }

    @SneakyThrows
    private LineString readWktFromFile(String name) {
        return (LineString) new WKTFileReader(FrechetDistanceCalculatorTest.class.getResource(name).getFile(), wktReader).read().getFirst();
    }
}
