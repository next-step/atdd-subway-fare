package nextstep.subway.unit.fare;

import nextstep.subway.domain.fare.FareBasis;
import nextstep.subway.domain.fare.FarePolicies;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 고속터미널역  --- *7호선* ---  강남구청역
 *    |
 *  *3호선*
 *    |
 *  교대역    --- *2호선*  ---  강남역
 *                             |
 *                         *신분당선*
 *                             |
 *                           양재역
 *  => total Distance = 24km
 */
class FarePoliciesTest {
    @DisplayName("거리 & 연령 & 노선 정책이 모두 포함된 상태에서 최종 요금 조회")
    @ParameterizedTest
    @CsvSource(value = {
            "3, 0",
            "9, 750",
            "15, 1200",
            "29, 1850",
            "70, 0"
    })
    void getTotalFareWithAllPolicy(int age, int expectedFare) {
        // given
        int distance = 24;
        int maxExtraLineFare = 300;
        FarePolicies farePolicies = new FarePolicies();
        FareBasis fareBasis = new FareBasis(distance, age, maxExtraLineFare);

        // when
        int totalFare = farePolicies.calculateFare(fareBasis);

        // then
        assertThat(totalFare).isEqualTo(expectedFare);
    }
}
