package nu.ndw.nls.geometry.mappers;

import com.mapbox.geojson.utils.PolylineUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JtsPolylineDecoderMapper {

    private static final int DECODE_PRECISION_5_DECIMALS = 5;
    private static final double SIMPLIFY_TOLERANCE_5_DECIMALS = Math.pow(10, -1.0 * DECODE_PRECISION_5_DECIMALS);
    private static final boolean SIMPLIFY_HIGHEST_QUALITY = true;

    private final GeometryFactoryWgs84 geometryFactoryWgs84;

    public LineString map(Object geometry) {
        if (geometry instanceof String string) {
            List<com.mapbox.geojson.Point> points =
                    PolylineUtils.decode(string, DECODE_PRECISION_5_DECIMALS);
            Coordinate[] coordinates =
                    PolylineUtils.simplify(points, SIMPLIFY_TOLERANCE_5_DECIMALS, SIMPLIFY_HIGHEST_QUALITY)
                            .stream()
                            .map(p -> new Coordinate(p.longitude(), p.latitude()))
                            .toArray(Coordinate[]::new);

            return geometryFactoryWgs84.createLineString(coordinates);
        }

        throw new IllegalStateException("Geometry is not a valid encoded polyline: " + geometry);
    }
}
