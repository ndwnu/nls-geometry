package nu.ndw.nls.geometry.jts.mappers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.factories.GeometryFactoryRijksdriehoek;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeoJsonJtsLineStringMapperRijksdriehoek {

    private final GeometryFactoryRijksdriehoek geometryFactoryRijksdriehoek;

    private final GeoJsonJtsLineStringMapper geoJsonJtsLineStringMapper;

    public LineString map(List<List<Double>> geoJsonCoordinates) {
        return geoJsonJtsLineStringMapper.map(geometryFactoryRijksdriehoek, geoJsonCoordinates);
    }

}
