package nu.ndw.nls.geometry.merging;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.operation.linemerge.LineMerger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeometryCollectors {

    /**
     * Uses {@link LineStringMergerCollector} to merge multiple {@link LineString} objects into a list of {@link LineString}s. If all lines
     * connect seamlessly, then a list with a single {@link LineString} is returned.
     *
     * @return {@link List} of {@link LineString}.
     */
    public static Collector<LineString, LineMerger, List<LineString>> mergedToLineStrings() {
        return new LineStringMergerCollector();
    }

    /**
     * Uses {@link ToSingleLineStringMergerCollector} to merge multiple {@link LineString} objects, that can seamlessly create a single line
     * string, into a single {@link LineString}. An empty result is returned if the line cannot be merged into a single line string.
     *
     * @return {@link Optional} of {@link LineString}. {@link Optional#empty()} is returned if line string merge resulted into 0 results or
     * multiple {@link LineString}s.
     */
    public static Collector<LineString, LineMerger, Optional<LineString>> mergeToLineString() {
        return new ToSingleLineStringMergerCollector();
    }
}
