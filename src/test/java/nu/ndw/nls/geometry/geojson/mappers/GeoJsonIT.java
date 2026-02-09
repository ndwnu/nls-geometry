package nu.ndw.nls.geometry.geojson.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import lombok.SneakyThrows;
import nu.ndw.nls.geometry.config.GeoJsonMapperConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GeoJsonMapperConfiguration.class})
class GeoJsonIT {

    @Autowired
    private GeoJsonMultiLineStringCoordinateMapper geoJsonMultiLineStringCoordinateMapper;

    @Test
    @SneakyThrows
    void map_ok() {
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read("MULTILINESTRING ((10 10, 20 20, 10 40),(40 40, 30 30, 40 20, 30 10))");

        if (geometry instanceof MultiLineString multiLineString) {
            List<List<Double>> lineStringA = List.of(List.of(10D, 10D), List.of(20D, 20D), List.of(10D, 40D));
            List<List<Double>> lineStringB = List.of(List.of(40D, 40D), List.of(30D, 30D), List.of(40D, 20D),
                    List.of(30D, 10D));

            List<List<List<Double>>> coordinates = geoJsonMultiLineStringCoordinateMapper.map(multiLineString);

            assertThat(coordinates).isEqualTo(List.of(lineStringA, lineStringB));
        } else {
            throw new IllegalArgumentException("Expecting multi line string");
        }
    }
}
