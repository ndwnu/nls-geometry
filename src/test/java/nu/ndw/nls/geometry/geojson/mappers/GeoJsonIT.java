package nu.ndw.nls.geometry.geojson.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.io.WKTReader;

class GeoJsonIT {
    private GeoJsonMultiLineStringCoordinateMapper geoJsonMultiLineStringCoordinateMapper =
            new GeoJsonMultiLineStringCoordinateMapper(
                    new GeoJsonLineStringCoordinateMapper(
                            new GeoJsonCoordinateMapper()));

    @Test
    @SneakyThrows
    void map_ok() {
        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read("MULTILINESTRING ((10 10, 20 20, 10 40),(40 40, 30 30, 40 20, 30 10))");

        if (geometry instanceof MultiLineString multiLineString) {
            List<List<Double>> lineStringA = List.of(List.of(10D, 10D), List.of(20D, 20D), List.of(10D, 40D));
            List<List<Double>> lineStringB = List.of(List.of(40D, 40D), List.of(30D, 30D), List.of(40D, 20D),
                    List.of(30D, 10D));

            assertEquals(List.of(lineStringA, lineStringB), geoJsonMultiLineStringCoordinateMapper.map(multiLineString));
        } else {
            throw new IllegalArgumentException("Expecting multi line string");
        }



    }
}