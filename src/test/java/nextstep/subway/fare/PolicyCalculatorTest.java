package nextstep.subway.fare;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PolicyCalculatorTest {

    @ParameterizedTest
    @CsvSource("1000, 400")
    void NonLoginCalculate(int fare, int extraFare) {
        PolicyCalculator policyCalculator = new PolicyCalculator(new NoneLoginPolicy(new LineExtraFarePolicy()));

        assertThat(policyCalculator.calculate(fare, extraFare)).isEqualTo(discountFare(fare + extraFare, 25));
    }

    @ParameterizedTest
    @CsvSource("1000, 400")
    void LoginChildCalculate(int fare, int extraFare) {
        PolicyCalculator policyCalculator = new PolicyCalculator(new ChildPolicy(new LineExtraFarePolicy()));

        assertThat(policyCalculator.calculate(fare, extraFare)).isEqualTo(discountFare(fare + extraFare, 8));
    }

    @ParameterizedTest
    @CsvSource("1000, 400")
    void LoginYouthCalculate(int fare, int extraFare) {
        PolicyCalculator policyCalculator = new PolicyCalculator(new YouthPolicy(new LineExtraFarePolicy()));

        assertThat(policyCalculator.calculate(fare, extraFare)).isEqualTo(discountFare(fare + extraFare, 18));
    }

    private int discountFare(int fare, int age) {
        if (age == 0 || age > 20) {
            return fare;
        }
        if (age > 13) {
            return (fare - 350) * 80 / 100;
        }
        if (age > 6) {
            return (fare - 350) * 50 / 100;
        }
        return 0;
    }
}
