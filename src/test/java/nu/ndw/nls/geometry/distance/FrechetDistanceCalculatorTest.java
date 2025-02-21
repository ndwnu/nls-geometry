package nu.ndw.nls.geometry.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import lombok.SneakyThrows;
import nu.ndw.nls.geometry.factories.GeodeticCalculatorFactory;
import nu.ndw.nls.geometry.factories.GeometryFactoryRijksdriehoek;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.junit.jupiter.api.Test;
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
        assertEquals(7.978, frechetDistance, DELTA_NEAREST_MILLIMETRE);
    }

    @Test
    void calculateFrechetDistanceInMetresFromWgs84_ok_largeDistanceDueToLoop() {
        LineString lineShape = readWktFromFile("/test-data/siterecord_200732_line_shape.txt");
        LineString fcdGeometry = readWktFromFile("/test-data/siterecord_200732_fcd_geometry.txt");
        double frechetDistance = frechetDistanceCalculator.calculateFrechetDistanceInMetresFromWgs84(lineShape, fcdGeometry);
        assertEquals(2153.04, frechetDistance, DELTA_NEAREST_MILLIMETRE);
    }

    @Test
    void calculateFrechetDistanceInMetresFromWgs84_exception_notWgs84() {
        LineString lineStringRd = new GeometryFactoryRijksdriehoek().createLineString();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> frechetDistanceCalculator.calculateFrechetDistanceInMetresFromWgs84(lineStringRd, lineStringRd));
        assertEquals("SRID must be WGS84, but is RIJKSDRIEHOEK", exception.getMessage());
    }

    @SneakyThrows
    private LineString readWktFromFile(String name) {
        return (LineString) new WKTFileReader(FrechetDistanceCalculatorTest.class.getResource(name).getFile(), wktReader).read().getFirst();
    }
}
