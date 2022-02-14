package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    private int DISTANCE = 100;
    private int DURATION = 10;

    Station 강남역;
    Station 역삼역;
    Station 삼성역;
    Line line;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        삼성역 = new Station("삼성역");
        line = new Line("2호선", "green");
    }

    @Test
    void addSection() {
        // when
        line.addSection(강남역, 역삼역, 10, 10);
        line.addSection(역삼역, 삼성역, 5, 10);

        // then
        assertThat(line.getStations()).containsExactly(강남역, 역삼역, 삼성역);
    }

    @DisplayName("상행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle() {
        // when
        line.addSection(강남역, 역삼역, DISTANCE, DURATION);
        line.addSection(강남역, 삼성역, 20, 2);

        // then
        assertThat(line.getSections().size()).isEqualTo(2);
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(20);
    }

    @DisplayName("하행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle2() {
        // when
        line.addSection(강남역, 역삼역, DISTANCE, DURATION);
        line.addSection(삼성역, 역삼역, 20, 2);

        // then
        assertThat(line.getSections().size()).isEqualTo(2);
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(80);
    }

    @DisplayName("목록 앞에 추가할 경우")
    @Test
    void addSectionInFront() {
        // when
        line.addSection(강남역, 역삼역, DISTANCE, DURATION);
        line.addSection(삼성역, 강남역, 20, 2);

        // then
        assertThat(line.getSections().size()).isEqualTo(2);
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(역삼역);
        assertThat(section.getDistance()).isEqualTo(100);
    }

    @DisplayName("목록 뒤에 추가할 경우")
    @Test
    void addSectionBehind() {
        // when
        line.addSection(강남역, 역삼역, DISTANCE, DURATION);
        line.addSection(역삼역, 삼성역, 20, 2);

        // then
        assertThat(line.getSections().size()).isEqualTo(2);
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 역삼역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(20);
    }

    @Test
    void getStations() {
        // given
        line.addSection(강남역, 역삼역, DISTANCE, DURATION);
        line.addSection(강남역, 삼성역, 20, 2);

        // when
        List<Station> result = line.getStations();

        // then
        assertThat(result).containsExactly(강남역, 삼성역, 역삼역);
    }

    @DisplayName("이미 존재하는 구간 추가 시 에러 발생")
    @Test
    void addSectionAlreadyIncluded() {
        // when
        line.addSection(강남역, 역삼역, DISTANCE, DURATION);

        // then
        assertThatThrownBy(() -> line.addSection(강남역, 역삼역, 20, 2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void removeSection() {
        // given
        line.addSection(강남역, 역삼역, DISTANCE, DURATION);
        line.addSection(역삼역, 삼성역, 20, 2);

        // when
        line.deleteSection(삼성역);

        // then
        assertThat(line.getStations()).containsExactly(강남역, 역삼역);
    }

    @Test
    void removeSectionInFront() {
        // given
        line.addSection(강남역, 역삼역, DISTANCE, DURATION);
        line.addSection(역삼역, 삼성역, 20, 2);

        // when
        line.deleteSection(강남역);

        // then
        assertThat(line.getStations()).containsExactly(역삼역, 삼성역);
    }

    @Test
    void removeSectionInMiddle() {
        // given
        line.addSection(강남역, 역삼역, DISTANCE, DURATION);
        line.addSection(역삼역, 삼성역, 20, 2);

        // when
        line.deleteSection(역삼역);

        // then
        assertThat(line.getStations()).containsExactly(강남역, 삼성역);
    }

    @DisplayName("구간이 하나인 노선에서 역 삭제 시 에러 발생")
    @Test
    void removeSectionNotEndOfList() {
        // given
        line.addSection(강남역, 역삼역, DISTANCE, DURATION);

        // then
        assertThatThrownBy(() -> line.deleteSection(역삼역))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
