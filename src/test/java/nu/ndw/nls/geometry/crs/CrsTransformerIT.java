package nu.ndw.nls.geometry.crs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import nu.ndw.nls.geometry.GeometryConfiguration;
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
@ContextConfiguration(classes = {GeometryConfiguration.class})
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
        assertThat(pointRd.getX()).isCloseTo(155000, within(0.01));
        assertThat(pointRd.getY()).isCloseTo(463000, within(0.01));
    }

    @Test
    void transformFromWgs84ToRdNew_exception() {
        Point pointWgs84 = wgs84GeometryFactory.createPoint(new Coordinate(5.3872036, 90.1551723));
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> crsTransformer.transformFromWgs84ToRdNew(pointWgs84));
    }

    @Test
    void transformFromRdNewToWgs84_ok() {
        Point pointRd = rdNewGeometryFactory.createPoint(new Coordinate(155000, 463000));
        Point pointWgs84 = (Point) crsTransformer.transformFromRdNewToWgs84(pointRd);
        assertThat(pointWgs84.getX()).isCloseTo(5.3872036, within(0.0000001));
        assertThat(pointWgs84.getY()).isCloseTo(52.1551723, within(0.0000001));
    }

    @Test
    void transformFromRdNewToWgs84_exception() {
        Point pointRd = rdNewGeometryFactory.createPoint(new Coordinate(155001, 463001));
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> crsTransformer.transformFromRdNewToWgs84(pointRd));
    }
}
