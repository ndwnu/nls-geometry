package nu.ndw.nls.geometry.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiLineString;

class JtsLineStringMapperTest {
    private final JtsLineStringMapper lineStringMapper = new JtsLineStringMapper(new JtsCoordinateMapper(), new GeometryFactoryWgs84());

    private static final List<List<Double>> LINE_STRING_A = List.of(
            List.of(5.41711435, 52.15390839),
            List.of(5.383333, 52.15)
    );
    private static final List<List<Double>> LINE_STRING_B = List.of(
            List.of(5.89711322, 52.15578),
            List.of(5.672876, 52.154789)
    );

    private static final List<List<Coordinate>> multiLineCoordinates = List.of(
            List.of(new Coordinate(5.89711322, 52.15578), new Coordinate(5.93711478, 52.15987)),
            List.of(new Coordinate(5.672876, 52.154), new Coordinate(5.67, 52.153), new Coordinate(5.66231, 52.152))
    );

    @Test
    void createMultiLineCoordinates() {
        Coordinate[][] result = lineStringMapper.createMultiLineCoordinates(List.of(LINE_STRING_A, LINE_STRING_B));

        assertEquals(2, Arrays.stream(result).count());
        assertEquals(new Coordinate(5.672876, 52.154789), result[1][1]);
    }

    @Test
    void createMultiLineString() {
        MultiLineString result = lineStringMapper.createMultiLineString(multiLineCoordinates);
        multiLineCoordinates.stream()
                .flatMap(Collection::stream)
                .forEach(coordinate ->
                        assertTrue(Arrays.asList(result.getCoordinates()).contains(coordinate)));
    }
}
