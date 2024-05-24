package nu.ndw.nls.geometry.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;

class JtsCoordinateMapperTest {

    private static final List<List<Double>> ORIGINAL_COORDINATES = List.of(
            List.of(4.9643, 52.52468),
            List.of(4.96503, 52.52588));

    private final JtsCoordinateMapper coordinateMapper = new JtsCoordinateMapper();

    @Test
    void mapToCoordinatesRounded_ok(){
        Coordinate[] result = coordinateMapper.mapToCoordinatesRounded(ORIGINAL_COORDINATES, 3);
        assertEquals(4.964, result[0].x);
        assertEquals(52.526, result[1].y);
    }
}
