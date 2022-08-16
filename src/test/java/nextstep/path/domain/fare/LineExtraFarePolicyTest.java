package nextstep.path.domain.fare;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineExtraFarePolicyTest {

    @DisplayName("노선 추가요금 기반 요금 계산")
    @Test
    void apply() {
        // given
        Line lineOne = new Line("1호선", "green", 100);
        Line lineTwo = new Line("2호선", "green", 200);
        Line lineThree = new Line("3호선", "green", 300);

        Section pathSection = new Section(1L, 2L, 4, 4);
        lineOne.addSection(pathSection);
        lineTwo.addSection(pathSection);

        List<Line> lines = List.of(lineOne, lineTwo, lineThree);
        List<Section> pathSections = List.of(pathSection);

        // when
        LineExtraFarePolicy policy = new LineExtraFarePolicy(lines, pathSections, new NullFarePolicy());
        int result = policy.apply(0);

        // then
        assertThat(result).isEqualTo(200);
    }
}