package nu.ndw.nls.geometry.jts.factories;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JtsPolygonFactoryWgs84Test {

    @Mock
    private GeometryFactoryWgs84 geometryFactoryWgs84;

    @Mock
    private JtsPolygonFactory jtsPolygonFactory;

    @InjectMocks
    private JtsPolygonFactoryWgs84 jtsPolygonFactoryWgs84;

    @Mock
    private Coordinate coordinateA;

    @Mock
    private Coordinate coordinateB;

    @Mock
    private Polygon boundingBox;

    @Test
    void createBoundingBox_ok() {
        when(jtsPolygonFactory.createBoundingBox(geometryFactoryWgs84, coordinateA, coordinateB)).thenReturn(boundingBox);
        assertThat(jtsPolygonFactoryWgs84.createBoundingBox(coordinateA, coordinateB)).isEqualTo(boundingBox);
    }
}