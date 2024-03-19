package nu.ndw.nls.geometry.constants;

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
}