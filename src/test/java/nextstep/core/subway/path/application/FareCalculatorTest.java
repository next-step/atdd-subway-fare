package nextstep.core.subway.path.application;

import nextstep.common.annotation.ComponentTest;
import nextstep.core.subway.line.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ComponentTest
@DisplayName("요금 계산기 관련 테스트")
public class FareCalculatorTest {

    Line 이호선;
    Line 신분당선;
    Line 삼호선;
    Line 사호선;
    Line 별내선;
    Line 일호선;

    FareCalculator fareCalculator;

    @Autowired
    DistanceFareCalculator distanceFareCalculator;

    @Autowired
    AdditionalFareCalculator additionalFareCalculator;

    @Autowired
    AgeFareCalculator ageFareCalculator;

    @BeforeEach
    void setUp() {
        fareCalculator = new FareCalculator(distanceFareCalculator, additionalFareCalculator, ageFareCalculator);
    }

    @BeforeEach
    void 사전_노선_설정() {
        이호선 = new Line("이호선", "green", 0);
        신분당선 = new Line("신분당선", "red", 400);
        삼호선 = new Line("삼호선", "orange", 800);
        사호선 = new Line("사호선", "blue", 600);
        별내선 = new Line("별내선", "pink", 200);
        일호선 = new Line("일호선", "blue", 300);

    }

    @ParameterizedTest
    @DisplayName("거리와 노선 추가 요금(가장 높은 금액만 적용)을 기반해서 요금이 계산된다.")
    @CsvSource(value =
            {"9:1250:800", "10:1250:800", "11:1350:800", "25:1550:800", "46:2050:800",
            "50:2050:800", "57:2150:800", "58:2150:800", "59:2250:800", "74:2350:800"}, delimiter = ':')
    void 요금_계산(int 이동_거리, int 예상하는_운임_비용, int 예상하는_가장_높은_노선_추가_요금) {
        // given
        List<Integer> 추가_요금_목록 = List.of(이호선.getAdditionalFare(), 사호선.getAdditionalFare(), 신분당선.getAdditionalFare(), 삼호선.getAdditionalFare());

        // when
        int 실제_계산된_요금 = fareCalculator.calculateTotalFare(이동_거리, 추가_요금_목록);

        // then
        assertThat(실제_계산된_요금).isEqualTo(예상하는_운임_비용 + 예상하는_가장_높은_노선_추가_요금);
    }

    @ParameterizedTest
    @DisplayName("거리, 노선 추가 요금(가장 높은 금액만 적용), 나이를 기반해서 요금이 계산된다.")
    @CsvSource(value =
            {"9:450:800:6", "9:450:800:12", "9:720:800:13", "9:720:800:18", "9:1250:800:19"}, delimiter = ':')
    void 요금_계산(int 이동_거리, int 예상하는_운임_비용, int 예상하는_가장_높은_노선_추가_요금, int 나이) {
        // given
        List<Integer> 추가_요금_목록 = List.of(이호선.getAdditionalFare(), 사호선.getAdditionalFare(), 신분당선.getAdditionalFare(), 삼호선.getAdditionalFare());

        // when
        int 실제_계산된_요금 = fareCalculator.calculateTotalFare(이동_거리, 추가_요금_목록, 나이);

        // then
        assertThat(실제_계산된_요금).isEqualTo(예상하는_운임_비용 + 예상하는_가장_높은_노선_추가_요금);
    }
}
