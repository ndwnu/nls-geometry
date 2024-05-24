package nu.ndw.nls.geometry.mappers;

import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.crs.CrsTransformer;
import nu.ndw.nls.geometry.factories.GeometryFactoryRijksdriehoek;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiameterToPolygonMapper {

    private static final int NUM_POINTS = 100;

    private final CrsTransformer crsTransformer;
    private final GeometryFactoryRijksdriehoek geometryFactoryRijksdriehoek;

    public Polygon mapToPolygonWgs84(Point point, double diameterInMeters) {
        var shapeFactory = new GeometricShapeFactory(geometryFactoryRijksdriehoek);
        Point pointRd = (Point) crsTransformer.transformFromWgs84ToRdNew(point);
        shapeFactory.setCentre(new Coordinate(pointRd.getX(), pointRd.getY()));
        shapeFactory.setNumPoints(NUM_POINTS);
        shapeFactory.setWidth(diameterInMeters);
        shapeFactory.setHeight(diameterInMeters);
        Polygon ellipseRd = shapeFactory.createEllipse();
        return (Polygon) crsTransformer.transformFromRdNewToWgs84(ellipseRd);
    }
}
