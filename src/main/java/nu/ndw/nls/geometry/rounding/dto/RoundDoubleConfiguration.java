package nu.ndw.nls.geometry.rounding.dto;

import java.math.RoundingMode;


public record RoundDoubleConfiguration(int roundingDecimalPlaces, RoundingMode roundingMode) {

    public static final RoundDoubleConfiguration ROUND_3_HALF_UP = new RoundDoubleConfiguration(3,
            RoundingMode.HALF_UP);

    public static final RoundDoubleConfiguration ROUND_7_HALF_UP = new RoundDoubleConfiguration(7,
            RoundingMode.HALF_UP);

}
