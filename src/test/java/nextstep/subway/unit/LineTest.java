package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    @DisplayName("첫차 시간은 막차 시간보다 늦을 수 없다.")
    @Test
    void create_exception() {
        LocalTime firstTime = LocalTime.of(1, 0);
        LocalTime lastTime = LocalTime.of(0, 0);

        assertThatThrownBy(() -> new Line("2호선", "green", 0, firstTime, lastTime, 10))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    void addSection() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        line.addSection(강남역, 역삼역, 10);
        line.addSection(역삼역, 삼성역, 5);

        assertThat(line.getStations()).containsExactly(강남역, 역삼역, 삼성역);
    }

    @DisplayName("상행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        line.addSection(강남역, 역삼역, 10);
        line.addSection(강남역, 삼성역, 5);

        assertThat(line.getSections().size()).isEqualTo(2);
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(5);
    }

    @DisplayName("하행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle2() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        line.addSection(강남역, 역삼역, 10);
        line.addSection(삼성역, 역삼역, 5);

        assertThat(line.getSections().size()).isEqualTo(2);
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(5);
    }

    @DisplayName("목록 앞에 추가할 경우")
    @Test
    void addSectionInFront() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        line.addSection(강남역, 역삼역, 10);
        line.addSection(삼성역, 강남역, 5);

        assertThat(line.getSections().size()).isEqualTo(2);
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(역삼역);
        assertThat(section.getDistance()).isEqualTo(10);
    }

    @DisplayName("목록 뒤에 추가할 경우")
    @Test
    void addSectionBehind() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        line.addSection(강남역, 역삼역, 10);
        line.addSection(역삼역, 삼성역, 5);

        assertThat(line.getSections().size()).isEqualTo(2);
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 역삼역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(5);
    }

    @Test
    void getStations() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10);
        line.addSection(강남역, 삼성역, 5);

        List<Station> result = line.getStations();

        assertThat(result).containsExactly(강남역, 삼성역, 역삼역);
    }

    @DisplayName("이미 존재하는 구간 추가 시 에러 발생")
    @Test
    void addSectionAlreadyIncluded() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10);

        assertThatThrownBy(() -> line.addSection(강남역, 역삼역, 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void removeSection() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10);
        line.addSection(역삼역, 삼성역, 5);

        line.deleteSection(삼성역);

        assertThat(line.getStations()).containsExactly(강남역, 역삼역);
    }

    @Test
    void removeSectionInFront() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10);
        line.addSection(역삼역, 삼성역, 5);

        line.deleteSection(강남역);

        assertThat(line.getStations()).containsExactly(역삼역, 삼성역);
    }

    @Test
    void removeSectionInMiddle() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10);
        line.addSection(역삼역, 삼성역, 5);

        line.deleteSection(역삼역);

        assertThat(line.getStations()).containsExactly(강남역, 삼성역);
    }

    @DisplayName("구간이 하나인 노선에서 역 삭제 시 에러 발생")
    @Test
    void removeSectionNotEndOfList() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10);

        assertThatThrownBy(() -> line.deleteSection(역삼역))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    void getSchedule() {
        // given
        Line line = new Line("2호선", "green", 0,
                LocalTime.of(5, 0),
                LocalTime.of(22, 0), 60);
        // when
        List<LocalTime> schedule = line.getFirstUpSchedule();
        // then
        assertThat(schedule).hasSize(18);
    }

    @Test
    void getNextSchedule() {
        // given
        Line line = new Line("2호선", "green", 0,
                LocalTime.of(5, 0),
                LocalTime.of(22, 0), 60);
        // when
        LocalDateTime searchDate = LocalDateTime.of(2023, 3, 5, 18, 1);
        LocalDateTime schedule = line.getFirstUpNextSchedule(searchDate);
        // then
        assertThat(schedule).isEqualTo(LocalDateTime.of(2023, 3, 5, 19, 0));
    }

    @DisplayName("조회한 일자가 막차 시간을 넘어서면 다음날 첫차 시간을 조회된다.")
    @Test
    void getNextSchedule2() {
        // given
        Line line = new Line("2호선", "green", 0,
                LocalTime.of(5, 0),
                LocalTime.of(22, 0), 60);
        // when
        LocalDateTime searchDate = LocalDateTime.of(2023, 3, 5, 22, 1);
        LocalDateTime schedule = line.getFirstUpNextSchedule(searchDate);
        // then
        assertThat(schedule).isEqualTo(LocalDateTime.of(2023, 3, 6, 5, 0));
    }
}
