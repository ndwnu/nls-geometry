package nu.ndw.nls.geometry.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = {"nu.ndw.nls.geometry.crs", "nu.ndw.nls.geometry.factories",
        "nu.ndw.nls.geometry.mappers"})
@Configuration
public class GeometryMapperConfiguration {

}
