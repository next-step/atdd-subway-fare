package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Price;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class SectionsTest {

    Station 교대역;
    Station 강남역;
    Station 삼성역;
    Station 남부터미널역;
    Station 양재역;
    Line 이호선;
    Line 삼호선;
    Line 신분당선;
    Sections sections;

    @BeforeEach
    void setUp() {
        //given
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        남부터미널역 = createStation(3L, "남부터미널역");

        삼성역 = createStation(4L, "삼성역");
        양재역 = createStation(5L, "양재역");

        이호선 = new Line("2호선", "green");
        삼호선 = new Line("3호선", "orange", Price.of(500));
        신분당선 = new Line("신분선", "red", Price.of(1_000));

        이호선.addSection(교대역, 강남역, 3, 3);
        이호선.addSection(강남역, 삼성역, 5, 10);
        sections = new Sections(이호선.getSections());
    }

    @Test
    @DisplayName("총 소요 시간")
    void totalDuration() {
        //when
        final int totalDuration = sections.totalDuration();

        //then
        assertThat(totalDuration).isEqualTo(13);
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }

    @Test
    @DisplayName("구간 가운데에 추가")
    void addSection() {
        sections.add(new Section(이호선, 강남역, 남부터미널역, 2, 5));

        Section section = sections.getSections().get(sections.getSections().size() - 2);

        assertAll(
            () -> assertThat(section.getUpStation()).isEqualTo(남부터미널역),
            () -> assertThat(section.getDistance()).isEqualTo(3),
            () -> assertThat(section.getDuration()).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("가운데 구간 삭제")
    void deleteSection() {
        //when
        sections.delete(강남역);

        //then
        final List<Section> sectionList = sections.getSections();

        //then
        assertThat(sectionList).hasSize(1);

        final Section section = sectionList.get(0);
        assertThat(section.getDuration()).isEqualTo(13);
        assertThat(section.getDistance()).isEqualTo(8);
    }

    @Test
    @DisplayName("노선이 가진 구간들 중 값이 제일 비싼 노선의 값을 가져옴")
    void maximumPrice() {
        //when
        sections.add(new Section(삼호선, 강남역, 남부터미널역, 5, 10));
        sections.add(new Section(신분당선, 강남역, 양재역, 5, 10));
        //then
        assertThat(sections.maximumPrice()).isEqualTo(1000);
    }

}