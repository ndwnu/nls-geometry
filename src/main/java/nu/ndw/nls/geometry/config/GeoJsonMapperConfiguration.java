package nu.ndw.nls.geometry.config;

import nu.ndw.nls.geometry.rounding.RoundingConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan(basePackages = {"nu.ndw.nls.geometry.geojson.mappers"})
@Configuration
@Import({RoundingConfiguration.class})
public class GeoJsonMapperConfiguration {

}
