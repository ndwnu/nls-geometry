package nu.ndw.nls.geometry.geojson.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import nu.ndw.nls.geometry.rounding.dto.RoundDoubleConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoJsonMultiLineStringCoordinateMapperTest {

    @Mock
    private GeoJsonLineStringCoordinateMapper geoJsonLineStringCoordinateMapper;

    @InjectMocks
    private GeoJsonMultiLineStringCoordinateMapper geoJsonMultiLineStringCoordinateMapper;

    @Mock
    private MultiLineString multiLineString;

    @Mock
    private LineString lineStringA;
    @Mock
    private LineString lineStringB;
    @Mock
    private LineString lineStringC;

    @Mock
    private List<List<Double>> coordinatesA;
    @Mock
    private List<List<Double>> coordinatesB;
    @Mock
    private List<List<Double>> coordinatesC;

    @Mock
    private RoundDoubleConfiguration roundDoubleConfiguration;

    @Test
    void map_ok() {
        when(multiLineString.getNumGeometries()).thenReturn(3);

        when(multiLineString.getGeometryN(0)).thenReturn(lineStringA);
        when(multiLineString.getGeometryN(1)).thenReturn(lineStringB);
        when(multiLineString.getGeometryN(2)).thenReturn(lineStringC);

        when(geoJsonLineStringCoordinateMapper.map(lineStringA)).thenReturn(coordinatesA);
        when(geoJsonLineStringCoordinateMapper.map(lineStringB)).thenReturn(coordinatesB);
        when(geoJsonLineStringCoordinateMapper.map(lineStringC)).thenReturn(coordinatesC);

        List<List<List<Double>>> coordinates = geoJsonMultiLineStringCoordinateMapper.map(multiLineString);

        assertEquals(List.of(coordinatesA, coordinatesB, coordinatesC), coordinates);
    }

    @Test
    void map_ok_roundingDecimalPlaces() {
        when(multiLineString.getNumGeometries()).thenReturn(3);

        when(multiLineString.getGeometryN(0)).thenReturn(lineStringA);
        when(multiLineString.getGeometryN(1)).thenReturn(lineStringB);
        when(multiLineString.getGeometryN(2)).thenReturn(lineStringC);

        when(geoJsonLineStringCoordinateMapper.map(lineStringA, roundDoubleConfiguration)).thenReturn(coordinatesA);
        when(geoJsonLineStringCoordinateMapper.map(lineStringB, roundDoubleConfiguration)).thenReturn(coordinatesB);
        when(geoJsonLineStringCoordinateMapper.map(lineStringC, roundDoubleConfiguration)).thenReturn(coordinatesC);

        List<List<List<Double>>> coordinates = geoJsonMultiLineStringCoordinateMapper.map(multiLineString,
                roundDoubleConfiguration);

        assertEquals(List.of(coordinatesA, coordinatesB, coordinatesC), coordinates);
    }
}
