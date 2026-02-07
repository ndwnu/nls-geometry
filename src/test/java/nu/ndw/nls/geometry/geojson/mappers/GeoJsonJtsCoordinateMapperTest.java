package nu.ndw.nls.geometry.geojson.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import nu.ndw.nls.geometry.rounding.dto.RoundDoubleConfiguration;
import nu.ndw.nls.geometry.rounding.mappers.RoundDoubleMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoJsonJtsCoordinateMapperTest {

    private static final double X = 1.123456D;
    private static final double Y = 2.123456D;

    private static final double X_ROUNDING_DECIMAL_PLACES_2 = 1.12;
    private static final double Y_ROUNDING_DECIMAL_PLACES_2 = 2.12;

    @Mock
    private RoundDoubleMapper roundDoubleMapper;

    @InjectMocks
    private GeoJsonCoordinateMapper geoJsonCoordinateMapper;

    @Mock
    private Coordinate coordinate;

    @Mock
    private RoundDoubleConfiguration roundDoubleConfiguration;


    @Test
    void map_ok() {
        when(coordinate.getX()).thenReturn(X);
        when(coordinate.getY()).thenReturn(Y);

        assertEquals(List.of(X, Y), geoJsonCoordinateMapper.map(coordinate));

        verify(coordinate).getX();
        verify(coordinate).getY();
    }

    @Test
    void map_ok_withRoundingDecimalPlaces() {
        when(coordinate.getX()).thenReturn(X);
        when(coordinate.getY()).thenReturn(Y);

        when(roundDoubleMapper.round(X, roundDoubleConfiguration)).thenReturn(X_ROUNDING_DECIMAL_PLACES_2);
        when(roundDoubleMapper.round(Y, roundDoubleConfiguration)).thenReturn(Y_ROUNDING_DECIMAL_PLACES_2);

        assertEquals(List.of(X_ROUNDING_DECIMAL_PLACES_2, Y_ROUNDING_DECIMAL_PLACES_2),
                geoJsonCoordinateMapper.map(coordinate, roundDoubleConfiguration));

        verify(roundDoubleMapper).round(X, roundDoubleConfiguration);
        verify(roundDoubleMapper).round(Y, roundDoubleConfiguration);

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

    @Test
    void mapCoordinate_ok() {
        String result = geoJsonCoordinateMapper.mapCoordinate(new Coordinate(5.28232, 51.87819));
        assertThat(result).isEqualTo("5.28232,51.87819");
    }
}
