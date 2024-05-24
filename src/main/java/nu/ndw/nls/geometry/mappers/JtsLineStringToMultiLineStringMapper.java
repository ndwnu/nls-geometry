package nu.ndw.nls.geometry.mappers;

import java.util.List;
import java.util.Optional;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class JtsLineStringToMultiLineStringMapper {

    /**
     * Turns {@link LineString} objects into a {@link MultiLineString} and uses the first
     * {@link LineString} to get the {@link GeometryFactory} that needs to be used to create the
     * {@link MultiLineString}. If the list is empty, then the {@link GeometryFactory} cannot be obtained and an
     * {@link Optional#empty()} is returned
     *
     * @param lineStrings lineStrings
     * @return MultiLineString that contains the individual LineStrings or {@link Optional#empty()} if list is empty
     */
    public Optional<MultiLineString> map(List<LineString> lineStrings) {
        if (CollectionUtils.isEmpty(lineStrings)) {
            return Optional.empty();
        }

        GeometryFactory factory = lineStrings.get(0).getFactory();
        LineString[] lineString = lineStrings.toArray(new LineString[0]);

        return Optional.of(new MultiLineString(lineString, factory));
    }

    /**
     * Turns {@link LineString} objects into a {@link MultiLineString}, using the {@link GeometryFactory} to
     * create the {@link MultiLineString}
     *
     * @param lineStrings lineStrings
     * @return MultiLineString that contains the individual LineStrings or {@link Optional#empty()} if list is empty
     */
    public MultiLineString map(GeometryFactory geometryFactory, List<LineString> lineStrings) {
        LineString[] lineString = lineStrings.toArray(new LineString[0]);
        return geometryFactory.createMultiLineString(lineString);
    }
}
