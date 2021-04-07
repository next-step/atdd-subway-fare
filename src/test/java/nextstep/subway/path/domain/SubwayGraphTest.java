package nextstep.subway.path.domain;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayGraphTest {
    @Test
    void findPath() {
        // given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 선릉역 = new Station("선릉역");
        Station 남부터미널 = new Station("남부터미널");
        Line line2 = new Line("2호선", "green", 900);
        line2.addSection(강남역, 역삼역, 10, 10);
        line2.addSection(역삼역, 선릉역, 10, 10);
        Line line3 = new Line("3호선", "green", 1200);
        line3.addSection(강남역, 남부터미널, 5, 10);
        SubwayGraph subwayGraph = new SubwayGraph(Lists.newArrayList(line2, line3), PathType.DISTANCE);

        // when
        PathResult pathResult = subwayGraph.findPath(선릉역, 남부터미널);

        // then
        assertThat(pathResult.getStations()).containsExactly(선릉역, 역삼역, 강남역, 남부터미널);
        assertThat(pathResult.getMaxExtraFare()).isEqualTo(1200);
    }
}
