package nextstep.path.fare;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorHandlerTest {

    private FareCalculatorHandler fareCalculatorHandler;

    @BeforeEach
    public void setUp() {
        FareCalculatorHandler baseFareCalculator = new BaseFareCalculator();
        baseFareCalculator.setNextHandler(new FirstExtraFareCalculator())
                .setNextHandler(new SecondExtraCalculator());
        fareCalculatorHandler = baseFareCalculator;
    }

    @DisplayName("거리에 따라 요금을 계산한다")
    @ParameterizedTest
    @CsvSource({"10, 1250", "11, 1350", "15, 1350", "16, 1450", "50, 2050", "51, 2150", "58, 2150", "59, 2250"})
    void calculate(int distance, int expected) {
        Fare fare = fareCalculatorHandler.handleFareCalculate(distance, Fare.DEFAULT_FARE);
        assertThat(fare).isEqualTo(Fare.of(expected));
    }

}