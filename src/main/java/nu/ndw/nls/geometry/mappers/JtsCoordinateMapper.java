package nu.ndw.nls.geometry.mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Component;

@Component
public class JtsCoordinateMapper {

    public Coordinate[] mapToCoordinates(List<List<Double>> coordinates) {
        return coordinates
                .stream()
                .map(it -> new Coordinate(it.getFirst(), it.get(1)))
                .toList().toArray(new Coordinate[0]);
    }

    public Coordinate[] mapToCoordinatesRounded(List<List<Double>> coordinates, Integer decimals) {
        return coordinates
                .stream()
                .map(it -> new Coordinate(round(it.getFirst(), decimals), round(it.get(1), decimals)))
                .toList().toArray(new Coordinate[0]);
    }

    public List<List<Double>> mapToDoubles(Geometry geometry) {
        if (Objects.nonNull(geometry)) {
            return mapFromCoordinates(geometry.getCoordinates());
        }
        return List.of();
    }

    public List<List<Double>> mapFromCoordinates(Coordinate[] coordinates) {
        return Arrays.stream(coordinates)
                .map(it -> List.of(it.x, it.y))
                .toList();
    }

    public List<List<Double>> mapToDoublesRounded(Geometry geometry, Integer decimals) {
        return Arrays.stream(geometry.getCoordinates())
                .map(it -> List.of(round(it.x, decimals), round(it.y, decimals)))
                .toList();
    }

    private Double round(Double number, Integer decimals) {
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(number)).setScale(decimals, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
