package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.distance.DefaultDistancePolicy;
import nextstep.subway.path.domain.policy.distance.OverFiftyDistancePolicy;
import nextstep.subway.path.domain.policy.distance.OverTenDistancePolicy;
import nextstep.subway.path.domain.policy.line.AdditionalLinePolicy;
import nextstep.subway.path.domain.policy.line.DefaultLinePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @DisplayName("50킬로 넘고, 라인 추가 요금 있을 때")
    @ParameterizedTest
    @CsvSource(value = {"53:300:2450", "58:500:2650", "67:100:2450"},  delimiter = ':')
    void calculate(int distance, int lineFare, int expectedFare) {
        // given
        OverFiftyDistancePolicy distancePolicy = new OverFiftyDistancePolicy(distance);
        AdditionalLinePolicy linePolicy = new AdditionalLinePolicy(lineFare);

        // when
        Fare fare = Fare.calculate(distancePolicy, linePolicy);

        // then
        assertThat(fare.getFareValue()).isEqualTo(expectedFare);
    }

    @DisplayName("10킬로 넘고, 라인 추가 요금 있을 때")
    @ParameterizedTest
    @CsvSource(value = {"12:300:1650", "16:500:1950", "25:100:1650"},  delimiter = ':')
    void calculate2(int distance, int lineFare, int expectedFare) {
        // given
        OverTenDistancePolicy distancePolicy = new OverTenDistancePolicy(distance);
        AdditionalLinePolicy linePolicy = new AdditionalLinePolicy(lineFare);

        // when
        Fare fare = Fare.calculate(distancePolicy, linePolicy);

        // then
        assertThat(fare.getFareValue()).isEqualTo(expectedFare);
    }

    @DisplayName("기본거리에 , 라인 추가 요금 있을 때")
    @ParameterizedTest
    @CsvSource(value = {"300:1550", "500:1750", "100:1350"},  delimiter = ':')
    void calculate3(int lineFare, int expectedFare) {
        // given
        DefaultDistancePolicy distancePolicy = new DefaultDistancePolicy();
        AdditionalLinePolicy linePolicy = new AdditionalLinePolicy(lineFare);

        // when
        Fare fare = Fare.calculate(distancePolicy, linePolicy);

        // then
        assertThat(fare.getFareValue()).isEqualTo(expectedFare);
    }

    @DisplayName("기본 거리에 , 라인 추가 요금 없을 때")
    @Test
    void calculate4() {
        // given
        DefaultDistancePolicy distancePolicy = new DefaultDistancePolicy();
        DefaultLinePolicy linePolicy = new DefaultLinePolicy();

        // when
        Fare fare = Fare.calculate(distancePolicy, linePolicy);

        // then
        assertThat(fare.getFareValue()).isEqualTo(1250);
    }
}
