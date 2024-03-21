package nu.ndw.nls.geometry.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

class GeometryFactoryWgs84Test {

    private static final double POINT_A_COORDINATE_X = 1.0;
    private static final double POINT_A_COORDINATE_Y = 2.0;
    private static final double POINT_B_COORDINATE_X = 3.0;
    private static final double POINT_B_COORDINATE_Y = 4.0;

    GeometryFactoryWgs84 factoryRijksdriehoek = new GeometryFactoryWgs84();

    @Test
    public void createLineString_ok() {

        Coordinate[] pointArray = new Coordinate[]{
                new Coordinate(POINT_A_COORDINATE_X, POINT_A_COORDINATE_Y),
                new Coordinate(POINT_B_COORDINATE_X, POINT_B_COORDINATE_Y)
        };

        LineString lineString = factoryRijksdriehoek.createLineString(pointArray);

        Coordinate coordinate1 = lineString.getCoordinateSequence().getCoordinate(0);
        Coordinate coordinate2 = lineString.getCoordinateSequence().getCoordinate(1);

        assertEquals(POINT_A_COORDINATE_X, coordinate1.getX());
        assertEquals(POINT_A_COORDINATE_Y, coordinate1.getY());
        assertEquals(POINT_B_COORDINATE_X, coordinate2.getX());
        assertEquals(POINT_B_COORDINATE_Y, coordinate2.getY());

    }
}