package nu.ndw.nls.geometry.jts.mappers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.mappers.JtsCoordinateMapper;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeoJsonJtsLineStringMapper {

    private final JtsCoordinateMapper jtsCoordinateMapper;

    /**
     * Maps geojson double coordinate arrays into a line string and allows a custom geometry factory. Should only be
     * used when using a different projection than wgs84 or rijksdriehoek, for which you can use the convenience
     * mappers: {@link GeoJsonJtsLineStringMapperWgs84} and {@link GeoJsonJtsLineStringMapperRijksdriehoek}
     *
     * @param geometryFactory geometry factory in the projection that you require
     * @param geoJsonCoordinates geojson coordinates
     * @return
     */
    public LineString map(GeometryFactory geometryFactory, List<List<Double>> geoJsonCoordinates) {
        Coordinate[] coordinates = jtsCoordinateMapper.mapToCoordinates(geoJsonCoordinates);
        return geometryFactory.createLineString(coordinates);
    }

}
