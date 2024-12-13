package nu.ndw.nls.geometry.factories;

import org.geotools.referencing.GeodeticCalculator;
import org.springframework.stereotype.Component;

@Component
public class GeodeticCalculatorFactory {

    public GeodeticCalculator createGeodeticCalculator() {
        return new GeodeticCalculator();
    }

}
