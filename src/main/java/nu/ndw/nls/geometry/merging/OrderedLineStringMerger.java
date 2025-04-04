package nu.ndw.nls.geometry.merging;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.crs.CrsValidator;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateList;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Component;

/**
 * Merges a list of LineString geometries to a single LineString. The LineStrings must already be in correct sequential order and must
 * connect (with a small distance tolerance).
 */
@Component
@RequiredArgsConstructor
public class OrderedLineStringMerger {

    private static final double TOLERANCE = 0.000001;

    private final GeometryFactoryWgs84 geometryFactoryWgs84;

    public Optional<LineString> merge(List<LineString> lineStrings) {
        if (lineStrings.isEmpty()) {
            return Optional.empty();
        }

        CoordinateList mergedCoordinates = new CoordinateList();

        for (int i = 0; i < lineStrings.size(); i++) {
            LineString lineString = lineStrings.get(i);
            CrsValidator.validateWgs84(lineString);

            Coordinate[] coordinates = lineString.getCoordinates();
            if (i == 0) {
                mergedCoordinates.add(coordinates, false);
            } else {
                if (mergedCoordinates.getLast().distance(coordinates[0]) >= TOLERANCE) {
                    return Optional.empty();
                }

                // Skip the first coordinate to avoid duplication
                mergedCoordinates.add(coordinates, false, 1, coordinates.length);
            }
        }

        return Optional.of(geometryFactoryWgs84.createLineString(mergedCoordinates.toCoordinateArray()));
    }
}
