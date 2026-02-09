package nu.ndw.nls.geometry.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.WKTReader;

@RequiredArgsConstructor
class JtsPolylineEncoderMapperTest {

    private static final String WKT_LINESTRING = "LINESTRING (5.09125 51.5595, 5.09113 51.55925, 5.0906 51.55848, 5.09059 51.55844)";

    private static final String POLYLINE = "{euyHika^p@VxChBF@";

    private final JtsPolylineEncoderMapper mapper = new JtsPolylineEncoderMapper();

    @Test
    void map() {
        String result = mapper.map(createLineString());
        assertThat(result).isEqualTo(POLYLINE);
    }

    @Test
    void map_null() {
        assertThatThrownBy(() -> mapper.map(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void encodeLineString_encodeDecodeIsEqualResult() {
        LineString lineStringInput = createLineString();
        String polyLineResult = mapper.map(lineStringInput);

        JtsPolylineDecoderMapper polylineDecoderMapper = new JtsPolylineDecoderMapper(new GeometryFactoryWgs84());
        LineString lineStringResult = polylineDecoderMapper.map(polyLineResult);

        assertThat(lineStringResult).isEqualTo(lineStringInput);
    }

    @SneakyThrows
    private static LineString createLineString() {
        WKTReader wktReader = new WKTReader();
        return (LineString) wktReader.read(WKT_LINESTRING);
    }
}

