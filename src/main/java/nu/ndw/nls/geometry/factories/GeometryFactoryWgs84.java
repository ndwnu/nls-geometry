package nu.ndw.nls.geometry.factories;


import static nu.ndw.nls.geometry.constants.SRID.WGS84;
import static org.locationtech.jts.geom.PrecisionModel.FLOATING;

import java.io.Serial;
import nu.ndw.nls.geometry.constants.SRID;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;

@Component
public class GeometryFactoryWgs84 extends GeometryFactory  implements GeometryFactorySrid  {

    @Serial
    private static final long serialVersionUID = 1;

    public GeometryFactoryWgs84() {
        super(new PrecisionModel(FLOATING), WGS84.value);
    }

    @Override
    public SRID getSrid() {
        return WGS84;
    }
}
