package nu.ndw.nls.geometry.jts.factories;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JtsPolygonFactoryTest {

    private static final double LONGITUDE_1 = 1.0;
    private static final double LATITUDE_1 = 2.0;
    private static final double LONGITUDE_2 = 3.0;
    private static final double LATITUDE_2 = 4.0;

    @Mock
    private GeometryFactory geometryFactory;

    @InjectMocks
    private JtsPolygonFactory jtsPolygonFactory;

    @Mock
    private Polygon polygon;

    @Test
    void createBoundingBox_ok() {

        when(geometryFactory.createPolygon(new Coordinate[] {
                new Coordinate(LONGITUDE_1, LATITUDE_1),
                new Coordinate(LONGITUDE_1, LATITUDE_2),
                new Coordinate(LONGITUDE_2, LATITUDE_2),
                new Coordinate(LONGITUDE_2, LATITUDE_1),
                new Coordinate(LONGITUDE_1, LATITUDE_1)})).thenReturn(polygon);

        assertThat(jtsPolygonFactory.createBoundingBox(geometryFactory, new Coordinate(LONGITUDE_1, LATITUDE_1),
                                                        new Coordinate(LONGITUDE_2, LATITUDE_2)))
                .isEqualTo(polygon);
    }
}