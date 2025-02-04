package nu.ndw.nls.geometry.distance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import nu.ndw.nls.geometry.factories.GeodeticCalculatorFactory;
import nu.ndw.nls.geometry.factories.LocationIndexedLineFactory;
import org.geotools.referencing.GeodeticCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.linearref.LinearLocation;
import org.locationtech.jts.linearref.LocationIndexedLine;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DistanceCalculatorTest {

    @Mock
    private LocationIndexedLineFactory locationIndexedLineFactory;

    @Mock
    private GeodeticCalculatorFactory geodeticCalculatorFactory;

    @InjectMocks
    private DistanceCalculator distanceCalculator;

    @Test
    void calculateDistance_ok() {
        double pointX = 5.42670371, pointY = 52.17673587;
        double snappedX = 5.42672895, snappedY = 52.17670980;

        LineString lineString = mock(LineString.class);

        Coordinate sourceCoordinate = newCoordinate(pointX, pointY);

        LocationIndexedLine locationIndexedLine = mock(LocationIndexedLine.class);
        LinearLocation linearLocation = mock(LinearLocation.class);
        Coordinate snappedCoordinate = newCoordinate(snappedX, snappedY);
        when(linearLocation.getCoordinate(lineString)).thenReturn(snappedCoordinate);
        when(locationIndexedLine.project(sourceCoordinate)).thenReturn(linearLocation);
        when(locationIndexedLineFactory.createLocationIndexedLine(lineString)).thenReturn(locationIndexedLine);

        GeodeticCalculator geodeticCalculator = mock(GeodeticCalculator.class);
        when(geodeticCalculatorFactory.createGeodeticCalculator()).thenReturn(geodeticCalculator);

        when(geodeticCalculator.getOrthodromicDistance()).thenReturn(13.37);

        double result = distanceCalculator.calculateDistance(sourceCoordinate, lineString);
        assertThat(result).isEqualTo(13.37);

        verify(geodeticCalculator).setStartingGeographicPoint(pointX, pointY);
        verify(geodeticCalculator).setDestinationGeographicPoint(snappedX, snappedY);
    }

    private static Coordinate newCoordinate(double snappedX, double snappedY) {
        Coordinate coordinate = mock(Coordinate.class);
        when(coordinate.getX()).thenReturn(snappedX);
        when(coordinate.getY()).thenReturn(snappedY);
        return coordinate;
    }
}
