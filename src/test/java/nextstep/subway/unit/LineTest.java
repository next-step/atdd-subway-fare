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
        addFirstSection(강남역, 역삼역);
        addSecondSection(역삼역, 삼성역);

        // then
        assertThat(line.getStations()).containsExactly(강남역, 역삼역, 삼성역);
    }

    @DisplayName("상행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle() {
        // when
        addFirstSection(강남역, 역삼역);
        addSecondSection(강남역, 삼성역);

        // then
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
        // when
        addFirstSection(강남역, 역삼역);
        addSecondSection(삼성역, 역삼역);

        // then
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
        // when
        addFirstSection(강남역, 역삼역);
        addSecondSection(삼성역, 강남역);

        // then
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
        // when
        addFirstSection(강남역, 역삼역);
        addSecondSection(역삼역, 삼성역);

        // then
        assertThat(line.getSections().size()).isEqualTo(2);
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 역삼역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(5);
    }

    @Test
    void getStations() {
        // given
        addFirstSection(강남역, 역삼역);
        addSecondSection(강남역, 삼성역);

        // when
        List<Station> result = line.getStations();

        // then
        assertThat(result).containsExactly(강남역, 삼성역, 역삼역);
    }

    @DisplayName("마지막에 구간 추가시 총 소요시간을 구한다")
    @Test
    public void get_duration_when_add_last() {
        // given
        addFirstSection(강남역, 역삼역);
        addSecondSection(역삼역, 삼성역);

        // when
        int duration = line.getTotalDuration();

        // then
        assertThat(duration).isEqualTo(5);
    }

    @DisplayName("중간에 구간 삽입시 총 소요시간을 구한다")
    @Test
    public void get_duration_when_add_middle() {
        // given
        addFirstSection(강남역, 역삼역);
        addSecondSection(강남역, 삼성역);

        // when
        int duration = line.getTotalDuration();

        // then
        assertThat(duration).isEqualTo(3);
    }

    @DisplayName("이미 존재하는 구간 추가 시 에러 발생")
    @Test
    void addSectionAlreadyIncluded() {
        addFirstSection(강남역, 역삼역);

        assertThatThrownBy(() -> line.addSection(강남역, 역삼역, 5, 2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void removeSection() {
        addFirstSection(강남역, 역삼역);
        addSecondSection(역삼역, 삼성역);

        line.deleteSection(삼성역);

        assertThat(line.getStations()).containsExactly(강남역, 역삼역);
    }

    @Test
    void removeSectionInFront() {
        addFirstSection(강남역, 역삼역);
        addSecondSection(역삼역, 삼성역);

        line.deleteSection(강남역);

        assertThat(line.getStations()).containsExactly(역삼역, 삼성역);
    }

    @Test
    void removeSectionInMiddle() {
        addFirstSection(강남역, 역삼역);
        addSecondSection(역삼역, 삼성역);

        line.deleteSection(역삼역);

        assertThat(line.getStations()).containsExactly(강남역, 삼성역);
    }

    @DisplayName("구간이 하나인 노선에서 역 삭제 시 에러 발생")
    @Test
    void removeSectionNotEndOfList() {
        addFirstSection(강남역, 역삼역);

        assertThatThrownBy(() -> line.deleteSection(역삼역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    void addFirstSection(Station upStation, Station downStation) {
        line.addSection(upStation, downStation, 10, 3);
    }

    void addSecondSection(Station upStation, Station downStation) {
        line.addSection(upStation, downStation, 5, 2);
    }
}
