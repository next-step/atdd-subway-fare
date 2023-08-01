package nextstep.subway.acceptance;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {

    @DisplayName("지하철 노선에 구간을 추가한다.")
    @Test
    void addSection() {
        // given
        Line line = new Line();
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");

        // when
        line.addSection(강남역, 양재역, 10, 5);

        // then
        assertThat(line.getSections().size()).isEqualTo(1);
        assertThat(line.getStations().stream().map(Station::getName))
                .containsExactly(강남역.getName(), 양재역.getName());
    }
}
