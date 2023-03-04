package nextstep.subway.unit;

import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.domain.Fare.ZERO_FARE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    Station 강남역;
    Station 역삼역;
    Station 삼성역;
    Station 구의역;

    Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        삼성역 = new Station("삼성역");
        구의역 = new Station("뚝섬역");

        이호선 = new Line("2호선", "green", ZERO_FARE);
        이호선.addSection(강남역, 역삼역, 10, 7);
        이호선.addSection(역삼역, 삼성역, 5, 4);
    }
    
    @Test
    void addSection() {
        이호선.addSection(삼성역, 구의역, 5, 4);
        assertThat(이호선.getStations()).containsExactly(강남역, 역삼역, 삼성역, 구의역);
    }

    @DisplayName("상행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle() {
        assertThat(이호선.getSections().size()).isEqualTo(2);
        Section section = 이호선.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(Distance.of(5));
        assertThat(section.getDuration()).isEqualTo(Duration.of(4));
    }

    @DisplayName("하행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle2() {
        assertThat(이호선.getSections().size()).isEqualTo(2);
        Section section = 이호선.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(Distance.of(5));
        assertThat(section.getDuration()).isEqualTo(Duration.of(3));
    }

    @DisplayName("목록 앞에 추가할 경우")
    @Test
    void addSectionInFront() {
        assertThat(이호선.getSections().size()).isEqualTo(2);
        Section section = 이호선.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(역삼역);
        assertThat(section.getDistance()).isEqualTo(Distance.of(10));
        assertThat(section.getDuration()).isEqualTo(Duration.of(7));
    }

    @DisplayName("목록 뒤에 추가할 경우")
    @Test
    void addSectionBehind() {
        assertThat(이호선.getSections().size()).isEqualTo(2);
        Section section = 이호선.getSections().stream()
                .filter(it -> it.getUpStation() == 역삼역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(Distance.of(5));
        assertThat(section.getDuration()).isEqualTo(Duration.of(4));
    }

    @Test
    void getStations() {
        List<Station> result = 이호선.getStations();

        assertThat(result).containsExactly(강남역, 삼성역, 역삼역);
    }

    @DisplayName("이미 존재하는 구간 추가 시 에러 발생")
    @Test
    void addSectionAlreadyIncluded() {
        이호선.addSection(강남역, 역삼역, 10, 7);

        assertThatThrownBy(() -> 이호선.addSection(강남역, 역삼역, 5, 4))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void removeSection() {
        이호선.deleteSection(삼성역);

        assertThat(이호선.getStations()).containsExactly(강남역, 역삼역);
    }

    @Test
    void removeSectionInFront() {
        이호선.deleteSection(강남역);

        assertThat(이호선.getStations()).containsExactly(역삼역, 삼성역);
    }

    @Test
    void removeSectionInMiddle() {
        이호선.deleteSection(역삼역);

        assertThat(이호선.getStations()).containsExactly(강남역, 삼성역);
    }

    @DisplayName("구간이 하나인 노선에서 역 삭제 시 에러 발생")
    @Test
    void removeSectionNotEndOfList() {
        Line line = new Line("2호선", "green", ZERO_FARE);
        line.addSection(강남역, 역삼역, 10, 7);

        assertThatThrownBy(() -> line.deleteSection(역삼역))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
