package nu.ndw.nls.geometry.distance;

import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.constants.SRID;
import nu.ndw.nls.geometry.crs.CrsTransformer;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.operation.distance.DistanceOp;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DistanceCalculator {

    private final CrsTransformer crsTransformer;

    /**
     * Calculates the distance between a point and a line string. Both geometries have to be in WGS84 format.
     *
     * @param sourcePoint      the source point for the calculation
     * @param targetLineString the target line string for the calculation
     * @return the distance in meters
     * @implNote Calculates the distance by first transforming the geometries to Rijksdriehoek. Because this CRS expresses points in meters,
     * the JTS distance calculation will be more accurate than calculating the distance on the original geometries. A calculation by
     * snapping the source point to the line string and then calculating the distance from the projected point to the line string with a
     * geodetic calculator will introduce a noticeable error.
     */
    public double calculateDistance(Point sourcePoint, LineString targetLineString) {
        validateWGS84(sourcePoint);
        validateWGS84(targetLineString);

        Point sourcePointRD = (Point) crsTransformer.transformFromWgs84ToRdNew(sourcePoint);
        LineString targetLineStringRD = (LineString) crsTransformer.transformFromWgs84ToRdNew(targetLineString);

        return DistanceOp.distance(sourcePointRD, targetLineStringRD);
    }

    private void validateWGS84(Geometry geometry) {
        SRID srid = geometry.getSRID() == 0 ? SRID.WGS84 : SRID.fromValue(geometry.getSRID());
        if (srid != SRID.WGS84) {
            throw new IllegalArgumentException("SRID must be WGS84 and is %s".formatted(srid));
        }
    }
}
