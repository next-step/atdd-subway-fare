package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간들 (Sections)")
class SectionsTest {

    @DisplayName("구간의 모든 소요시간의 합을 구한다.(새로 연장된)")
    @Test
    void totalDurationByNewLine() {
        // given
        final Station 강남역 = new Station("강남역");
        final Station 역삼역 = new Station("역삼역");
        final Station 삼성역 = new Station("삼성역");

        final Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10, 10);
        line.addSection(역삼역, 삼성역, 5, 5);

        final Sections sections = new Sections(line.getSections());

        // when
        final int actual = sections.totalDuration();

        // then
        assertThat(actual).isEqualTo(15);
    }

//    @DisplayName("중간에 들어온 구간의 모든 소요시간의 합을 구한다.")
//    @Test
//    void totalDuration() {
//        // given
//        final Station 강남역 = new Station("강남역");
//        final Station 역삼역 = new Station("역삼역");
//        final Station 삼성역 = new Station("삼성역");
//
//        final Line line = new Line("2호선", "green");
//        line.addSection(강남역, 역삼역, 10, 10);
//        line.addSection(강남역, 삼성역, 5, 5);
//
//        final Sections sections = new Sections(line.getSections());
//
//        // when
//        final int actual = sections.totalDuration();
//
//        // then
//        assertThat(actual).isEqualTo(10);
//    }
}
