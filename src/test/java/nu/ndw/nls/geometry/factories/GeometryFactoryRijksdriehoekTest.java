package nu.ndw.nls.geometry.factories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

class GeometryFactoryRijksdriehoekTest {

    private static final double POINT_A_COORDINATE_X = 1.0;
    private static final double POINT_A_COORDINATE_Y = 2.0;
    private static final double POINT_B_COORDINATE_X = 3.0;
    private static final double POINT_B_COORDINATE_Y = 4.0;

    GeometryFactoryRijksdriehoek factoryRijksdriehoek = new GeometryFactoryRijksdriehoek();

    @Test
    void createLineString_ok() {

        Coordinate[] pointArray = new Coordinate[]{
                new Coordinate(POINT_A_COORDINATE_X, POINT_A_COORDINATE_Y),
                new Coordinate(POINT_B_COORDINATE_X, POINT_B_COORDINATE_Y)
        };

        LineString lineString = factoryRijksdriehoek.createLineString(pointArray);

        Coordinate coordinate1 = lineString.getCoordinateSequence().getCoordinate(0);
        Coordinate coordinate2 = lineString.getCoordinateSequence().getCoordinate(1);

        assertThat(coordinate1.getX()).isEqualTo(POINT_A_COORDINATE_X);
        assertThat(coordinate1.getY()).isEqualTo(POINT_A_COORDINATE_Y);
        assertThat(coordinate2.getX()).isEqualTo(POINT_B_COORDINATE_X);
        assertThat(coordinate2.getY()).isEqualTo(POINT_B_COORDINATE_Y);

    }
}