package nu.ndw.nls.geometry.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;

class JtsGeometrySimplifierMapperTest {

    private static final GeometryFactory WGS84_GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);
    private static final Coordinate COORDINATE_A = new Coordinate(5.431641, 52.17898);
    private static final Coordinate COORDINATE_B = new Coordinate(5.431601, 52.178947);
    private static final Coordinate COORDINATE_C = new Coordinate(5.431077, 52.1786);
    private static final Coordinate COORDINATE_D = new Coordinate(5.430663, 52.178413);
    private static final Coordinate COORDINATE_X = new Coordinate(5.43111, 52.178622);
    private static final Coordinate COORDINATE_SAME_VALUES_AS_X = new Coordinate(5.43111, 52.178622);

    private final JtsGeometrySimplifierMapper geometrySimplifierMapper = new JtsGeometrySimplifierMapper();

    @Test
    void map_ok_defaultAndSimplified() {
        LineString expected = createLineString(COORDINATE_A, COORDINATE_B, COORDINATE_C, COORDINATE_D);

        LineString result = geometrySimplifierMapper.map(
                createLineString(COORDINATE_A, COORDINATE_B, COORDINATE_X, COORDINATE_SAME_VALUES_AS_X, COORDINATE_C,
                        COORDINATE_D));

        assertEquals(expected, result);
    }

    @Test
    void map_ok_withDistanceToleranceNotSimplified() {
        LineString expected = createLineString(COORDINATE_A, COORDINATE_B, COORDINATE_X, COORDINATE_C, COORDINATE_D);

        LineString result = geometrySimplifierMapper.map(
                0.00000000000000000000000001,
                createLineString(COORDINATE_A, COORDINATE_B, COORDINATE_X, COORDINATE_C, COORDINATE_D)
        );

        assertEquals(expected, result);
    }

    @Test
    void map_ok_withDistanceToleranceSimplified() {
        LineString expected = createLineString(COORDINATE_A, COORDINATE_B, COORDINATE_C, COORDINATE_D);

        LineString result = geometrySimplifierMapper.map(
                0.000001, createLineString(COORDINATE_A, COORDINATE_B, COORDINATE_X, COORDINATE_C, COORDINATE_D)
        );

        assertEquals(expected, result);
    }

    @Test
    void map_ok_withHigherDistanceToleranceMoreSimplified() {
        LineString expected = createLineString(COORDINATE_A, COORDINATE_D);

        LineString result = geometrySimplifierMapper.map(
                0.01, createLineString(COORDINATE_A, COORDINATE_B, COORDINATE_X, COORDINATE_C, COORDINATE_D)
        );

        assertEquals(expected, result);
    }

    private LineString createLineString(Coordinate... coordinates) {
        return WGS84_GEOMETRY_FACTORY.createLineString(coordinates);
    }
}
