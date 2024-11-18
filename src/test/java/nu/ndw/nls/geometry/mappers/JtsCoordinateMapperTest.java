package nu.ndw.nls.geometry.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

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
    @Test
    void mapToDoubles_ok(){
        Coordinate[] coordinateArr = ORIGINAL_COORDINATES.stream()
                .map(it -> new Coordinate(it.getFirst(), it.getLast()))
                .toList().toArray(new Coordinate[0]);
        Geometry geometry = new GeometryFactoryWgs84().createLineString(
                new Coordinate[]{coordinateArr[0], coordinateArr[1]});

        List<List<Double>> result = coordinateMapper.mapToDoubles(geometry);

        assertEquals(ORIGINAL_COORDINATES, result);
    }


    @Test
    void mapToDoubles_ok_withEmptyGeometry(){
        Coordinate[] coordinateArr = new Coordinate[0];
        Geometry geometry = new GeometryFactory().createPolygon(coordinateArr);

        List<List<Double>> result = coordinateMapper.mapToDoubles(geometry);

        assertEquals(List.of(), result);
    }


    @Test
    void mapToDoubles_ok_withNullGeometry(){
        Geometry geometry = null;

        List<List<Double>> result = coordinateMapper.mapToDoubles(geometry);

        assertThat(result).isEmpty();
    }
}
