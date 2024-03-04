package nextstep.path.application.fare.extra.line;

import nextstep.common.fixture.LineFactory;
import nextstep.common.fixture.SectionFactory;
import nextstep.common.fixture.StationFactory;
import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineExtraHandlerTest {

    final Station 강남역 = StationFactory.createStation("강남역");
    final Station 선릉역 = StationFactory.createStation("선릉역");
    final Section 임시_구간 = SectionFactory.createSection(강남역, 선릉역, 10, 10);
    final Line 무료_노선 = LineFactory.createLine("2호선", "green", 0L, 임시_구간);
    final Line 오백원_노선 = LineFactory.createLine("3호선", "orange", 500L, 임시_구간);
    final Line 천원_노선 = LineFactory.createLine("신분당선", "blue", 1000L, 임시_구간);

    @Test
    @DisplayName("가장 높은 금액의 노선요금이 추가된 요금이 반환된다.")
    void lineFareHandlerTest() {
        final LineExtraHandler lineFareHandler = new LineExtraHandler();

        final long calculated = lineFareHandler.calculate(List.of(무료_노선, 오백원_노선, 천원_노선));

        assertThat(calculated).isEqualTo(천원_노선.getExtraFare());
    }

}
