package nu.ndw.nls.geometry.crs;

import nu.ndw.nls.geometry.constants.SRID;
import org.locationtech.jts.geom.Geometry;

public final class CrsValidator {

    private CrsValidator() {
        // Util class
    }

    public static void validateWgs84(Geometry geometry) {
        SRID srid = geometry.getSRID() == 0 ? SRID.WGS84 : SRID.fromValue(geometry.getSRID());
        if (srid != SRID.WGS84) {
            throw new IllegalArgumentException("SRID must be WGS84 and is %s".formatted(srid));
        }
    }
}
