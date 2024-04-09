package nu.ndw.nls.geometry.crs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nu.ndw.nls.geometry.TestConfig;
import nu.ndw.nls.geometry.factories.GeometryFactoryRijksdriehoek;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class CrsTransformerIT {

    @Autowired
    private CrsTransformer crsTransformer;
    @Autowired
    private GeometryFactoryWgs84 wgs84GeometryFactory;
    @Autowired
    private GeometryFactoryRijksdriehoek rdNewGeometryFactory;

    @Test
    void transformFromWgs84ToRdNew_ok() {
        Point pointWgs84 = wgs84GeometryFactory.createPoint(new Coordinate(5.3872036, 52.1551723));
        Point pointRd = (Point) crsTransformer.transformFromWgs84ToRdNew(pointWgs84);
        assertEquals(155000, pointRd.getX(), 0.01);
        assertEquals(463000, pointRd.getY(), 0.01);
    }

    @Test
    void transformFromWgs84ToRdNew_exception() {
        Point pointWgs84 = wgs84GeometryFactory.createPoint(new Coordinate(5.3872036, 90.1551723));
        assertThrows(IllegalStateException.class, () -> crsTransformer.transformFromWgs84ToRdNew(pointWgs84));
    }

    @Test
    void transformFromRdNewToWgs84_ok() {
        Point pointRd = rdNewGeometryFactory.createPoint(new Coordinate(155000, 463000));
        Point pointWgs84 = (Point) crsTransformer.transformFromRdNewToWgs84(pointRd);
        assertEquals(5.3872036, pointWgs84.getX(), 0.0000001);
        assertEquals(52.1551723, pointWgs84.getY(), 0.0000001);
    }

    @Test
    void transformFromRdNewToWgs84_exception() {
        Point pointRd = rdNewGeometryFactory.createPoint(new Coordinate(155001, 463001));
        assertThrows(IllegalStateException.class, () -> crsTransformer.transformFromRdNewToWgs84(pointRd));
    }
}
