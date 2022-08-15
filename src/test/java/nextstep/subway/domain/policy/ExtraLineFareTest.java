package nextstep.subway.domain.policy;

import nextstep.subway.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExtraLineFareTest {

    private FarePolicy extraLineFare;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private List<Line> lines;

    @BeforeEach

    void setUp() {
        extraLineFare = new ExtraLineFare();

        신분당선 = new Line("신분당선", "red", 900);
        이호선 = new Line("이호선", "green", 300);
        삼호선 = new Line("이호선", "green", 0);

        lines = List.of(신분당선, 이호선, 삼호선);
    }

    @DisplayName("노선별 추가 요금 정책은 항상 적용한다.")
    @ParameterizedTest(name = "#{index} - 거리={0}km")
    @ValueSource(ints = {1, 10, 11, 50, 51})
    void support(int distance) {
        PathByFare pathByFare = PathByFare.builder().distance(distance).build();

        assertThat(extraLineFare.supports(pathByFare)).isTrue();
    }

    @Test
    @DisplayName("여러 노선을 환승할 때 가장 높은 금액의 추가요금만 적용한다.")
    void fare() {
        PathByFare pathByFare = PathByFare.builder()
                .lines(lines)
                .build();

        assertThat(extraLineFare.fare(pathByFare)).isEqualTo(900);
    }
}