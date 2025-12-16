package nu.ndw.nls.geometry.jts.matchers;

import static org.locationtech.jts.geom.util.LineStringExtracter.getLines;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.crs.CrsTransformer;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MultiLineStringPointGeometryMatcher {

    private final CrsTransformer crsTransformer;

    /**
     * Matches a point to a line from a MultiLineString that falls within a specified buffer distance. Transforms the point and lines to an
     * RD coordinate reference system to ensure buffer distances in metres. Then checks if the point is near any line within the buffer
     * distance and returns the matching line transformed back to the original reference system.
     *
     * @param multiLineString the MultiLineString containing lines to search for a match
     * @param point           the Point to be matched to a line
     * @param bufferInMetres  the buffer distance in meters within which the point should match a line
     * @return the matched LineString if the point falls within the buffer distance of a line
     * @throws IllegalStateException if no matching line is found for the given point
     */
    public LineString matchPointOnMultiLine(MultiLineString multiLineString, Point point, int bufferInMetres) {
        Point pointRd = (Point) crsTransformer.transformFromWgs84ToRdNew(point);
        //noinspection unchecked
        List<Geometry> lines = getLines(multiLineString);
        return lines
                .stream()
                .map(crsTransformer::transformFromWgs84ToRdNew)
                .filter(line -> isPointNearLineWithBuffer(line, pointRd, bufferInMetres))
                .findFirst()
                .map(crsTransformer::transformFromRdNewToWgs84)
                .map(LineString.class::cast)
                .orElseThrow(() -> new IllegalStateException("No matching line found for point %s".formatted(point)));
    }

    private boolean isPointNearLineWithBuffer(Geometry line, Point pointRd, int bufferInMetres) {
        return line.buffer(bufferInMetres)
                .contains(pointRd);
    }
}
