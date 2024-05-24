package nu.ndw.nls.geometry.geojson.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoJsonJtsCoordinateMapperTest {

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

    @Test
    void mapList_ok() {
        List<Coordinate> coordinates = List.of(
                new Coordinate(5.28232,51.87819),
                new Coordinate(5.12705,52.07013),
                new Coordinate(5.43831,52.11337)
        );

        String mapped = geoJsonCoordinateMapper.mapList(coordinates);

        String expected = "5.28232,51.87819;5.12705,52.07013;5.43831,52.11337";
        assertThat(mapped).isEqualTo(expected);
    }

    @Test
    void mapList_ok_forgetZAndDefaultCoordinate() {
        List<Coordinate> coordinates = List.of(
                new Coordinate(),
                new Coordinate(5.43831,52.11337, 2)
        );
        String mapped = geoJsonCoordinateMapper.mapList(coordinates);

        String expected = "0.0,0.0;5.43831,52.11337";
        assertThat(mapped).isEqualTo(expected);
    }

    @Test
    void mapList_ok_empty() {
        String mapped = geoJsonCoordinateMapper.mapList(List.of());

        assertThat(mapped).isBlank();
    }

    @Test
    void mapList_ok_null() {
        String mapped = geoJsonCoordinateMapper.mapList(null);

        assertThat(mapped).isBlank();
    }
}
