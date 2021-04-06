package nextstep.subway.path.domain;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayGraphTest {

    private final Station 강남역 = new Station("강남역");
    private final Station 역삼역 = new Station("역삼역");
    private final Station 삼성역 = new Station("삼성역");
    private final Station 사당역 = new Station("사당역");
    private final Line 이호선 = new Line("2호선", "green");
    private final Line 삼호선 = new Line("3호선", "orange");
    private final Line 사호선 = new Line("4호선", "blue");

    @BeforeEach
    void setUp() {
        이호선.addSection(강남역, 역삼역, 10, 10);
        삼호선.addSection(역삼역, 사당역, 10, 10);
        삼호선.addSection(삼성역, 사당역, 3, 3);
        사호선.addSection(강남역, 삼성역, 5, 5);
    }

    @Test
    void findPath() {
        // given
        SubwayGraph subwayGraph = new SubwayGraph(Lists.newArrayList(이호선, 삼호선, 사호선), PathType.DISTANCE);

        // when
        PathResult pathResult = subwayGraph.findPath(강남역, 사당역);

        // then
        assertThat(pathResult.getStations()).containsExactly(강남역, 삼성역, 사당역);
    }

    @Test
    void findAllPath() {
        // given
        SubwayGraph subwayGraph = new SubwayGraph(Lists.newArrayList(이호선, 삼호선, 사호선), PathType.ARRIVAL_TIME);

        // when
        List<PathResult> pathResults = subwayGraph.findAllPath(강남역, 사당역);

        // then
        assertThat(pathResults.get(0).getStations()).containsExactly(강남역, 삼성역, 사당역);
        assertThat(pathResults.get(1).getStations()).containsExactly(강남역, 역삼역, 삼성역, 사당역);
    }
}
