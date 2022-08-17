package nextstep.subway.unit;

import nextstep.subway.domain.SubwayFare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayFareTest {
    private final int baseFare = 1250;
    private SubwayFare fare;
    private int totalFare;

    @BeforeEach
    void setUp() {
        fare = new SubwayFare();
        totalFare = 0;
    }

    @DisplayName("기본 운임 거리")
    @Test
    public void calculateBaseDistance() {

        totalFare = fare.calculateFare(9);
        assertThat(totalFare).isEqualTo(baseFare);
    }

    @DisplayName("50km 이하의 거리 요금")
    @Test
    public void calculateOverDistance() {
        totalFare = fare.calculateFare(46);
        assertThat(totalFare).isEqualTo(baseFare + 800);

    }

    @DisplayName("50Km 초과의 거리 요금")
    @Test
    public void calculateOverLongDistance() {
        totalFare = fare.calculateFare(56);
        assertThat(totalFare).isEqualTo(baseFare + 900);
    }

    @DisplayName("거리별 요금")
    @ParameterizedTest
    @CsvSource({
            "9,0",
            "46,800",
            "56, 900"
    })
    public void calculateFarePerDistance(int distance, int overFare) {
        totalFare = fare.calculateFare(distance);
        assertThat(totalFare).isEqualTo(baseFare + overFare);
    }
}

