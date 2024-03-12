package nextstep.core.subway.path.application;

import nextstep.common.annotation.ComponentTest;
import nextstep.core.auth.domain.constants.AgeGroup;
import nextstep.core.subway.fare.application.AdditionalFareCalculatePolicy;
import nextstep.core.subway.fare.application.AgeFareCalculatePolicy;
import nextstep.core.subway.fare.application.DistanceFareCalculatePolicy;
import nextstep.core.subway.fare.application.FareCalculator;
import nextstep.core.subway.line.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ComponentTest
@DisplayName("요금 계산기 관련 테스트")
public class FareCalculatorTest {

    Line 이호선;
    Line 신분당선;
    Line 삼호선;
    Line 사호선;
    Line 별내선;
    Line 일호선;

    int 일호선_추가_요금;
    int 이호선_추가_요금;
    int 신분당선_추가_요금;
    int 삼호선_추가_요금;
    int 사호선_추가_요금;
    int 별내선_추가_요금;


    int 기본_운임_거리;
    int 기본_운임_비용;

    FareCalculator fareCalculator;

    @Autowired
    DistanceFareCalculatePolicy distanceFareCalculatePolicy;

    @Autowired
    AdditionalFareCalculatePolicy additionalFareCalculatePolicy;

    @Autowired
    AgeFareCalculatePolicy ageFareCalculatePolicy;

    @BeforeEach
    void 사전_객체_생성() {
        fareCalculator = new FareCalculator(List.of(distanceFareCalculatePolicy, additionalFareCalculatePolicy, ageFareCalculatePolicy));
    }

    @BeforeEach
    void 사전_노선_설정() {
        기본_운임_거리 = 10;
        기본_운임_비용 = 1250;

        일호선_추가_요금 = 300;
        이호선_추가_요금 = 0;
        신분당선_추가_요금 = 400;
        삼호선_추가_요금 = 800;
        사호선_추가_요금 = 600;
        별내선_추가_요금 = 200;

        일호선 = new Line("일호선", "blue", 일호선_추가_요금);
        이호선 = new Line("이호선", "green", 이호선_추가_요금);
        신분당선 = new Line("신분당선", "red", 신분당선_추가_요금);
        삼호선 = new Line("삼호선", "orange", 삼호선_추가_요금);
        사호선 = new Line("사호선", "blue", 사호선_추가_요금);
        별내선 = new Line("별내선", "pink", 별내선_추가_요금);
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class 이동_거리_정책 {

        @DisplayName("이동 거리 정책만 적용된 요금이 계산된다.")
        @ParameterizedTest
        @CsvSource(value =
                {"9:1250", "10:1250", "11:1350", "25:1550", "46:2050",
                        "50:2050", "57:2150", "58:2150", "59:2250", "74:2350"}, delimiter = ':')
        void 요금_계산(int 이동_거리, int 정책이_적용된_예상_요금) {
            // given
            List<Integer> 추가_요금_목록 = List.of(이호선_추가_요금, 사호선_추가_요금, 신분당선_추가_요금, 삼호선_추가_요금);

            // when
            int 실제_계산된_요금 = fareCalculator.calculateTotalFare(new FareCalculationContext(이동_거리, 추가_요금_목록, AgeGroup.UNKNOWN));

            // then
            assertThat(실제_계산된_요금).isEqualTo(정책이_적용된_예상_요금 + 삼호선_추가_요금);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class 노선_추가_요금_정책 {

        Stream<Arguments> additionalFares() {
            return Stream.of(
                    arguments(List.of(일호선_추가_요금), 일호선_추가_요금),
                    arguments(List.of(일호선_추가_요금, 이호선_추가_요금), 일호선_추가_요금),
                    arguments(List.of(일호선_추가_요금, 이호선_추가_요금, 신분당선_추가_요금), 신분당선_추가_요금),
                    arguments(List.of(신분당선_추가_요금, 이호선_추가_요금, 일호선_추가_요금), 신분당선_추가_요금)
            );
        }

        @DisplayName("노선 추가 요금 정책만 적용된 요금이 계산된다.")
        @ParameterizedTest
        @MethodSource(value = "additionalFares")
        void 요금_계산(List<Integer> 노선_추가_요금_목록, int 정책이_적용된_예상_요금) {
            // when
            int 실제_계산된_요금 = fareCalculator.calculateTotalFare(new FareCalculationContext(기본_운임_거리, 노선_추가_요금_목록, AgeGroup.UNKNOWN));

            // then
            assertThat(실제_계산된_요금).isEqualTo(기본_운임_비용 + 정책이_적용된_예상_요금);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class 나이_정책 {

        Stream<Arguments> ages() {
            return Stream.of(
                    arguments(AgeGroup.UNKNOWN, 1250),
                    arguments(AgeGroup.CHILDREN, 450),
                    arguments(AgeGroup.TEENAGE, 720),
                    arguments(AgeGroup.ADULT, 1250)
            );
        }

        @ParameterizedTest
        @DisplayName("나이 정책만 적용된 요금이 계산된다.")
        @MethodSource(value = "ages")
        void 요금_계산(AgeGroup 연령대, int 정책이_적용된_예상_요금) {
            // given
            List<Integer> 추가_요금_목록 = List.of(이호선_추가_요금, 사호선_추가_요금, 신분당선_추가_요금, 삼호선_추가_요금);

            // when
            int 실제_계산된_요금 = fareCalculator.calculateTotalFare(new FareCalculationContext(기본_운임_거리, 추가_요금_목록, 연령대));

            // then
            assertThat(실제_계산된_요금).isEqualTo(정책이_적용된_예상_요금 + 삼호선_추가_요금);
        }
    }
}
