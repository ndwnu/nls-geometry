package nu.ndw.nls.geometry.factories;

import nu.ndw.nls.geometry.constants.SRID;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.springframework.stereotype.Component;

@Component
public class GeodeticCalculatorFactory {

    public GeodeticCalculator createGeodeticCalculator(SRID srid) {

        try {
            CoordinateReferenceSystem crs = CRS.decode("EPSG:" + srid.value);
            return new GeodeticCalculator(crs);
        } catch (FactoryException e) {
            throw new IllegalArgumentException(e);
        }

    }

}
