package nu.ndw.nls.geometry.factories;

import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.linearref.LocationIndexedLine;
import org.springframework.stereotype.Component;

@Component
public class LocationIndexedLineFactory {

    public LocationIndexedLine createLocationIndexedLine(LineString lineString) {
        return new LocationIndexedLine(lineString);
    }
}
