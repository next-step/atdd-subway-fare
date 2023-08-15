package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineFarePolicyTest {
    @Test
    @DisplayName("노선 추가 요금 계산")
    void calculate() {
        List<Line> lines = List.of(
                new Line("신분당선", "red", 1000),
                new Line("이호선", "green"),
                new Line("이호선", "orange", 500)
        );
        assertThat(new LineFarePolicy(lines).calculate()).isEqualTo(1000);
    }
}
