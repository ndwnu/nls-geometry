package nu.ndw.nls.geometry.distance.model;

import lombok.Builder;
import org.locationtech.jts.geom.Coordinate;

@Builder
public record CoordinateAndBearing(Coordinate coordinate, double bearing) {

}
