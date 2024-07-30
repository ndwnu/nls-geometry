package nu.ndw.nls.geometry.rounding.mappers;

import java.math.BigDecimal;
import nu.ndw.nls.geometry.rounding.dto.RoundDoubleConfiguration;
import org.springframework.stereotype.Component;

@Component
public class RoundDoubleMapper {

    public double round(double number, RoundDoubleConfiguration roundingDecimalPlaces) {
        return BigDecimal.valueOf(number)
                .setScale(roundingDecimalPlaces.roundingDecimalPlaces(), roundingDecimalPlaces.roundingMode())
                .doubleValue();
    }

}
