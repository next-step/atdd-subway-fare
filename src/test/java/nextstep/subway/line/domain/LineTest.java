package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineTest {
    @Test
    void addSection() {
        // given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        // when
        line.addSection(강남역, 역삼역, 10, 10);
        line.addSection(역삼역, 삼성역, 5, 5);

        // then
        assertThat(line.getStations()).containsExactly(강남역, 역삼역, 삼성역);
    }

    @DisplayName("상행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle() {
        // given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        // when
        line.addSection(강남역, 역삼역, 10, 10);
        line.addSection(강남역, 삼성역, 4, 4);

        // then
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 삼성역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertAll(() -> {
            assertThat(line.getSections().size()).isEqualTo(2);
            assertThat(section.getDownStation()).isEqualTo(역삼역);
            assertThat(section.getDistance()).isEqualTo(6);
            assertThat(section.getDuration()).isEqualTo(6);
        });
    }

    @DisplayName("하행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle2() {
        // given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        // when
        line.addSection(강남역, 역삼역, 10, 10);
        line.addSection(삼성역, 역삼역, 4, 4);

        // then
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);

        assertAll(() -> {
            assertThat(line.getSections().size()).isEqualTo(2);
            assertThat(section.getDownStation()).isEqualTo(삼성역);
            assertThat(section.getDistance()).isEqualTo(6);
            assertThat(section.getDuration()).isEqualTo(6);
        });
    }

    @DisplayName("목록 앞에 추가할 경우")
    @Test
    void addSectionInFront() {
        // given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        // when
        line.addSection(강남역, 역삼역, 10, 10);
        line.addSection(삼성역, 강남역, 5, 5);

        // then
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);

        assertAll(() -> {
            assertThat(line.getSections().size()).isEqualTo(2);
            assertThat(section.getDownStation()).isEqualTo(역삼역);
            assertThat(section.getDistance()).isEqualTo(10);
            assertThat(section.getDuration()).isEqualTo(10);
        });
    }

    @DisplayName("목록 뒤에 추가할 경우")
    @Test
    void addSectionBehind() {
        // given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        // when
        line.addSection(강남역, 역삼역, 10, 10);
        line.addSection(역삼역, 삼성역, 5, 5);

        // then
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 역삼역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertAll(() -> {
            assertThat(line.getSections().size()).isEqualTo(2);
            assertThat(section.getDownStation()).isEqualTo(삼성역);
            assertThat(section.getDistance()).isEqualTo(5);
            assertThat(section.getDuration()).isEqualTo(5);
        });
    }

    @Test
    void getStations() {
        // given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");

        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10, 10);
        line.addSection(강남역, 삼성역, 5, 5);

        // when
        List<Station> result = line.getStations();

        // then
        assertThat(result).containsExactly(강남역, 삼성역, 역삼역);
    }

    @DisplayName("이미 존재하는 구간 추가 시 에러 발생")
    @Test
    void addSectionAlreadyIncluded() {
        // given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");

        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10, 10);

        // when, then
        assertThatThrownBy(() -> line.addSection(강남역, 역삼역, 5, 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void removeSection() {
        // given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");

        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10, 10);
        line.addSection(역삼역, 삼성역, 5, 5);

        // when
        line.deleteSection(삼성역);

        // then
        assertThat(line.getStations()).containsExactly(강남역, 역삼역);
    }

    @Test
    void removeSectionInFront() {
        // given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");

        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10, 10);
        line.addSection(역삼역, 삼성역, 5, 5);

        // when
        line.deleteSection(강남역);

        // then
        assertThat(line.getStations()).containsExactly(역삼역, 삼성역);
    }

    @Test
    void removeSectionInMiddle() {
        // given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");

        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10, 10);
        line.addSection(역삼역, 삼성역, 5, 5);

        // when
        line.deleteSection(역삼역);

        // then
        Section section = line.getSections()
                .stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst()
                .orElseThrow(RuntimeException::new);

        assertAll(() -> {
            assertThat(line.getStations()).containsExactly(강남역, 삼성역);
            assertThat(section.getDistance()).isEqualTo(15);
            assertThat(section.getDuration()).isEqualTo(15);
        });
    }

    @DisplayName("구간이 하나인 노선에서 역 삭제 시 에러 발생")
    @Test
    void removeSectionNotEndOfList() {
        // given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");

        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10, 10);

        // when, then
        assertThatThrownBy(() -> line.deleteSection(역삼역))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
