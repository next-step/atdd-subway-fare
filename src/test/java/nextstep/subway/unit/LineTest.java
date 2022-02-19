package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {
    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;

    private Line line;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        삼성역 = new Station("삼성역");

        line = new Line("2호선", "green");
    }

    @Test
    void addSection() {
        line.addSection(강남역, 역삼역, 10);
        line.addSection(역삼역, 삼성역, 5);

        assertThat(line.getStations()).containsExactly(강남역, 역삼역, 삼성역);
    }

    @DisplayName("상행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle() {
        line.addSection(강남역, 역삼역, 10, 6);
        line.addSection(강남역, 삼성역, 5, 2);

        assertThat(line.getSections().size()).isEqualTo(2);
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 삼성역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(역삼역);
        assertThat(section.getDistance()).isEqualTo(5);
        assertThat(section.getDuration()).isEqualTo(4);
    }

    @DisplayName("하행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle2() {
        line.addSection(강남역, 역삼역, 10, 6);
        line.addSection(삼성역, 역삼역, 5, 2);

        assertThat(line.getSections().size()).isEqualTo(2);
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(5);
        assertThat(section.getDuration()).isEqualTo(4);
    }

    @DisplayName("목록 앞에 추가할 경우")
    @Test
    void addSectionInFront() {
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
        line.addSection(강남역, 역삼역, 10);
        line.addSection(강남역, 삼성역, 5);

        List<Station> result = line.getStations();

        assertThat(result).containsExactly(강남역, 삼성역, 역삼역);
    }

    @DisplayName("이미 존재하는 구간 추가 시 에러 발생")
    @Test
    void addSectionAlreadyIncluded() {
        line.addSection(강남역, 역삼역, 10);

        assertThatThrownBy(() -> line.addSection(강남역, 역삼역, 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void removeSection() {
        line.addSection(강남역, 역삼역, 10);
        line.addSection(역삼역, 삼성역, 5);

        line.deleteSection(삼성역);

        assertThat(line.getStations()).containsExactly(강남역, 역삼역);
    }

    @Test
    void removeSectionInFront() {
        line.addSection(강남역, 역삼역, 10);
        line.addSection(역삼역, 삼성역, 5);

        line.deleteSection(강남역);

        assertThat(line.getStations()).containsExactly(역삼역, 삼성역);
    }

    @Test
    void removeSectionInMiddle() {
        line.addSection(강남역, 역삼역, 10);
        line.addSection(역삼역, 삼성역, 5);

        line.deleteSection(역삼역);

        assertThat(line.getStations()).containsExactly(강남역, 삼성역);
    }

    @DisplayName("구간이 하나인 노선에서 역 삭제 시 에러 발생")
    @Test
    void removeSectionNotEndOfList() {
        line.addSection(강남역, 역삼역, 10);

        assertThatThrownBy(() -> line.deleteSection(역삼역))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
