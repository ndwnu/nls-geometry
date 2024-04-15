package nu.ndw.nls.geometry.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = {"nu.ndw.nls.geometry.geojson.mappers"})
@Configuration
public class GeoJsonMapperConfiguration {

}
