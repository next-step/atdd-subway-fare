package nextstep.subway.unit;

import nextstep.subway.domain.SubwayFare;
import nextstep.subway.domain.SubwayFarePolicyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayFareTest {
    private final int baseFare = 1250;
    private int totalFare;
    private SubwayFare subwayFare;

    @BeforeEach
    void setUp() {
        subwayFare = new SubwayFare(SubwayFarePolicyType.ADULT);
        totalFare = 0;
    }

    @DisplayName("기본 운임 거리")
    @Test
    public void calculateBaseDistance() {

        totalFare = subwayFare.calculateFare(0, 9);
        assertThat(totalFare).isEqualTo(baseFare);
    }

    @DisplayName("50km 이하의 거리 요금")
    @Test
    public void calculateOverDistance() {
        totalFare = subwayFare.calculateFare(0, 46);
        assertThat(totalFare).isEqualTo(baseFare + 800);

    }

    @DisplayName("50Km 초과의 거리 요금")
    @Test
    public void calculateOverLongDistance() {
        totalFare = subwayFare.calculateFare(0, 56);
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
        totalFare = subwayFare.calculateFare(0, distance);
        assertThat(totalFare).isEqualTo(baseFare + overFare);
    }

    @DisplayName("나이별 할인 요금")
    @ParameterizedTest
    @CsvSource({
            "9, CHILD, 450",
            "46, YOUTH, 1360",
            "56, ADULT, 2150"
    })
    public void discountFarePerAge(int totalDistance, String person, int discountFare) {
        SubwayFare subwayFare = new SubwayFare(SubwayFarePolicyType.valueOf(person));
        int results = subwayFare.calculateFare(0, totalDistance);
        assertThat(results).isEqualTo(discountFare);
    }
}

