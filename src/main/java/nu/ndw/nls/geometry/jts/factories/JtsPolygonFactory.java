package nu.ndw.nls.geometry.jts.factories;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JtsPolygonFactory {

    /**
     * Creates a bounding box polygon based on two coordinates
     *
     * @param geometryFactory the factory
     * @param coordinateA one coordinate of the bounding box
     * @param coordinateB second coordinate of the bounding box
     * @return Polygon representing a bounding box
     */
    public Polygon createBoundingBox(GeometryFactory geometryFactory, Coordinate coordinateA, Coordinate coordinateB) {
        double lat1 = coordinateA.getY();
        double lon1 = coordinateA.getX();

        double lat2 = coordinateB.getY();
        double lon2 = coordinateB.getX();

        return geometryFactory.createPolygon(new Coordinate[]{
                new Coordinate(lon1, lat1),
                new Coordinate(lon1, lat2),
                new Coordinate(lon2, lat2),
                new Coordinate(lon2, lat1),
                new Coordinate(lon1, lat1)
            });
        }
}
