package nu.ndw.nls.geometry.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import nu.ndw.nls.geometry.crs.CrsTransformer;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import nu.ndw.nls.geometry.mappers.JtsCoordinateMapper;
import nu.ndw.nls.geometry.mappers.JtsGeometrySimplifierMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

class ExtractGeometryServiceTest {
    private final GeometryFactoryWgs84 geometryFactoryWgs84 = new GeometryFactoryWgs84();
    private final JtsCoordinateMapper coordinateMapper = new JtsCoordinateMapper();
    private ExtractGeometryService extractGeometryService;
    @BeforeEach
    void setup () {
        extractGeometryService = new ExtractGeometryService(new CrsTransformer(), new JtsGeometrySimplifierMapper());
    }
    private static final List<List<Double>> ORIGINAL_COORDINATES = List.of(
            List.of(4.9643, 52.52468),
            List.of(4.96473, 52.52540),
            List.of(4.96503, 52.52588),
            List.of(4.9651, 52.52592),
            List.of(4.96515, 52.52598),
            List.of(4.96557, 52.52668),
            List.of(4.96562, 52.52674));

    private static final List<List<Double>>  EXPECTED_COORDINATES = List.of(
            List.of(4.9646112, 52.5252012),
            List.of(4.96503, 52.5258801),
            List.of(4.9651, 52.5259201),
            List.of(4.96515, 52.5259801),
            List.of(4.9652954, 52.5262224));

    @Test
    void extract_ok_zeroOffsets() {
        validate(ORIGINAL_COORDINATES, 0, 1);
    }

    @Test
    void extract_ok_useFractions() {
        validate(EXPECTED_COORDINATES, 0.25, 0.75);
    }

    @Test
    void extract_ok_useMeters() {
        LineString geometry = geometryFactoryWgs84.createLineString(coordinateMapper.mapToCoordinates(ORIGINAL_COORDINATES));
        Geometry extracted = extractGeometryService.extractByMetres(61.71229242182326, 185.13687726546976, geometry);
        List<List<Double>> actualCoordinates = coordinateMapper.mapToDoubles(extracted);
        assertEquals(EXPECTED_COORDINATES, actualCoordinates);
    }

    @Test
    void extract_ok_useMeters_rounded() {
        LineString geometry = geometryFactoryWgs84.createLineString(coordinateMapper.mapToCoordinates(ORIGINAL_COORDINATES));
        Geometry extracted = extractGeometryService.extractByMetres(61.71229242182326, 185.13687726546976, geometry);
        List<List<Double>> actualCoordinates = coordinateMapper.mapToDoubles(extracted);
        assertEquals(EXPECTED_COORDINATES, actualCoordinates);
    }

    @Test
    void extract_simplify_ok_rounded() {
        LineString geometry = geometryFactoryWgs84.createLineString(coordinateMapper.mapToCoordinates(ORIGINAL_COORDINATES));
        Geometry extracted = extractGeometryService.simplifyAndTransformRdNewToWgs84Rounded(geometry, 6);
        List<List<Double>> actualCoordinates = coordinateMapper.mapToDoubles(extracted);
        assertEquals(List.of(List.of(3.313605, 47.975239), List.of(3.313605, 47.975239)), actualCoordinates);
    }

    private void validate(List<List<Double>> expectedCoordinates, double startPos, double endPos) {
        LineString geometry = geometryFactoryWgs84.createLineString(coordinateMapper.mapToCoordinates(ORIGINAL_COORDINATES));
        Geometry extracted = extractGeometryService.extractByFractions(startPos, endPos, geometry);
        List<List<Double>> actualCoordinates = coordinateMapper.mapToDoubles(extracted);

        for (int i = 0; i < expectedCoordinates.size(); i++){
            assertThat(expectedCoordinates.get(i).get(0)).isCloseTo(actualCoordinates.get(i).get(0), within(0.0000001));
            assertThat(expectedCoordinates.get(i).get(1)).isCloseTo(actualCoordinates.get(i).get(1), within(0.0000001));
        }
    }
}
