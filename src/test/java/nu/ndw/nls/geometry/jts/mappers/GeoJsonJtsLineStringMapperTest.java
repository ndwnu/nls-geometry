package nu.ndw.nls.geometry.jts.mappers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import nu.ndw.nls.geometry.mappers.JtsCoordinateMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class GeoJsonJtsLineStringMapperTest {

    @Mock
    private GeometryFactory geometryFactory;

    @Mock
    private JtsCoordinateMapper jtsCoordinateMapper;

    @InjectMocks
    private GeoJsonJtsLineStringMapper geoJsonJtsLineStringMapper;

    @Mock
    private List<List<Double>> geoJsonCoordinates;


    private final Coordinate[] coordinates = new Coordinate[0];

    @Mock
    private LineString lineString;

    @Test
    void map_ok() {
        when(jtsCoordinateMapper.mapToCoordinates(geoJsonCoordinates)).thenReturn(coordinates);
        when(geometryFactory.createLineString(coordinates)).thenReturn(lineString);
        assertThat(geoJsonJtsLineStringMapper.map(geometryFactory, geoJsonCoordinates)).isEqualTo(lineString);

    }
}