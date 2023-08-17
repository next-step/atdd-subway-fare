package nextstep.subway.unit;


import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class LineTest {

    @DisplayName("노선 생성 및 구간 거리 및 시간 확인")
    @Test
    void createSectionTest() {
        // given : 선행조건 기술
        Line line = createLine("2호선", "green");
        Station upStation = createStation("교대역");
        Station downStation = createStation("강남역");
        int distance = 10;
        int duration = 10;
        line.addSection(upStation, downStation, distance, duration);

        // when : 기능 수행
        List<Section> sections = line.getSections();

        // then : 결과 확인
        assertThat(sections).hasSize(1)
                        .extracting("line", "upStation", "downStation", "distance", "duration")
                        .containsExactly(tuple(line, upStation, downStation, distance, duration));
    }

    private Station createStation(String name) {
        return new Station(name);
    }

    private Line createLine(String name, String color) {
        return new Line(name, color);
    }
}