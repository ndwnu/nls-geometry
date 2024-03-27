package nu.ndw.nls.geometry.geojson.mappers;


import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeoJsonLineStringCoordinateMapper {

    private final GeoJsonCoordinateMapper geoJsonCoordinateMapper;

    public List<List<Double>> map(LineString lineString) {
        return Arrays.stream(lineString.getCoordinates())
                .map(geoJsonCoordinateMapper::map)
                .toList();
    }

}
