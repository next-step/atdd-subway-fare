package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 도메인 테스트")
class SectionTest {


    @DisplayName("구간 생성 및 거리 및 시간 확인")
    @Test
    void createSectionTest() {
        // given : 선행조건 기술
        Line line = createLine("2호선", "green");
        Station upStation = createStation("교대역");
        Station downStation = createStation("강남역");
        int distance = 10;
        int duration = 10;

        // when : 기능 수행
        Section section = createSection(line, upStation, downStation,  distance, duration);

        // then : 결과 확인
        assertThat(section.getLine()).isEqualTo(line);
        assertThat(section.getUpStation()).isEqualTo(upStation);
        assertThat(section.getDownStation()).isEqualTo(downStation);
        assertThat(section.getDistance()).isEqualTo(distance);
        assertThat(section.getDuration()).isEqualTo(duration);
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