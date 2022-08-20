package nextstep.subway.unit;

import nextstep.subway.domain.FareCalculator;
import nextstep.subway.domain.FiftiethLessDistanceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FiftiethLessDistanceCalculatorTest {
    private FareCalculator shortOverDistanceCalculator;
    private int addFare;

    @BeforeEach
    void setUp(){
        shortOverDistanceCalculator = new FiftiethLessDistanceCalculator();
        addFare = 0;
    }

    @DisplayName("10Km 이하의 추가 요금")
    @Test
    public void calculateUnderLongDistance() {
        addFare = shortOverDistanceCalculator.calculate(0, 9);
        assertThat(addFare).isEqualTo(0);
    }

    @DisplayName("10km 에서 5km 미만 초과 했을 떄 추가 요금")
    @Test
    public void calculateOverLongDistance() {
        addFare = shortOverDistanceCalculator.calculate(0, 12);
        assertThat(addFare).isEqualTo(100);
    }
    @DisplayName("10Km 에서 5km 초과의 추가 요금")
    @Test
    public void calculateOverLongDistanceAndFiveOver() {
        addFare = shortOverDistanceCalculator.calculate(0, 16);
        assertThat(addFare).isEqualTo(200);
    }

    @DisplayName("50Km 초과해도 50Km 이하의 추가 요금만 계산되기")
    @Test
    public void calculateOnlyUnderFiftiethFare() {
        addFare = shortOverDistanceCalculator.calculate(0, 1000);
        assertThat(addFare).isEqualTo(800);
    }
}
