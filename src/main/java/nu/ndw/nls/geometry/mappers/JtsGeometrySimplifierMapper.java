package nu.ndw.nls.geometry.mappers;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.springframework.stereotype.Component;

@Component
public class JtsGeometrySimplifierMapper {

    public static final double DEFAULT_DISTANCE_TOLERANCE = 0.000005;

    public <T extends Geometry> T map(T lineString) {
        return map(DEFAULT_DISTANCE_TOLERANCE, lineString);
    }

    @SuppressWarnings("unchecked")
    public <T extends Geometry> T map(double distanceTolerance, T geometry) {
        return (T)DouglasPeuckerSimplifier.simplify(geometry, distanceTolerance);
    }
}
