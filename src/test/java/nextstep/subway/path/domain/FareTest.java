package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.line.LineFarePolicy;
import nextstep.subway.path.domain.policy.line.LineFarePolicyFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @ParameterizedTest
    @CsvSource(value = {"10:1250", "20:1450", "50:2050", "51:2150", "66:2250"}, delimiter = ':')
    @DisplayName("요금 계산")
    void getFare(int distance, int expected) {
        LineFarePolicy strategy = LineFarePolicyFactory.from(distance);
        Fare fare = new Fare(strategy, distance);
        assertThat(fare.getFare()).isEqualTo(expected);
    }
}
