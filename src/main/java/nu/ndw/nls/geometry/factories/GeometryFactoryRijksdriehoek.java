package nu.ndw.nls.geometry.factories;


import static nu.ndw.nls.geometry.constants.SRID.RIJKSDRIEHOEK;
import static org.locationtech.jts.geom.PrecisionModel.FLOATING;

import java.io.Serial;
import nu.ndw.nls.geometry.constants.SRID;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;



@Component
public class GeometryFactoryRijksdriehoek extends GeometryFactory implements GeometryFactorySrid  {

    @Serial
    private static final long serialVersionUID = 1;

    public GeometryFactoryRijksdriehoek() {
        super(new PrecisionModel(FLOATING), RIJKSDRIEHOEK.value);
    }

    @Override
    public SRID getSrid() {
        return RIJKSDRIEHOEK;
    }
}
