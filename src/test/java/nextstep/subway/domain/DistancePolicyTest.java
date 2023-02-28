package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistancePolicyTest {

    /**
     * 기본운임(10㎞ 이내) : 기본운임 1,250원
     * 10km초과∼50km까지(5km마다 100원)
     * 50km초과 시 (8km마다 100원)
     */
    @ParameterizedTest
    @CsvSource({"10, 1250",
            "12, 1350",
            "16, 1450",
            "50, 2050",
            "57, 2150"
    })
    @DisplayName("거리에 따른 계산 요청")
    void 거리에_따른_계산_요청(int distance, int fare) {
        // When
        DistancePolicy distancePolicy = new DistancePolicy(distance);

        // Then
        assertThat(distancePolicy.calcFare()).isEqualTo(fare);
    }

}