package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Path 단위 테스트")
class PathTest {

    private Line line;
    private Station 교대역, 강남역, 양재역;
    private Section 첫번쨰구간, 두번쨰구간;

    @BeforeEach
    void setup() {
        첫번쨰구간 = firstSection();
        두번쨰구간 = secondSection();
    }

    @DisplayName("Path 생성 및 거리 및 시간 확인")
    @Test
    void createPathTest() {
        // given : 선행조건 기술
        Sections sections = new Sections(List.of(첫번쨰구간, 두번쨰구간));

        // when : 기능 수행
        Path path = new Path(sections);

        // then : 결과 확인
        List<Integer> distances = path.getSections().getSections().stream()
                .map(Section::getDistance)
                .collect(Collectors.toList());
        assertThat(distances).hasSize(2).containsExactly(10, 10);
        assertThat(path.extractDistance()).isEqualTo(20);
        List<Integer> durations = path.getSections().getSections().stream()
                .map(Section::getDuration)
                .collect(Collectors.toList());
        assertThat(durations).hasSize(2).containsExactly(10, 11);
        assertThat(path.extractDuration()).isEqualTo(21);
    }

    private Section firstSection() {
        line = createLine("2호선", "green");
        교대역 = createStation("교대역");
        강남역 = createStation("강남역");
        return createSection(line, 교대역, 강남역,  10, 10);
    }

    private Section secondSection() {
        line = createLine("신분당선", "green");
        양재역 = createStation("양재역");
        return createSection(line, 강남역, 양재역,  10, 11);
    }

    private Section createSection(Line line, Station upStation, Station downStation, int distance, int duration) {
        return new Section(line, upStation, downStation, distance, duration);
    }

    private Station createStation(String name) {
        return new Station(name);
    }

    private Line createLine(String name, String color) {
        return new Line(name, color);
    }

}