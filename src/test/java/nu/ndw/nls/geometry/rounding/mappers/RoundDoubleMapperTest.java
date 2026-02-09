package nu.ndw.nls.geometry.rounding.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import nu.ndw.nls.geometry.rounding.dto.RoundDoubleConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoundDoubleMapperTest {

    @InjectMocks
    private RoundDoubleMapper roundDoubleMapper;

    @Test
    void round_ok_round7HalfUp() {
        assertThat(roundDoubleMapper.round(1.123456789, RoundDoubleConfiguration.ROUND_7_HALF_UP)).isEqualTo(1.1234568);
        assertThat(roundDoubleMapper.round(0.00000004, RoundDoubleConfiguration.ROUND_7_HALF_UP)).isEqualTo(0.0);
        assertThat(roundDoubleMapper.round(0.00000005, RoundDoubleConfiguration.ROUND_7_HALF_UP)).isEqualTo(0.0000001);
    }

    @Test
    void round_ok_round3HalfUp() {
        assertThat(roundDoubleMapper.round(0.0004, RoundDoubleConfiguration.ROUND_3_HALF_UP)).isEqualTo(0.0);
        assertThat(roundDoubleMapper.round(0.0005, RoundDoubleConfiguration.ROUND_3_HALF_UP)).isEqualTo(0.001);
    }
}