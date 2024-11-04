package nu.ndw.nls.geometry.jts.mappers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.LineString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoJsonJtsLineStringMapperWgs84Test {

    @Mock
    private GeometryFactoryWgs84 geometryFactoryWgs84;

    @Mock
    private GeoJsonJtsLineStringMapper geoJsonJtsLineStringMapper;

    @InjectMocks
    private GeoJsonJtsLineStringMapperWgs84 geoJsonJtsLineStringMapperWgs84;

    @Mock
    private List<List<Double>> geoJsonCoordinates;

    @Mock
    private LineString lineStringWgs84;

    @Test
    void map_ok() {
        when(geoJsonJtsLineStringMapper.map(geometryFactoryWgs84, geoJsonCoordinates))
                .thenReturn(lineStringWgs84);
        
        assertThat(geoJsonJtsLineStringMapperWgs84.map(geoJsonCoordinates)).isEqualTo(lineStringWgs84);
    }
}