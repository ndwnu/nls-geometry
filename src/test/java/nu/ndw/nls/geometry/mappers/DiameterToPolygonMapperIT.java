package nu.ndw.nls.geometry.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import nu.ndw.nls.geometry.config.GeometryMapperConfiguration;
import nu.ndw.nls.geometry.constants.SRID;
import nu.ndw.nls.geometry.factories.GeodeticCalculatorFactory;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.assertj.core.data.Offset;
import org.geotools.referencing.GeodeticCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GeometryMapperConfiguration.class})
class DiameterToPolygonMapperIT {

    @Autowired
    private DiameterToPolygonMapper diameterToPolygonMapper;
    @Autowired
    private GeodeticCalculatorFactory geodeticCalculatorFactory;
    @Autowired
    private GeometryFactoryWgs84 geometryFactoryWgs84;

    @Test
    void mapToPolygonWgs84_ok() {
        Point centerPoint = geometryFactoryWgs84.createPoint(new Coordinate(4.893394, 52.373239));
        double diameterInMeters = 50;
        Polygon polygon = diameterToPolygonMapper.mapToPolygonWgs84(centerPoint, diameterInMeters);
        Coordinate coordinate = polygon.getCoordinate();
        GeodeticCalculator geodeticCalculator = geodeticCalculatorFactory.createGeodeticCalculator(SRID.WGS84);
        geodeticCalculator.setStartingGeographicPoint(centerPoint.getX(), centerPoint.getY());
        geodeticCalculator.setDestinationGeographicPoint(coordinate.getX(), coordinate.getY());
        assertThat(geodeticCalculator.getOrthodromicDistance()).isCloseTo(25, Offset.offset(0.005));
    }
}
