package nextstep.subway.path.domain;

import nextstep.subway.path.domain.specification.distance.FirstDistanceFare;
import nextstep.subway.path.domain.valueobject.Distance;
import nextstep.subway.path.domain.valueobject.Fare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 거리 가격 계산")
public class DiscountFareTest {
    private DistanceFare distanceFareCalculator;

    @BeforeEach
    public void setup(){
        this.distanceFareCalculator = new FirstDistanceFare();

    }

    @ParameterizedTest
    @DisplayName("거리에 따른 요금 계산")
    @MethodSource("provideDistanceAndOverFare")
    public void calculateFareByDistance(Distance distance, Fare overFare) {
        // when
        Fare fare = distanceFareCalculator.calculate(distance);

        // then
        assertThat(fare).isEqualTo(overFare);
    }

    private static Stream< Arguments > provideDistanceAndOverFare() {
        return Stream.of(
                Arguments.of(Distance.of(5), Fare.of(1250)),
                Arguments.of(Distance.of(20), Fare.of(1250+300)),
                Arguments.of(Distance.of(51), Fare.of(1250+1000))
        );
    }
}
