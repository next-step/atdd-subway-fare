package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class SectionsTest {

    @DisplayName("새로운 구간을 추가한다")
    @Test
    void insertSection() {
        Sections sections = new Sections(new ArrayList<>(List.of(
            new Section(null, new Station("강남역"), new Station("판교역"), 10, 15)
        )));

        sections.add(new Section(null, new Station("판교역"), new Station("양재역"), 2, 3));

        assertThat(sections.getSections())
            .extracting(Section::getDistance)
            .containsExactly(10, 2);

        assertThat(sections.getSections())
            .extracting(Section::getDuration)
            .containsExactly(15, 3);

        assertThat(sections.totalFare()).isEqualTo(1350);
    }

    @DisplayName("총 거리가 10km 이내라면 비용은 1250원으로 고정이다")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10})
    void totalFare_under10km(int distance) {
        Sections sections = new Sections(
            new Section(null, new Station("강남역"), new Station("판교역"), distance, 15)
        );

        assertThat(sections.totalFare()).isEqualTo(1250);
    }

    @DisplayName("총 거리가 50km 이내라면 10km 초과 ~ 50km 까지 5km 단위로 100원씩 추가된다")
    @ParameterizedTest
    @CsvSource({"12, 1350", "14, 1350", "15, 1350", "18, 1450"})
    void totalFare_under50km(int distance, int fare) {
        Sections sections = new Sections(
            new Section(null, new Station("강남역"), new Station("판교역"), distance, 15)
        );

        assertThat(sections.totalFare()).isEqualTo(fare);
    }

    @DisplayName("총 거리가 50km 초과시 8km마다 100원씩 추가된다")
    @ParameterizedTest
    @CsvSource({"50, 2050", "55, 2150", "57, 2150", "58, 2150"})
    void totalFare_over50km(int distance, int fare) {
        Sections sections = new Sections(
            new Section(null, new Station("강남역"), new Station("판교역"), distance, 15)
        );

        assertThat(sections.totalFare()).isEqualTo(fare);
    }
}