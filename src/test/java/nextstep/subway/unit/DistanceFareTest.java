package nextstep.subway.unit;

import nextstep.subway.domain.DistanceFare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class DistanceFareTest {

    @DisplayName("거리별 요금 테스트")
    @ParameterizedTest
    @CsvSource(value = {"1,1250","10,1250","11,1350","50,2050","51,2150","58,2150","59,2250"})
    void fare(int distance, BigDecimal fare) {
        BigDecimal result = DistanceFare.extractFare(distance);

        assertThat(result).isEqualTo(fare);
    }
}
