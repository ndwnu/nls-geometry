package nu.ndw.nls.geometry.mappers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JtsPolylineEncoderMapper {

    private static final int FIVE_BIT_SHIFT = 32;

    private static final int CHAR_OFFSET = 63;

    public String map(@NonNull LineString lineString) {
        long lastLat = 0;
        long lastLng = 0;
        StringBuilder result = new StringBuilder();
        var coordinates = lineString.getCoordinates();
        for (Coordinate coordinate : coordinates) {
            long lat = Math.round(coordinate.getY() * 1e5);
            long lng = Math.round(coordinate.getX() * 1e5);
            long dLat = lat - lastLat;
            long dLng = lng - lastLng;
            encode(dLat, result);
            encode(dLng, result);
            lastLat = lat;
            lastLng = lng;
        }
        return result.toString();
    }

    private static void addCharacter(StringBuilder result, long v) {
        result.append(Character.toChars((int) (v + CHAR_OFFSET)));
    }

    private void encode(long v, StringBuilder result) {
        v = v < 0 ? ~(v << 1) : (v << 1);
        while (v >= FIVE_BIT_SHIFT) {
            addCharacter(result, v % FIVE_BIT_SHIFT + FIVE_BIT_SHIFT);
            v = v / FIVE_BIT_SHIFT;
        }
        addCharacter(result, v);
    }
}
