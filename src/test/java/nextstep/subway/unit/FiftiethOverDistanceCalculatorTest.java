package nextstep.subway.unit;

import nextstep.subway.domain.FareCalculator;
import nextstep.subway.domain.FiftiethOverDistanceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FiftiethOverDistanceCalculatorTest {
    private FareCalculator longDistanceCalculator;
    private int addFare;

    @BeforeEach
    void setUp(){
        longDistanceCalculator = new FiftiethOverDistanceCalculator();
        addFare = 0;
    }

    @DisplayName("50Km 이하의 추가 요금")
    @Test
    public void calculateUnderLongDistance() {
        addFare = longDistanceCalculator.calculate(0, 50);
        assertThat(addFare).isEqualTo(0);
    }

    @DisplayName("50km 에서 8km 미만 초과 했을 떄 추가 요금")
    @Test
    public void calculateOverLongDistance() {
        addFare = longDistanceCalculator.calculate(0, 56);
        assertThat(addFare).isEqualTo(100);
    }
    @DisplayName("50Km 에서 8km 초과의 추가 요금")
    @Test
    public void calculateOverLongDistanceAndEigthOver() {
        addFare = longDistanceCalculator.calculate(0, 59);
        assertThat(addFare).isEqualTo(200);
    }
}
