package nu.ndw.nls.geometry.constants;

import java.util.Arrays;

/**
 * A spatial reference identifier (SRID) Enum.
 */
public enum SRID {
    WGS84(4326),
    RIJKSDRIEHOEK(28992);

    public final int value;

    SRID(int value) {
        this.value = value;
    }

    public static SRID fromValue(int value) {
        return Arrays.stream(SRID.values())
                .filter(s -> s.value == value)
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("Invalid SRID: " + value));

    }
}
