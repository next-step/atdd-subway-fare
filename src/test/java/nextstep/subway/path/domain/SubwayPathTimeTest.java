package nextstep.subway.path.domain;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathTimeTest {

    private final Station 강남역 = new Station("강남역");
    private final Station 역삼역 = new Station("역삼역");
    private final Station 삼성역 = new Station("삼성역");
    private final Station 사당역 = new Station("사당역");
    private final Line 이호선 = new Line("2호선", "green", 0, LocalTime.of(5, 0), LocalTime.of(23, 0), 13);
    private final Line 삼호선 = new Line("3호선", "orange", 0, LocalTime.of(5, 0), LocalTime.of(23, 0), 10);
    private final Line 사호선 = new Line("4호선", "blue", 0, LocalTime.of(5, 0), LocalTime.of(23, 0), 8);

    @BeforeEach
    void setUp() {
        이호선.addSection(강남역, 역삼역, 10, 10);
        삼호선.addSection(역삼역, 사당역, 10, 10);
        삼호선.addSection(삼성역, 사당역, 3, 3);
        사호선.addSection(강남역, 삼성역, 5, 5);
    }

    @Test
    @DisplayName("도착 시간 구하기")
    void getArriveTime() {
        // given
        SubwayGraph subwayGraph = new SubwayGraph(Lists.newArrayList(이호선, 삼호선, 사호선), PathType.ARRIVAL_TIME);
        List<PathResult> pathResults = subwayGraph.findAllPath(강남역, 사당역);
        SubwayPathTime subwayPathTime = new SubwayPathTime(pathResults);
        LocalDateTime dateTime = LocalDateTime.of(2021, 4, 5, 5, 3);

        // when
        FastPathResult fastPathResult = subwayPathTime.getFastPathResult(dateTime);

        // then
        assertThat(fastPathResult.getArriveTime()).isEqualTo(LocalDateTime.of(2021, 4, 5, 5, 20));
    }
}
