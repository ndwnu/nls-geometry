package nu.ndw.nls.geometry.distance;

import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.crs.CrsValidator;
import nu.ndw.nls.geometry.factories.GeodeticCalculatorFactory;
import org.geotools.referencing.GeodeticCalculator;
import org.locationtech.jts.densify.Densifier;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Component;

/**
 * Car A drives the original geometry, car B drives the matched geometry. They can vary their speed to stay as close to each other as
 * possible, but they cannot not drive backwards. Fréchet distance is the maximum distance the cars are apart over the whole trajectory.
 * Where the Hausdorff distance only finds the largest distance between the two lines, Fréchet also finds loops in the matched geometry.
 * <p>
 * We have our own implementation instead of using JTS, because the latter uses a more naive approach that goes out of memory for long
 * trajectories.
 */
@Component
@RequiredArgsConstructor
public class FrechetDistanceCalculator {

    private static final double POINT_DISTANCE_MAX_1_METRE = 0.00001;

    private final GeodeticCalculatorFactory geodeticCalculatorFactory;

    public double calculateFrechetDistanceInMetresFromWgs84(LineString p, LineString q) {
        CrsValidator.validateWgs84(p);
        CrsValidator.validateWgs84(q);

        // Because the algorithm advances one point of the line string at a time, a large distance between points distorts the outcome.
        // Densify both line strings to have at least one point per metre.
        LineString pDensified = densify(p);
        LineString qDensified = densify(q);

        int pLength = pDensified.getNumPoints();
        int qLength = qDensified.getNumPoints();

        GeodeticCalculator geodeticCalculator = this.geodeticCalculatorFactory.createGeodeticCalculator();

        int i = 0;
        int j = 0;
        double maxDist = 0;

        while (i < pLength - 1 || j < qLength - 1) {
            Coordinate currP = pDensified.getCoordinateN(i);
            Coordinate currQ = qDensified.getCoordinateN(j);
            double distCurrCurr = FractionAndDistanceCalculator.calculateDistance(currP, currQ, geodeticCalculator);
            maxDist = Math.max(maxDist, distCurrCurr);

            // Determine whether to advance on p, q or both.
            if (i < pLength - 1 && j < qLength - 1) {
                // Look ahead and choose the advancing option with the smallest distance.
                Coordinate nextP = pDensified.getCoordinateN(i + 1);
                Coordinate nextQ = qDensified.getCoordinateN(j + 1);
                double distNextCurr = FractionAndDistanceCalculator.calculateDistance(nextP, currQ, geodeticCalculator);
                double distCurrNext = FractionAndDistanceCalculator.calculateDistance(currP, nextQ, geodeticCalculator);
                double distNextNext = FractionAndDistanceCalculator.calculateDistance(nextP, nextQ, geodeticCalculator);

                if (distNextNext <= distNextCurr && distNextNext <= distCurrNext) {
                    // Advance on both p and q.
                    i++;
                    j++;
                } else if (distNextCurr < distCurrNext) {
                    // Advance on p.
                    i++;
                } else {
                    // Advance on q.
                    j++;
                }
            } else if (i < pLength - 1) {
                // Only p can advance.
                i++;
            } else {
                // Only q can advance.
                j++;
            }
        }

        return maxDist;
    }

    private static LineString densify(LineString l) {
        Densifier densifier = new Densifier(l);
        densifier.setDistanceTolerance(POINT_DISTANCE_MAX_1_METRE);
        return (LineString) densifier.getResultGeometry();
    }
}
