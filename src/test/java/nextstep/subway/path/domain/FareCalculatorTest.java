package nextstep.subway.path.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 거리 가격 계산")
public class FareCalculatorTest {
    private DistanceFare calculator;

    @BeforeEach
    public void setup(){
        this.calculator = new DistanceFare();

    }

    @ParameterizedTest
    @DisplayName("거리에 따른 요금 계산")
    @MethodSource("provideDistanceAndOverFare")
    public void calculateFareByDistance(int distance, int overFare) {
        // when
        int fare = calculator.calculate(distance);

        // then
        assertThat(fare).isEqualTo(DistanceFare.BASE_FARE+overFare);
    }

    private static Stream< Arguments > provideDistanceAndOverFare() {
        return Stream.of(
                Arguments.of(5, 0),
                Arguments.of(20, 300),
                Arguments.of(51, 1000)
        );
    }
}
