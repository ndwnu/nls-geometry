package nu.ndw.nls.geometry.geojson.mappers;


import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Component;

@Component
public class GeoJsonCoordinateMapper {
    public List<Double> map(Coordinate coordinate) {
        return List.of(coordinate.getX(), coordinate.getY());
    }

}
