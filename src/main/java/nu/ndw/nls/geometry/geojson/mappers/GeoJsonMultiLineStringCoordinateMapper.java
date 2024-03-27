package nu.ndw.nls.geometry.geojson.mappers;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeoJsonMultiLineStringCoordinateMapper {

    private final GeoJsonLineStringCoordinateMapper geoJsonLineStringCoordinateMapper;

    public List<List<List<Double>>> map(MultiLineString multiLineString) {

        int numGeometries = multiLineString.getNumGeometries();

        List<List<List<Double>>> multiLineStringCoordinates = new ArrayList<>();
        for (int i=0; i < numGeometries; i++) {
            Geometry geometry = multiLineString.getGeometryN(i);
            if (geometry instanceof LineString lineString) {
                multiLineStringCoordinates.add(geoJsonLineStringCoordinateMapper.map(lineString));
            } else {
                throw new IllegalArgumentException("MultiLineString returned non-LineString geometry: " +
                        geometry.getClass().getSimpleName());
            }
        }

        return multiLineStringCoordinates;
    }
}
