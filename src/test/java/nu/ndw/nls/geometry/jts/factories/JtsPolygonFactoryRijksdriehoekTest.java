package nu.ndw.nls.geometry.jts.factories;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import nu.ndw.nls.geometry.factories.GeometryFactoryRijksdriehoek;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JtsPolygonFactoryRijksdriehoekTest {

    @Mock
    private GeometryFactoryRijksdriehoek geometryFactoryRijksdriehoek;

    @Mock
    private JtsPolygonFactory jtsPolygonFactory;

    @InjectMocks
    private JtsPolygonFactoryRijksdriehoek jtsPolygonFactoryRijksdriehoek;

    @Mock
    private Coordinate coordinateA;

    @Mock
    private Coordinate coordinateB;

    @Mock
    private Polygon boundingBox;

    @Test
    void createBoundingBox_ok() {
        when(jtsPolygonFactory.createBoundingBox(geometryFactoryRijksdriehoek, coordinateA, coordinateB)).thenReturn(boundingBox);
        assertThat(jtsPolygonFactoryRijksdriehoek.createBoundingBox(coordinateA, coordinateB)).isEqualTo(boundingBox);
    }
}