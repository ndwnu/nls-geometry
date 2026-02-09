package nu.ndw.nls.geometry.jts.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StringJtsCoordinateMapperTest {

    private static final double X = 5.123456D;

    private static final double Y = 52.123456D;

    @InjectMocks
    private StringJtsCoordinateMapper stringJtsCoordinateMapper;

    @Mock
    private Coordinate coordinate;

    @Test
    void map_coordinateList() {
        List<Coordinate> coordinates = List.of(
                new Coordinate(5.28232,51.87819),
                new Coordinate(5.12705,52.07013),
                new Coordinate(5.43831,52.11337)
        );

        String mapped = stringJtsCoordinateMapper.map(coordinates);

        assertThat(mapped).isEqualTo("5.28232,51.87819;5.12705,52.07013;5.43831,52.11337");
    }

    @Test
    void map_coordinateEmptyList() {
        List<Coordinate> coordinates = Collections.emptyList();

        String mapped = stringJtsCoordinateMapper.map(coordinates);

        assertThat(mapped).isEmpty();
    }

    @Test
    void map_coordinateNull() {
        List<Coordinate> coordinates = null;

        assertThatThrownBy(() -> stringJtsCoordinateMapper.map(coordinates))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void map_coordinate() {
        when(coordinate.getX()).thenReturn(X);
        when(coordinate.getY()).thenReturn(Y);

        String result = stringJtsCoordinateMapper.map(coordinate);
        assertThat(result).isEqualTo("5.123456,52.123456");
    }

    @Test
    void map_coordinate_null() {
        Coordinate coordinate = null;

        assertThatThrownBy(() -> stringJtsCoordinateMapper.map(coordinate))
                .isInstanceOf(NullPointerException.class);
    }



}