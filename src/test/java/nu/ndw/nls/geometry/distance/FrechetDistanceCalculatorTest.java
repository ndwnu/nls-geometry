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

    private final FrechetDistanceCalculator frechetDistanceCalculator = new FrechetDistanceCalculator(new GeodeticCalculatorFactory());
    private final WKTReader wktReader = new WKTReader(new GeometryFactoryWgs84());

    @Test
    void calculateFrechetDistance_ok_normalDistance() {
        LineString lineShape = readWktFromFile("/test-data/siterecord_218928_line_shape.txt");
        LineString fcdGeometry = readWktFromFile("/test-data/siterecord_218928_fcd_geometry.txt");
        double frechetDistance = frechetDistanceCalculator.calculateFrechetDistance(lineShape, fcdGeometry);
        assertEquals(7.9784048890492585, frechetDistance);
    }

    @Test
    void calculateFrechetDistance_ok_largeDistanceDueToLoop() {
        LineString lineShape = readWktFromFile("/test-data/siterecord_200732_line_shape.txt");
        LineString fcdGeometry = readWktFromFile("/test-data/siterecord_200732_fcd_geometry.txt");
        double frechetDistance = frechetDistanceCalculator.calculateFrechetDistance(lineShape, fcdGeometry);
        assertEquals(2153.0395525485847, frechetDistance);
    }

    @Test
    void calculateFrechetDistance_exception_notWgs84() {
        LineString lineStringRd = new GeometryFactoryRijksdriehoek().createLineString();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> frechetDistanceCalculator.calculateFrechetDistance(lineStringRd, lineStringRd));
        assertEquals("SRID must be WGS84 and is RIJKSDRIEHOEK", exception.getMessage());
    }

    @SneakyThrows
    private LineString readWktFromFile(String name) {
        return (LineString) new WKTFileReader(FrechetDistanceCalculatorTest.class.getResource(name).getFile(), wktReader).read().getFirst();
    }
}
