package nu.ndw.nls.geometry.jts.factories;

import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JtsPolygonFactoryWgs84 {

    private final GeometryFactoryWgs84 geometryFactoryWgs84;

    private final JtsPolygonFactory jtsPolygonFactory;

    /**
     * Creates a bounding box polygon based on two coordinates in wgs84
     *
     * @param coordinateA one coordinate of the bounding box
     * @param coordinateB second coordinate of the bounding box
     * @return Polygon representing a bounding box
     */
    public Polygon createBoundingBox(Coordinate coordinateA, Coordinate coordinateB) {
        return jtsPolygonFactory.createBoundingBox(geometryFactoryWgs84, coordinateA, coordinateB);
    }

}
