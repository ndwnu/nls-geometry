package nu.ndw.nls.geometry.stream.collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;

class LineStringMergerCollectorTest {

    private static final GeometryFactory WGS84_GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

    @Test
    void collect_ok_lineStringsMergeIntoOne() {
        LineString lineString1 = createLineString(new Coordinate(5.431641, 52.17898),
                new Coordinate(5.431601, 52.178947), new Coordinate(5.43111, 52.178622));
        LineString lineString2 = createLineString(new Coordinate(5.43111, 52.178622),
                new Coordinate(5.431077, 52.1786), new Coordinate(5.430663, 52.178413));

        LineString expected = createLineString(new Coordinate(5.431641, 52.17898),
                new Coordinate(5.431601, 52.178947), new Coordinate(5.43111, 52.178622),
                        new Coordinate(5.431077, 52.1786), new Coordinate(5.430663, 52.178413));

        assertEquals(List.of(expected), Stream.of(lineString1, lineString2)
                .collect(new LineStringMergerCollector()));
    }

    private LineString createLineString(Coordinate... coordinates) {
        return WGS84_GEOMETRY_FACTORY.createLineString(coordinates);
    }
}

