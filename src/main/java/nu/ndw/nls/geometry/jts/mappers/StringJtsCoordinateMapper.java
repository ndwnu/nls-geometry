package nu.ndw.nls.geometry.jts.mappers;

import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StringJtsCoordinateMapper {

    /**
     * Maps a list of JTS coordinates to a string format that can be used as a path parameter
     * @param coordinates Jts coordinates
     * @return A string 5.383333,52.15;5.417114349703827,52.153908390414884
     */
    public String map(@NonNull List<Coordinate> coordinates) {
        return coordinates
                .stream()
                .map(this::mapCoordinateToCommmaSeparatedString)
                .collect(Collectors.joining(";"));
    }

    /**
     * Maps a JTS coordinate to a string format that can be used as a path parameter
     * @param coordinate JTS coordinate
     * @return A string of longitude latitude, separated by a comma. Example: 5.417114349703827,52.153908390414884
     */
    public String map(@NonNull Coordinate coordinate) {
        return this.mapCoordinateToCommmaSeparatedString(coordinate);
    }

    private String mapCoordinateToCommmaSeparatedString(Coordinate coordinate) {
        return this.mapCoordinateToList(coordinate)
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    private List<Double> mapCoordinateToList(Coordinate coordinate) {
        return List.of(coordinate.getX(), coordinate.getY());
    }

}
