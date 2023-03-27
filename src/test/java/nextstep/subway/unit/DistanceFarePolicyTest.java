package nextstep.subway.unit;

import nextstep.subway.domain.path.DistanceFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 조회 기능 단위 테스트")
class DistanceFarePolicyTest {

    @Nested
    @DisplayName("경로 구간 총 거리가 10km 이하면")
    class Context_with_total_distance_less_than_10_km {


        @Test
        @DisplayName("기본 요금이 부과된다")
        void it_returns_basic_fare() {
            int totalPrice = new DistanceFarePolicy(0).calculateFare(10);
            assertThat(totalPrice).isEqualTo(1250);
        }
    }

    @Nested
    @DisplayName("경로 구간 총 거리가 10km 초과 50km 이하라면")
    class Context_with_total_distance_between_10km_and_50km {

        @Test
        @DisplayName("추가 요금은 5km당 100원씩 부과된다.")
        void it_surcharge_is_100_won_per_5_kilometer() {
            int totalPrice = new DistanceFarePolicy(0).calculateFare(36);
            assertThat(totalPrice).isEqualTo(1250 + 600);
        }
    }

    @Nested
    @DisplayName("경로 구간 총 거리가 50km 초과라면")
    class Context_with_total_distance_more_than_50_km {

        @Test
        @DisplayName("추가 요금은 8km당 100원씩 부과된다")
        void it_surcharge_is_100_won_per_8_kilometer() {
            int totalPrice = new DistanceFarePolicy(0).calculateFare(86);
            assertThat(totalPrice).isEqualTo(1250 + 800 + 500);
        }
    }

    @Nested
    @DisplayName("경로 구간 총 거리가 10km 이하면서 추가 요금이 900원인 노선을 이용하면")
    class Context_with_total_distance_less_than_10_km_and_addtional_fare_is_900 {

        @Test
        @DisplayName("기본 요금에 추가 요금 900원이 부과된다")
        void it_returns_basic_fare_plus_additional_fare() {
            int totalPrice = new DistanceFarePolicy(900).calculateFare(10);
            assertThat(totalPrice).isEqualTo(1250 + 900);
        }
    }
}
