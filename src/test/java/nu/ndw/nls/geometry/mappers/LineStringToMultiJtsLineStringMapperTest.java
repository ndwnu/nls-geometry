package nu.ndw.nls.geometry.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.WKTReader;

class LineStringToMultiJtsLineStringMapperTest {

    private static final GeometryFactory WGS84_GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

    private final JtsLineStringToMultiLineStringMapper lineStringToMultiLineStringMapper =
            new JtsLineStringToMultiLineStringMapper();

    @Test
    @SneakyThrows
    void map_ok_lineStrings() {
        WKTReader wktReader = new WKTReader();
        LineString lineStringA = (LineString) wktReader.read("LINESTRING (10 10, 20 20, 10 40)");
        LineString lineStringB = (LineString) wktReader.read("LINESTRING (40 40, 30 30, 40 20, 30 10)");

        Optional<MultiLineString> result = lineStringToMultiLineStringMapper.map(List.of(lineStringA, lineStringB));

        MultiLineString expected = (MultiLineString)
                wktReader.read("MULTILINESTRING ((10 10, 20 20, 10 40),(40 40, 30 30, 40 20, 30 10))");

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    @SneakyThrows
    void map_ok() {
        WKTReader wktReader = new WKTReader();
        LineString lineStringA = (LineString) wktReader.read("LINESTRING (10 10, 20 20, 10 40)");
        LineString lineStringB = (LineString) wktReader.read("LINESTRING (40 40, 30 30, 40 20, 30 10)");

        MultiLineString result = lineStringToMultiLineStringMapper.map(WGS84_GEOMETRY_FACTORY,
                List.of(lineStringA, lineStringB));

        MultiLineString expected = (MultiLineString)
                wktReader.read("MULTILINESTRING ((10 10, 20 20, 10 40),(40 40, 30 30, 40 20, 30 10))");

        assertEquals(expected, result);
    }
}
