package nextstep.subway.unit;

import nextstep.subway.domain.fare.AgePolicy;
import nextstep.subway.domain.fare.DistancePolicy;
import nextstep.subway.domain.fare.Fare;
import nextstep.subway.domain.fare.LinePolicy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @CsvSource({"1, 1250", "10, 1250", "11, 1350", "50, 2050", "51, 1850", "59, 1950"})
    @ParameterizedTest
    void 거리별_요금_계산(int distance, int expectedFare) {
        Fare fare = Fare.standard();
        DistancePolicy policy = DistancePolicy.from(distance);
        policy.calculate(fare);
        assertThat(fare.getFare()).isEqualTo(expectedFare);
    }

    @CsvSource({"10, 500, 1750", "16, 500, 1950", "59, 500, 2450"})
    @ParameterizedTest
    void 노선별_추가_요금_계산(int distance, int extraCharge, int expectedFare) {
        Fare fare = Fare.standard();
        DistancePolicy distancePolicy = DistancePolicy.from(distance);
        distancePolicy.calculate(fare);

        LinePolicy linePolicy = LinePolicy.from(extraCharge);
        linePolicy.calculate(fare);

        assertThat(fare.getFare()).isEqualTo(expectedFare);
    }

    @CsvSource({"10, 5, 1250", "16, 8, 1230", "59, 17, 1150"})
    @ParameterizedTest
    void 연령별_요금_계산(int distance, int age, int expectedFare) {
        Fare fare = Fare.standard();
        DistancePolicy distancePolicy = DistancePolicy.from(distance);
        distancePolicy.calculate(fare);

        AgePolicy agePolicy = AgePolicy.from(age);
        agePolicy.calculate(fare);

        assertThat(fare.getFare()).isEqualTo(expectedFare);
    }
}
