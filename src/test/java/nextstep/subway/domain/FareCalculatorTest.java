package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareCalculatorTest {

    @DisplayName("거리가 50km이하시 요금 5km 마다 100원 추가한다")
    @ParameterizedTest
    @CsvSource({
            "50,1000",
            "1,100",
            "6,200",
            "10,200"
    })
    void shouldAdd100WonForEach5KmWhenDistanceIsLessThanOrEqualTo50Km(int distance, int expect) {
        // when
        int fare = FareCalculator.calculateByDistance(distance);

        // then
        assertThat(fare).isEqualTo(expect);
    }

    @DisplayName("거리가 50km이하시 요금 8km 마다 100원 추가한다")
    @ParameterizedTest
    @CsvSource({
            "51,1100",
            "58,1100",
            "59,1200"
    })
    void shouldAdd100WonForEach8KmWhenDistanceIsGreaterThan50Km(int distance, int expect) {
        // when
        int fare = FareCalculator.calculateByDistance(distance);

        // then
        assertThat(fare).isEqualTo(expect);
    }

    @DisplayName("연령별 요금 할인정책의 할인율을 반환한다")
    @ParameterizedTest
    @CsvSource({
            "5,1",
            "6,0.5",
            "12,0.5",
            "13,0.2",
            "18,0.2",
            "19,0"
    })
    void testReturnAgeBasedFareDiscountPolicy(int age, float expect) {
        assertThat(FareCalculator.getAgeBasedDiscountPolicyDiscountRate(age)).isEqualTo(expect);
    }
}
