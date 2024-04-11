package nu.ndw.nls.geometry.geojson.mappers;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Component;

@Component
public class GeoJsonCoordinateMapper {

    public List<Double> map(Coordinate coordinate) {
        return List.of(coordinate.getX(), coordinate.getY());
    }

    public String mapList(List<Coordinate> coordinates) {
        return Optional.ofNullable(coordinates)
                .orElse(List.of())
                .stream()
                .map(this::map)
                .map(this::mapCoordinate)
                .collect(Collectors.joining(";"));
    }

    private String mapCoordinate(List<Double> doubles) {
        return doubles.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

}
