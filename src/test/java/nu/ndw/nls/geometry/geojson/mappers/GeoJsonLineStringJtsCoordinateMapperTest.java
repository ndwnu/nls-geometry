package nu.ndw.nls.geometry.geojson.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoJsonLineStringJtsCoordinateMapperTest {

    @Mock
    private GeoJsonCoordinateMapper geoJsonCoordinateMapper;

    @InjectMocks
    private GeoJsonLineStringCoordinateMapper geoJsonLineStringCoordinateMapper;

    @Mock
    private LineString lineString;

    @Mock
    private Coordinate coordinateA;
    @Mock
    private Coordinate coordinateB;

    @Mock
    private List<Double> coordinateAListDouble;
    @Mock
    private List<Double> coordinateBListDouble;

    @Test
    void map_ok() {
        when(lineString.getCoordinates()).thenReturn(new Coordinate[]{coordinateA, coordinateB});
        when(geoJsonCoordinateMapper.map(coordinateA)).thenReturn(coordinateAListDouble);
        when(geoJsonCoordinateMapper.map(coordinateB)).thenReturn(coordinateBListDouble);

        List<List<Double>> coordinates = geoJsonLineStringCoordinateMapper.map(lineString);

        assertEquals(List.of(coordinateAListDouble, coordinateBListDouble), coordinates);

        verify(lineString).getCoordinates();
        verify(geoJsonCoordinateMapper).map(coordinateA);
        verify(geoJsonCoordinateMapper).map(coordinateB);
    }
}
