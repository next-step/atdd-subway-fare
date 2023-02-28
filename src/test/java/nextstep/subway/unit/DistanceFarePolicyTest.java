package nextstep.subway.unit;

import nextstep.subway.domain.DistanceFarePolicy;
import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Sections;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("거리 요금 계산 테스트")
class DistanceFarePolicyTest {

    private static Sections 최단_구간_목록 = new Sections(Lists.newArrayList());

    @DisplayName("각 거리의 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "10,1250", "14,1250", "15,1350", "20,1450", "25,1550", "30,1650", "35,1750",
            "40,1850", "45,1950", "49,1950", "50,2050", "58,2150", "63,2150", "66,2250"})
    void calculateFiveIntervalExtraFare (final int weight, final String totalFare) {
        final Path path = new Path(최단_구간_목록, weight, 0);
        final Fare expected = DistanceFarePolicy.from(path).plus(Fare.from(1250));
        assertThat(expected).isEqualTo(new Fare(new BigDecimal(totalFare)));
    }
}
