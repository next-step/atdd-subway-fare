package nextstep.subway.unit;

import static nextstep.member.domain.MemberAge.UNKNOWN;
import static nextstep.subway.unit.support.FareSupporter.계산된_요금과_일치한다;
import static nextstep.subway.unit.support.FareSupporter.운임_요금_계산;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.member.domain.MemberAge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("요금 조회 기능 단위 테스트")
class DistanceFarePolicyTest {

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 비로그인_상태로_경로_조회_시 {

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 추가_요금_노선이_없는_경우 {

            @Nested
            @DisplayName("경로 구간 총 거리가 10km 이하면")
            class Context_with_total_distance_less_than_10_km {

                @Test
                @DisplayName("기본 요금이 부과된다")
                void it_returns_basic_fare() {
                    int totalPrice = 운임_요금_계산(0, 10, UNKNOWN);

                    계산된_요금과_일치한다(totalPrice, 1250);
                }
            }

            @Nested
            @DisplayName("경로 구간 총 거리가 10km 초과 50km 이하라면")
            class Context_with_total_distance_between_10km_and_50km {

                @Test
                @DisplayName("추가 요금은 5km당 100원씩 부과된다.")
                void it_surcharge_is_100_won_per_5_kilometer() {
                    int totalPrice = 운임_요금_계산(0, 36, UNKNOWN);

                    계산된_요금과_일치한다(totalPrice, 1250 + 600);
                }
            }

            @Nested
            @DisplayName("경로 구간 총 거리가 50km 초과라면")
            class Context_with_total_distance_more_than_50_km {

                @Test
                @DisplayName("추가 요금은 8km당 100원씩 부과된다")
                void it_surcharge_is_100_won_per_8_kilometer() {
                    int totalPrice = 운임_요금_계산(0, 86, UNKNOWN);

                    계산된_요금과_일치한다(totalPrice, 1250 + 800 + 500);
                }
            }

        }

        @Nested
        @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
        class 추가_요금_노선이_있는_경우 {

            @Nested
            @DisplayName("경로 구간 총 거리가 10km 이하면서 추가 요금이 900원인 노선을 이용하면")
            class Context_with_total_distance_less_than_10_km_and_addtional_fare_is_900 {

                @Test
                @DisplayName("기본 요금에 추가 요금 900원이 부과된다")
                void it_returns_basic_fare_plus_additional_fare() {
                    int totalPrice = 운임_요금_계산(900, 10, UNKNOWN);

                    계산된_요금과_일치한다(totalPrice, 1250 + 900);
                }
            }
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class 로그인_상태로_경로_조회_시 {

        @Nested
        @DisplayName("나이가 19세 이상일 때 추가 요금 노선 없이 기본 거리 경로를 이용하면")
        class Context_with_age_more_than_19 {

            @Test
            @DisplayName("성인 요금 정책이 적용된다")
            void it_returns_adult_fare_policy() {
                int totalPrice = 운임_요금_계산(0, 10, MemberAge.of(19));

                계산된_요금과_일치한다(totalPrice, 1250);
            }
        }

        @Nested
        @DisplayName("나이가 13세 이상 19세 미만일 때 추가 요금 노선 없이 기본 거리 경로를 이용하면")
        class Context_with_age_between_13_and_19 {

            @Test
            @DisplayName("청소년 요금 정책이 적용된다")
            void it_returns_youth_fare_policy() {
                int totalPrice = 운임_요금_계산(0, 10, MemberAge.of(13));

                계산된_요금과_일치한다(totalPrice, (int) ((1250 - 350) * (1 - 0.2)));
            }
        }

        @Nested
        @DisplayName("나이가 6세 이상 13세 미만일 때 추가 요금 노선 없이 기본 거리 경로를 이용하면")
        class Context_with_age_between_6_and_13 {

            @Test
            @DisplayName("어린이 요금 정책이 적용된다")
            void it_returns_child_fare_policy() {
                int totalPrice = 운임_요금_계산(0, 10, MemberAge.of(12));

                계산된_요금과_일치한다(totalPrice, (int) ((1250 - 350) * (1 - 0.5)));
            }
        }

        @Nested
        @DisplayName("나이가 6세 미만일 때 추가 요금 노선 없이 기본 거리 경로를 이용하면")
        class Context_with_age_less_than_6 {

            @Test
            @DisplayName("무료로 적용된다")
            void it_returns_child_fare_policy() {
                int totalPrice = 운임_요금_계산(0, 10, MemberAge.of(5));

                계산된_요금과_일치한다(totalPrice, 0);
            }
        }
    }
}
