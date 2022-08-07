package nextstep.subway.domain.fare;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareRuleTest {

    @ParameterizedTest(name = "[{argumentsWithNames}] 각 거리에 맞는 FareRule에 반환된다.")
    @CsvSource(value = {"5:STANDARD", "10:STANDARD", "12:OVER_10KM", "50:OVER_10KM", "51:OVER_50KM"}, delimiter = ':')
    void fareRuleOfTest(int distance, FareRule fareRule) {
        assertThat(FareRule.of(distance)).isEqualTo(fareRule);
    }

    @ParameterizedTest(name = "[{argumentsWithNames}] 각 거리에 맞는 요금이 반환된다.")
    @CsvSource(value = {"5:1250", "10:1250", "12:1350", "16:1450", "50:2050", "51:1850"}, delimiter = ':')
    void getFare(int distance, int expectedFare) {
        FareRule fareRule = FareRule.of(distance);
        assertThat(fareRule.getFare(distance)).isEqualTo(expectedFare);
    }

}