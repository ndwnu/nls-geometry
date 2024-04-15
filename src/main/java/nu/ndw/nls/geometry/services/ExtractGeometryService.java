package nu.ndw.nls.geometry.services;

import static org.locationtech.jts.precision.GeometryPrecisionReducer.reduceKeepCollapsed;

import lombok.RequiredArgsConstructor;
import nu.ndw.nls.geometry.crs.CrsTransformer;
import nu.ndw.nls.geometry.mappers.GeometrySimplifierMapper;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.PointOutsideEnvelopeException;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.linearref.LengthIndexedLine;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExtractGeometryService {
    private static final double SIMPLIFY_DISTANCE_TOLERANCE_METRES = 0.5;
    private static final double SCALE_BASE = 10;
    private static final double SCALE_POWER_ROUNDING_DECIMALS = 7;
    private static final CoordinateReferenceSystem RD_NEW_CRS;
    private static final String EPSG_28992_RD_NEW = "EPSG:28992";

    static {
        try {
            RD_NEW_CRS = CRS.decode(EPSG_28992_RD_NEW);
        } catch (FactoryException e) {
            throw new IllegalStateException("Could not initialize ExtractGeometryService", e);
        }
    }

    private final CrsTransformer crsTransformer;
    private final GeometrySimplifierMapper geometrySimplifierMapper;

    /** Extract coordinates from a geometry defined in WGS84 using RD_NEW */
    public Geometry extractByFractions(double startFraction, double endFraction, Geometry geometry) {
        checkCoordinatesRange(geometry, DefaultGeographicCRS.WGS84);

        Geometry geometryRdNew = crsTransformer.transformFromWgs84ToRdNew(geometry);

        double lengthMetres = geometryRdNew.getLength();

        Geometry extractedRdNew = new LengthIndexedLine(geometryRdNew)
                .extractLine(startFraction * lengthMetres, endFraction * lengthMetres);

        return simplifyAndTransformRdNewToWgs84(extractedRdNew);
    }

    /** Extract coordinates from a geometry defined in WGS84 using RD_NEW */
    public Geometry extractByMetres(double startMetres, double endMetres, Geometry geometry) {
        checkCoordinatesRange(geometry, DefaultGeographicCRS.WGS84);

        Geometry geometryRdNew = crsTransformer.transformFromWgs84ToRdNew(geometry);

        Geometry extractedRdNew = new LengthIndexedLine(geometryRdNew)
                .extractLine(startMetres, endMetres);

        return simplifyAndTransformRdNewToWgs84(extractedRdNew);
    }

    public Geometry simplifyAndTransformRdNewToWgs84(Geometry rdNewGeometry) {
        checkCoordinatesRange(rdNewGeometry, RD_NEW_CRS);
        // NOTE: it is important to simplify using RD_NEW coordinates because the CRS uses metres
        Geometry simplified = geometrySimplifierMapper.map(SIMPLIFY_DISTANCE_TOLERANCE_METRES, rdNewGeometry);
        Geometry transformed = crsTransformer.transformFromRdNewToWgs84(simplified);
        PrecisionModel precisionModel = new PrecisionModel(Math.pow(SCALE_BASE, SCALE_POWER_ROUNDING_DECIMALS));
        return reduceKeepCollapsed(transformed, precisionModel);
    }

    public Geometry simplifyAndTransformRdNewToWgs84Rounded(Geometry rdNewGeometry, Integer decimals) {
        checkCoordinatesRange(rdNewGeometry, RD_NEW_CRS);
        // NOTE: it is important to simplify using RD_NEW coordinates because the CRS uses metres
        Geometry simplified = geometrySimplifierMapper.map(SIMPLIFY_DISTANCE_TOLERANCE_METRES, rdNewGeometry);
        Geometry transformed = crsTransformer.transformFromRdNewToWgs84(simplified);
        PrecisionModel precisionModel = new PrecisionModel(Math.pow(SCALE_BASE, decimals));
        return reduceKeepCollapsed(transformed, precisionModel);
    }

    private void checkCoordinatesRange(Geometry geometry, CoordinateReferenceSystem crs) {
        try {
            JTS.checkCoordinatesRange(geometry, crs);
        } catch (PointOutsideEnvelopeException e) {
            throw new IllegalStateException("Geometry coordinates are not valid for %s".formatted(crs.getName()), e);
        }
    }
}
