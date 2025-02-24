package nu.ndw.nls.geometry.confidence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import nu.ndw.nls.geometry.distance.FrechetDistanceCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.LineString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LineStringReliabilityCalculatorTest {

    @Mock
    private FrechetDistanceCalculator frechetDistanceCalculator;
    @Mock
    private LineString originalGeometry;
    @Mock
    private LineString matchGeometry;

    @InjectMocks
    private LineStringReliabilityCalculator lineStringReliabilityCalculator;

    @Test
    void calculateLineStringReliability_ok() {
        when(frechetDistanceCalculator.calculateFrechetDistanceInMetresFromWgs84(originalGeometry, matchGeometry)).thenReturn(10.0);
        assertEquals(85.0, lineStringReliabilityCalculator.calculateLineStringReliability(originalGeometry, matchGeometry));
    }

    @Test
    void calculateLineStringReliability_ok_minimumValue() {
        when(frechetDistanceCalculator.calculateFrechetDistanceInMetresFromWgs84(originalGeometry, matchGeometry)).thenReturn(1000.0);
        assertEquals(0.0, lineStringReliabilityCalculator.calculateLineStringReliability(originalGeometry, matchGeometry));
    }
}
