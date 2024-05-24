package nu.ndw.nls.geometry.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.LineString;

class JtsPolylineDecoderMapperTest {

    private static final String POLYLINE = "{euyHika^NFPFNFzBtAB@VN@@F@";
    private static final String WKT_RESULT = "LINESTRING (5.09125 51.5595, 5.09113 51.55925, 5.0906 51.55848, "
            + "5.09059 51.55844)";

    private final JtsPolylineDecoderMapper polylineDecoderMapper = new JtsPolylineDecoderMapper(
            new GeometryFactoryWgs84());

    @Test
    void map_ok() {
        LineString lineString = polylineDecoderMapper.map(POLYLINE);
        assertEquals(WKT_RESULT, lineString.toString());
    }
}
