package nu.ndw.nls.geometry.bearing.client;

public final class BearingValidator {

    private static final int MIN_BEARING = 0;
    private static final int MAX_BEARING = 360;
    private static final int MIN_RANGE = 0;
    private static final int MAX_RANGE = 180;

    private BearingValidator() {
    }

    public static void validate(Integer bearing, Integer range) {
        if (bearing == null && range != null) {
            throw new IllegalArgumentException("If bearingTarget is null bearingCutoffMargin should be null as well");
        } else if (bearing != null && range == null) {
            throw new IllegalArgumentException("If bearingCutoffMargin is null bearingTarget should be null as well");
        }
        if (bearing == null) {
            return;
        }
        boolean bearingInvalid = bearing < MIN_BEARING || bearing > MAX_BEARING;
        boolean rangeInvalid = range < MIN_RANGE || range > MAX_RANGE;

        if (bearingInvalid || rangeInvalid) {
            StringBuilder sb = new StringBuilder();
            if (bearingInvalid) {
                sb.append("Bearing %d exceeded valid range between %d and %d".formatted(
                        bearing, MIN_BEARING, MAX_BEARING));
            }

            if (rangeInvalid) {
                if (bearingInvalid) {
                    sb.append(". ");
                }

                sb.append("Range %d exceeded valid range between %d and %d".formatted(
                        range, MIN_RANGE, MAX_RANGE));
            }

            throw new IllegalArgumentException(sb.toString());
        }

    }
}
