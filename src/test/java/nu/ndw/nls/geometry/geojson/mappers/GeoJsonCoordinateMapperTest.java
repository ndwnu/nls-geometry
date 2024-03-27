package nu.ndw.nls.geometry.geojson.mappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoJsonCoordinateMapperTest {

    private static final double X = 1D;
    private static final double Y = 2D;
    private final GeoJsonCoordinateMapper geoJsonCoordinateMapper = new GeoJsonCoordinateMapper();

    @Mock
    private Coordinate coordinate;

    @Test
    void map_ok() {
        when(coordinate.getX()).thenReturn(X);
        when(coordinate.getY()).thenReturn(Y);

        assertEquals(List.of(X, Y), geoJsonCoordinateMapper.map(coordinate));

        verify(coordinate).getX();
        verify(coordinate).getY();
    }
}