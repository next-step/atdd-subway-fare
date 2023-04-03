package nextstep.subway.unit;

import static nextstep.subway.unit.support.FareSupporter.계산된_요금과_일치한다;
import static nextstep.subway.unit.support.FareSupporter.운임_요금_계산;

import nextstep.member.domain.MemberAgePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("요금 할인 기능 단위 테스트")
class MemberAgePolicyTest {

    private int 연령_할인_전_요금;

    @BeforeEach
    void setUp() {
        연령_할인_전_요금 = 운임_요금_계산(0, 10);
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
                MemberAgePolicy 회원_나이 = MemberAgePolicy.of(19);

                계산된_요금과_일치한다(회원_나이.discountFare(연령_할인_전_요금), 1250);
            }
        }

        @Nested
        @DisplayName("나이가 13세 이상 19세 미만일 때 추가 요금 노선 없이 기본 거리 경로를 이용하면")
        class Context_with_age_between_13_and_19 {

            @Test
            @DisplayName("청소년 요금 정책이 적용된다")
            void it_returns_youth_fare_policy() {
                MemberAgePolicy 회원_나이 = MemberAgePolicy.of(13);

                계산된_요금과_일치한다(회원_나이.discountFare(연령_할인_전_요금), (int) ((1250 - 350) * (1 - 0.2)));
            }
        }

        @Nested
        @DisplayName("나이가 6세 이상 13세 미만일 때 추가 요금 노선 없이 기본 거리 경로를 이용하면")
        class Context_with_age_between_6_and_13 {

            @Test
            @DisplayName("어린이 요금 정책이 적용된다")
            void it_returns_child_fare_policy() {
                MemberAgePolicy 회원_나이 = MemberAgePolicy.of(12);

                계산된_요금과_일치한다(회원_나이.discountFare(연령_할인_전_요금), (int) ((1250 - 350) * (1 - 0.5)));
            }
        }

        @Nested
        @DisplayName("나이가 6세 미만일 때 추가 요금 노선 없이 기본 거리 경로를 이용하면")
        class Context_with_age_less_than_6 {

            @Test
            @DisplayName("무료로 적용된다")
            void it_returns_child_fare_policy() {
                MemberAgePolicy 회원_나이 = MemberAgePolicy.of(5);

                계산된_요금과_일치한다(회원_나이.discountFare(연령_할인_전_요금), 0);
            }
        }
    }
}
