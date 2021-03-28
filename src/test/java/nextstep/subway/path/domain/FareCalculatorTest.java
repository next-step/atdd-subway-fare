package nextstep.subway.path.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 거리 가격 계산")
public class FareCalculatorTest {
    private FareCalculator calculator;

    @BeforeEach
    public void setup(){
        this.calculator = new FareCalculator();

    }

    @DisplayName("10km 미만인 경우")
    @Test
    public void calculateFareUnder10km() {
        // given
        int distance = 5;

        // when
        int fare = calculator.calculate(distance);

        // then
        assertThat(fare).isEqualTo(FareCalculator.BASE_FARE);
    }

    @DisplayName("10km~50km 사이의 경우")
    @Test
    public void calculateFareBetween10kmAnd50km() {
        // given
        int distance = 20;
        int overFare = 300;

        // when
        int fare = calculator.calculate(distance);

        // then
        assertThat(fare).isEqualTo(FareCalculator.BASE_FARE+overFare);
    }


    @DisplayName("50km 초과인 경우")
    @Test
    public  void calculateFareOver50km() {
        // given
        int distance = 51;
        int overFare = 1000;

        // when
        int fare = calculator.calculate(distance);

        // then
        assertThat(fare).isEqualTo(FareCalculator.BASE_FARE+overFare);
    }
}
