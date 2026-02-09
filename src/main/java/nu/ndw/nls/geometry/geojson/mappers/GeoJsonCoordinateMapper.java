package nu.ndw.nls.geometry.geojson.mappers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.rounding.dto.RoundDoubleConfiguration;
import nu.ndw.nls.geometry.rounding.mappers.RoundDoubleMapper;
import org.locationtech.jts.geom.Coordinate;
import org.mapstruct.Context;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeoJsonCoordinateMapper {

    private final RoundDoubleMapper roundDoubleMapper;


    public List<Double> map(Coordinate coordinate) {
        return List.of(coordinate.getX(), coordinate.getY());
    }

    public List<Double> map(Coordinate coordinate, @Context RoundDoubleConfiguration roundDoubleConfiguration) {
        return List.of( roundDoubleMapper.round(coordinate.getX(), roundDoubleConfiguration),
                        roundDoubleMapper.round(coordinate.getY(), roundDoubleConfiguration));
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
