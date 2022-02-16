package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineTest {
    Station 강남역;
    Station 역삼역;
    Station 삼성역;

    Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        삼성역 = new Station("삼성역");

        이호선 = new Line("2호선", "green");
    }

    @Test
    void addSection() {
        // when
        이호선_구간_추가(강남역, 역삼역, 10);
        이호선_구간_추가(역삼역, 삼성역, 5);

        // then
        assertThat(이호선.getStations()).containsExactly(강남역, 역삼역, 삼성역);
    }

    @DisplayName("상행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle() {
        // when
        이호선_구간_추가(강남역, 역삼역, 10);
        이호선_구간_추가(강남역, 삼성역, 4);

        // then
        Section section = 이호선.getSections().stream()
                .filter(it -> it.getUpStation() == 삼성역)
                .findFirst().orElseThrow(RuntimeException::new);

        assertAll(() -> {
            assertThat(이호선.getSections().size()).isEqualTo(2);
            assertThat(section.getDownStation()).isEqualTo(역삼역);
            assertThat(section.getDistance()).isEqualTo(6);
            assertThat(section.getDuration()).isEqualTo(6);
        });
    }

    @DisplayName("하행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle2() {
        // when
        이호선_구간_추가(강남역, 역삼역, 10);
        이호선_구간_추가(삼성역, 역삼역, 4);

        // then
        Section section = 이호선.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);

        assertAll(() -> {
            assertThat(이호선.getSections().size()).isEqualTo(2);
            assertThat(section.getDownStation()).isEqualTo(삼성역);
            assertThat(section.getDistance()).isEqualTo(6);
            assertThat(section.getDuration()).isEqualTo(6);
        });
    }

    @DisplayName("목록 앞에 추가할 경우")
    @Test
    void addSectionInFront() {
        // when
        이호선_구간_추가(강남역, 역삼역, 10);
        이호선_구간_추가(삼성역, 강남역, 5);

        // then
        Section section = 이호선.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);

        assertAll(() -> {
            assertThat(이호선.getSections().size()).isEqualTo(2);
            assertThat(section.getDownStation()).isEqualTo(역삼역);
            assertThat(section.getDistance()).isEqualTo(10);
            assertThat(section.getDuration()).isEqualTo(10);
        });
    }

    @DisplayName("목록 뒤에 추가할 경우")
    @Test
    void addSectionBehind() {
        // when
        이호선_구간_추가(강남역, 역삼역, 10);
        이호선_구간_추가(역삼역, 삼성역, 5);

        // then
        Section section = 이호선.getSections().stream()
                .filter(it -> it.getUpStation() == 역삼역)
                .findFirst().orElseThrow(RuntimeException::new);

        assertAll(() -> {
            assertThat(이호선.getSections().size()).isEqualTo(2);
            assertThat(section.getDownStation()).isEqualTo(삼성역);
            assertThat(section.getDistance()).isEqualTo(5);
            assertThat(section.getDuration()).isEqualTo(5);
        });
    }

    @Test
    void getStations() {
        // given
        이호선_구간_추가(강남역, 역삼역, 10);
        이호선_구간_추가(강남역, 삼성역, 5);

        // when
        List<Station> result = 이호선.getStations();

        // then
        assertThat(result).containsExactly(강남역, 삼성역, 역삼역);
    }

    @DisplayName("이미 존재하는 구간 추가 시 에러 발생")
    @Test
    void addSectionAlreadyIncluded() {
        // given
        이호선_구간_추가(강남역, 역삼역, 10);

        // when, then
        assertThatThrownBy(() -> 이호선.addSection(강남역, 역삼역, 5, 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void removeSection() {
        // given
        이호선_구간_추가(강남역, 역삼역, 10);
        이호선_구간_추가(역삼역, 삼성역, 5);

        // when
        이호선.deleteSection(삼성역);

        // then
        assertThat(이호선.getStations()).containsExactly(강남역, 역삼역);
    }

    @Test
    void removeSectionInFront() {
        // given
        이호선_구간_추가(강남역, 역삼역, 10);
        이호선_구간_추가(역삼역, 삼성역, 5);

        // when
        이호선.deleteSection(강남역);

        // then
        assertThat(이호선.getStations()).containsExactly(역삼역, 삼성역);
    }

    @Test
    void removeSectionInMiddle() {
        // given
        이호선_구간_추가(강남역, 역삼역, 10);
        이호선_구간_추가(역삼역, 삼성역, 5);

        // when
        이호선.deleteSection(역삼역);

        // then
        Section section = 이호선.getSections()
                .stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst()
                .orElseThrow(RuntimeException::new);

        assertAll(() -> {
            assertThat(이호선.getStations()).containsExactly(강남역, 삼성역);
            assertThat(section.getDistance()).isEqualTo(15);
            assertThat(section.getDuration()).isEqualTo(15);
        });
    }

    @DisplayName("구간이 하나인 노선에서 역 삭제 시 에러 발생")
    @Test
    void removeSectionNotEndOfList() {
        // given
        이호선_구간_추가(강남역, 역삼역, 10);

        // when, then
        assertThatThrownBy(() -> 이호선.deleteSection(역삼역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void 이호선_구간_추가(Station upStation, Station downStation, int value) {
        이호선.addSection(upStation, downStation, value, value);
    }
}
