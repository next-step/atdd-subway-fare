package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {
    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;

    private Line line;

    private Sections sections;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        삼성역 = new Station("삼성역");

        line = new Line("2호선", "green");

        sections = new Sections();
        sections.add(new Section(line, 강남역, 역삼역, 5, 10));
    }

    @DisplayName("add메서드를 테스트")
    @Test
    void add() {
        // when
        sections.add(new Section(line, 역삼역, 삼성역, 4, 8));

        // then
        assertThat(sections.getStations()).containsExactly(강남역, 역삼역, 삼성역);
        assertThat(sections.totalDistance()).isEqualTo(9);
        assertThat(sections.totalDuration()).isEqualTo(18);
    }

    @DisplayName("delete메서드를 테스트(중간역 제거)")
    @Test
    void delete() {
        // given
        sections.add(new Section(line, 역삼역, 삼성역, 4, 8));

        // when
        sections.delete(역삼역);

        // then
        assertThat(sections.getStations()).containsExactly(강남역, 삼성역);
        assertThat(sections.totalDistance()).isEqualTo(9);
        assertThat(sections.totalDuration()).isEqualTo(18);
    }

    @DisplayName("delete메서드를 테스트(상행종점역 제거)")
    @Test
    void delete2() {
        // given
        sections.add(new Section(line, 역삼역, 삼성역, 4, 8));

        // when
        sections.delete(강남역);

        // then
        assertThat(sections.getStations()).containsExactly(역삼역, 삼성역);
        assertThat(sections.totalDistance()).isEqualTo(4);
        assertThat(sections.totalDuration()).isEqualTo(8);
    }

    @DisplayName("delete메서드를 테스트(하행종점역 제거)")
    @Test
    void delete3() {
        // given
        sections.add(new Section(line, 역삼역, 삼성역, 4, 8));

        // when
        sections.delete(삼성역);

        // then
        assertThat(sections.getStations()).containsExactly(강남역, 역삼역);
        assertThat(sections.totalDistance()).isEqualTo(5);
        assertThat(sections.totalDuration()).isEqualTo(10);
    }

    @DisplayName("extractFare메서드를 테스트")
    @Test
    void extractFare() {
        // when
        sections.add(new Section(line, 역삼역, 삼성역, 4, 8));

        // then
        assertThat(sections.fare()).isEqualTo(1250);
    }
}
