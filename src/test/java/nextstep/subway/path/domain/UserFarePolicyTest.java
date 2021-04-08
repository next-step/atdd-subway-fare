package nextstep.subway.path.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserFarePolicyTest {
    private UserFarePolicy userFarePolicy;
    private static final int BASIC_FEE = 1250;

    @ParameterizedTest
    @CsvSource(value = {"7:450", "15:720", "25:1250"}, delimiter = ':')
    void calculateFareByPolicyTest_WithUserAge(int age, int fare) {
        userFarePolicy = new UserFarePolicy(age);
        assertThat(userFarePolicy.calculateFareByPolicy(BASIC_FEE)).isEqualTo(fare);
    }
}
