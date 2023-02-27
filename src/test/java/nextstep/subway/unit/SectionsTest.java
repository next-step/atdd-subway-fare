package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SectionsTest {

    private Station 강남역;
    private Station 역삼역;
    private Line 이호선;
    private Sections sections;

    @BeforeEach
    void setUp() {
        // Given
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        이호선 = new Line("2호선", "green");

        ArrayList<Section> initialSection = new ArrayList<>();
        initialSection.add(new Section(이호선, 강남역, 역삼역, 10, 10));

        sections = new Sections(initialSection);
    }

    @DisplayName("상행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle() {
        // Given
        Station 삼성역 = new Station("삼성역");

        // When
        sections.add(new Section(이호선, 강남역, 삼성역, 5, 5));

        // Then
        Section section = sections.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst()
                .orElseThrow(RuntimeException::new);

        assertAll(
                () -> assertThat(section.getDownStation()).isEqualTo(삼성역),
                () -> assertThat(section.getDistance()).isEqualTo(5),
                () -> assertThat(section.getDuration()).isEqualTo(5)
        );
    }

    @DisplayName("하행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle2() {
        // Given
        Station 삼성역 = new Station("삼성역");

        // When
        sections.add(new Section(이호선, 삼성역, 역삼역, 5, 5));

        Section section = sections.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst()
                .orElseThrow(RuntimeException::new);

        // Then
        assertAll(
                () -> assertThat(section.getDownStation()).isEqualTo(삼성역),
                () -> assertThat(section.getDistance()).isEqualTo(5),
                () -> assertThat(section.getDuration()).isEqualTo(5)
        );
    }

    /**
     * (이동거리)
     *
     * 강남역 <-5-> 삼성역 <-5-> 역삼역 <-3-> 잠실역
     */
    @DisplayName("구간의 이동거리")
    @Test
    void totalDistance() {
        // Given
        Station 삼성역 = new Station("삼성역");
        sections.add(new Section(이호선, 강남역, 삼성역, 5, 5));

        Station 잠실역 = new Station("잠실역");
        sections.add(new Section(이호선, 역삼역, 잠실역, 3, 3));

        // When
        int totalDistance = sections.totalDistance();

        // Then
        assertThat(totalDistance).isEqualTo(13);
    }

    /**
     * (이동시간)
     *
     * 강남역 <-5-> 삼성역 <-5-> 역삼역 <-3-> 잠실역
     */
    @DisplayName("구간의 이동시간")
    @Test
    void totalDuration() {
        // Given
        Station 삼성역 = new Station("삼성역");
        sections.add(new Section(이호선, 강남역, 삼성역, 5, 5));

        Station 잠실역 = new Station("잠실역");
        sections.add(new Section(이호선, 역삼역, 잠실역, 3, 3));

        // When
        int totalDuration = sections.totalDuration();

        // Then
        assertThat(totalDuration).isEqualTo(13);
    }
}
