package nextstep.subway.domain;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간의 일급 객체")
class SectionsTest {

    private int DISTANCE = 100;
    private int DURATION = 10;
    private Station 강남역;
    private Station 판교역;
    private Station 정자역;
    private Station 미금역;
    private Line 신분당선;
    private Section 강남_정자;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");
        정자역 = new Station("정자역");
        미금역 = new Station("미금역");
        신분당선 = new Line();
        강남_정자 = Section.of(신분당선, 강남역, 정자역, DISTANCE, DURATION);
    }

    @DisplayName("구간 추가 - 상행역을 기준으로")
    @Test
    void add_up() {
        // given
        Sections sections = new Sections(Lists.newArrayList(강남_정자));
        Section 강남_판교 = Section.of(신분당선, 강남역, 판교역, 20, 2);

        // when
        sections.add(강남_판교);

        // then
        Integer totalDistance = sections.getSections()
                .stream()
                .map(Section::getDistance)
                .reduce(0, Integer::sum);
        Integer totalDuration = sections.getSections()
                .stream()
                .map(Section::getDuration)
                .reduce(0, Integer::sum);
        Assertions.assertThat(sections.getSections().size()).isEqualTo(2);
        Assertions.assertThat(totalDistance).isEqualTo(100);
        Assertions.assertThat(totalDuration).isEqualTo(10);
    }

    @DisplayName("구간 추가 - 하행역을 기준으로")
    @Test
    void add_dwon() {
        // given
        Sections sections = new Sections(Lists.newArrayList(강남_정자));
        Section 정자_미금 = Section.of(신분당선, 정자역, 미금역, 20, 2);

        // when
        sections.add(정자_미금);

        // then
        Integer totalDistance = sections.getSections()
                .stream()
                .map(Section::getDistance)
                .reduce(0, Integer::sum);
        Integer totalDuration = sections.getSections()
                .stream()
                .map(Section::getDuration)
                .reduce(0, Integer::sum);
        Assertions.assertThat(sections.getSections().size()).isEqualTo(2);
        Assertions.assertThat(totalDistance).isEqualTo(120);
        Assertions.assertThat(totalDuration).isEqualTo(12);
    }

}