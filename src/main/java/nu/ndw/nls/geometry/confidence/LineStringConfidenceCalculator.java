package nu.ndw.nls.geometry.confidence;

import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.distance.FrechetDistanceCalculator;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LineStringConfidenceCalculator {

    private static final int MIN_CONFIDENCE_SCORE = 0;
    private static final int MAX_CONFIDENCE_SCORE = 100;
    private static final double DISTANCE_PENALTY_FACTOR = 1.5;

    private final FrechetDistanceCalculator frechetDistanceCalculator;

    public double calculateLineStringConfidenceScore(LineString originalGeometry, LineString matchGeometry) {
        double maximumDistanceInMeters = frechetDistanceCalculator.calculateFrechetDistanceInMetresFromWgs84(originalGeometry,
                matchGeometry);

        double score = MAX_CONFIDENCE_SCORE - (DISTANCE_PENALTY_FACTOR * maximumDistanceInMeters);
        return Math.max(MIN_CONFIDENCE_SCORE, score);
    }
}
