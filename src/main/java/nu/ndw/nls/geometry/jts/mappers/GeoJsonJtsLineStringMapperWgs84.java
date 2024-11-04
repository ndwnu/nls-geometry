package nu.ndw.nls.geometry.jts.mappers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.factories.GeometryFactoryWgs84;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeoJsonJtsLineStringMapperWgs84 {

    private final GeometryFactoryWgs84 geometryFactoryWgs84;

    private final GeoJsonJtsLineStringMapper geoJsonJtsLineStringMapper;

    public LineString map(List<List<Double>> geoJsonCoordinates) {
        return geoJsonJtsLineStringMapper.map(geometryFactoryWgs84, geoJsonCoordinates);
    }

}
