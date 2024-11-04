package nu.ndw.nls.geometry.jts.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import nu.ndw.nls.geometry.factories.GeometryFactoryRijksdriehoek;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.LineString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoJsonJtsLineStringMapperRijksdriehoekTest {

    @Mock
    private GeometryFactoryRijksdriehoek geometryFactoryRijksdriehoek;

    @Mock
    private GeoJsonJtsLineStringMapper geoJsonJtsLineStringMapper;

    @InjectMocks
    private GeoJsonJtsLineStringMapperRijksdriehoek geoJsonJtsLineStringMapperRijksdriehoek;

    @Mock
    private List<List<Double>> geoJsonCoordinates;

    @Mock
    private LineString lineStringRijksdriehoek;

    @Test
    void map_ok() {
        when(geoJsonJtsLineStringMapper.map(geometryFactoryRijksdriehoek, geoJsonCoordinates))
                .thenReturn(lineStringRijksdriehoek);

        assertThat(geoJsonJtsLineStringMapperRijksdriehoek.map(geoJsonCoordinates)).isEqualTo(lineStringRijksdriehoek);
    }
}