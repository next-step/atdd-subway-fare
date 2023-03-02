package nextstep.subway.domain;

import nextstep.subway.domain.exception.MinimumDistanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistancePolicyTest {

    /**
     * When && Then  최소한 거리 보다 작으면 거리 정책 생성 요청 시 생성이 안된다
     */
    @Test
    @DisplayName("최소한 거리 보다 작으면 거리 정책 생성 요청 시 생성이 안된다")
    void 최소한_거리_보다_작으면_거리_정책_생성_요청_시_생성이_안된다() {
        // When && Then
        assertThatThrownBy(() -> new DistanceFarePolicy(0))
                .isInstanceOf(MinimumDistanceException.class)
                .hasMessage("거리는 최소한 1 이상이여야 합니다");
    }

    /**
     * 기본운임(10㎞ 이내) : 기본운임 1,250원
     * 10km초과∼50km까지(5km마다 100원)
     * 50km초과 시 (8km마다 100원)
     */
    @ParameterizedTest
    @CsvSource({
            "10, 1250",
            "12, 1350",
            "16, 1450",
            "50, 2050",
            "57, 2150"
    })
    @DisplayName("거리에 따른 계산 요청")
    void 거리에_따른_계산_요청(int distance, int fare) {
        // When
        DistanceFarePolicy distancePolicy = new DistanceFarePolicy(distance);

        // Then
        int defaultFare = 1250;
        assertThat(distancePolicy.calcFare(defaultFare)).isEqualTo(fare);
    }

}