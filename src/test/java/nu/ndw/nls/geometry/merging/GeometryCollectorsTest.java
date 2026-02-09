package nu.ndw.nls.geometry.merging;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(mergedLineStrings).isNotNull();
        assertThat(mergedLineStrings.getClass()).isEqualTo(LineStringMergerCollector.class);
    }

    @Test
    void mergeToLineString_ok() {
        Collector<LineString, LineMerger, Optional<LineString>> lineStringCollector =
                GeometryCollectors.mergeToLineString();
        assertThat(lineStringCollector).isNotNull();
        assertThat(lineStringCollector.getClass()).isEqualTo(ToSingleLineStringMergerCollector.class);
    }
}
