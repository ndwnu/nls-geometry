package nu.ndw.nls.geometry.mappers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LineStringMapper {
    private final CoordinateMapper coordinateMapper;
    private final GeometryFactoryWgs84 geometryFactoryWgs84;
    public Coordinate[][] createMultiLineCoordinates(List<List<List<Double>>> multiLineAsDoubles) {
        return multiLineAsDoubles
                .stream()
                .map(coordinateMapper::mapToCoordinates)
                .toList().toArray(new Coordinate[0][0]);
    }

    public MultiLineString createMultiLineString(List<List<Coordinate>> multiLineCoordinates) {
        List<LineString> lineStrings = multiLineCoordinates
                .stream()
                .map(ls -> geometryFactoryWgs84.createLineString(ls.toArray(new Coordinate[0])))
                .toList();
        return geometryFactoryWgs84.createMultiLineString(lineStrings.toArray(LineString[]::new));
    }
}
