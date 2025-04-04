package nu.ndw.nls.geometry.merging;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

class OrderedLineStringMergerTest {

    private final GeometryFactoryWgs84 geometryFactoryWgs84 = new GeometryFactoryWgs84();
    private final OrderedLineStringMerger orderedLineStringMerger = new OrderedLineStringMerger(geometryFactoryWgs84);

    @Test
    void merge_ok() {
        LineString lineString1 = geometryFactoryWgs84.createLineString(new Coordinate[]{new Coordinate(6.0163605, 52.1854891),
                new Coordinate(6.0167396, 52.1855713)});
        LineString lineString2 = geometryFactoryWgs84.createLineString(new Coordinate[]{new Coordinate(6.0167397, 52.1855709),
                new Coordinate(6.0169488, 52.1856119)});

        Optional<LineString> result = orderedLineStringMerger.merge(List.of(lineString1, lineString2));

        LineString expected = geometryFactoryWgs84.createLineString(new Coordinate[]{new Coordinate(6.0163605, 52.1854891),
                new Coordinate(6.0167396, 52.1855713), new Coordinate(6.0169488, 52.1856119)});
        assertThat(result).contains(expected);
    }

    @Test
    void merge_ok_empty() {
        Optional<LineString> result = orderedLineStringMerger.merge(List.of());

        assertThat(result).isEmpty();
    }

    @Test
    void merge_ok_exceedsTolerance() {
        LineString lineString1 = geometryFactoryWgs84.createLineString(new Coordinate[]{new Coordinate(6.0163605, 52.1854891),
                new Coordinate(6.0167396, 52.1855713)});
        LineString lineString2 = geometryFactoryWgs84.createLineString(new Coordinate[]{new Coordinate(6.0167396, 52.1855702),
                new Coordinate(6.0169488, 52.1856119)});

        Optional<LineString> result = orderedLineStringMerger.merge(List.of(lineString1, lineString2));

        assertThat(result).isEmpty();
    }
}
