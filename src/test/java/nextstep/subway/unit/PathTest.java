package nextstep.subway.unit;


import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathTest {

    Station 강남역;
    Station 역삼역;
    Station 삼성역;
    Line 이호선;
    Line 삼호선;
    Section 이호선_강남_역삼_구간;
    Section 이호선_역삼_삼성_구간;
    Section 삼호선_삼성역_강남역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        삼성역 = new Station("삼성역");

        이호선 = new Line("2호선", "green");
        삼호선 = new Line("3호선", "green");

        이호선_강남_역삼_구간 = new Section(이호선, 강남역, 역삼역, 10, 5);
        이호선_역삼_삼성_구간 = new Section(이호선, 역삼역, 삼성역, 5, 5);
        삼호선_삼성역_강남역 = new Section(삼호선, 삼성역, 강남역, 5, 5);
    }


    @DisplayName("경로에 포함된 모든 노선 조회")
    @Test
    void findAllIncludedLine() {
        Sections sections = new Sections(List.of(이호선_강남_역삼_구간, 이호선_역삼_삼성_구간, 삼호선_삼성역_강남역));
        Path path = new Path(sections);

        List<Line> lines = path.findIncludedLines();

        assertThat(lines).contains(이호선, 삼호선);
    }
}
