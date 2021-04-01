package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.age.AgeFarePolicy;
import nextstep.subway.path.domain.policy.age.AgeFarePolicyFactory;
import nextstep.subway.path.domain.policy.line.LineFarePolicy;
import nextstep.subway.path.domain.policy.line.LineFarePolicyFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @ParameterizedTest
    @CsvSource(value = {"20:10:1250", "20:20:1450", "20:50:2050", "20:51:2150", "20:66:2250"}, delimiter = ':')
    @DisplayName("요금 계산")
    void getFare(int age, int distance, int expected) {
        LineFarePolicy lineFarePolicy = LineFarePolicyFactory.from(distance);
        AgeFarePolicy ageFarePolicy = AgeFarePolicyFactory.from(age);
        FarePolicyTemplate fare = new Fare(lineFarePolicy, ageFarePolicy, distance);

        fare.applyPolicy(0);
        assertThat(fare.getFare()).isEqualTo(expected);
    }
}
