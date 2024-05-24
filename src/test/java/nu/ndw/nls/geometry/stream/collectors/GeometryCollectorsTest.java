package nu.ndw.nls.geometry.stream.collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.operation.linemerge.LineMerger;

class GeometryCollectorsTest {

    @Test
    void mergedToLineStrings_ok() {
        Collector<LineString, LineMerger, List<LineString>> mergedLineStrings =
                GeometryCollectors.mergedToLineStrings();
        assertNotNull(mergedLineStrings);
        assertEquals(LineStringMergerCollector.class, mergedLineStrings.getClass());
    }

    @Test
    void mergeToLineString_ok() {
        Collector<LineString, LineMerger, Optional<LineString>> lineStringCollector =
                GeometryCollectors.mergeToLineString();
        assertNotNull(lineStringCollector);
        assertEquals(ToSingleLineStringMergerCollector.class, lineStringCollector.getClass());
    }
}
