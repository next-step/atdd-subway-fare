package nextstep.subway.maps.map.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.map.application.path.TimePath;
import nextstep.subway.utils.TestObjectUtils;

public class TimePathTest {

    private TimePath timePath;

    @BeforeEach
    void setUp() {
        Line 서울_지하철_3호선 = TestObjectUtils.createLine(3L, "3호선", "ORANGE", 0);
        LineStation 교대역 = new LineStation(1L, null, 0, 0);
        LineStation 남부터미널역 = new LineStation(4L, 1L, 1, 2);
        LineStation 양재역 = new LineStation(3L, 4L, 2, 2);
        서울_지하철_3호선.addLineStation(교대역);
        서울_지하철_3호선.addLineStation(남부터미널역);
        서울_지하철_3호선.addLineStation(양재역);

        LineStationEdge 교대역_에찌 = new LineStationEdge(교대역, 서울_지하철_3호선);
        LineStationEdge 남부터미널역_에찌 = new LineStationEdge(남부터미널역, 서울_지하철_3호선);
        LineStationEdge 양재역_에찌 = new LineStationEdge(양재역, 서울_지하철_3호선);

        List<LineStationEdge> lineStationEdges = Lists.newArrayList(
            교대역_에찌,
            남부터미널역_에찌,
            양재역_에찌
        );

        SubwayPath subwayPath = new SubwayPath(lineStationEdges);
        timePath = new TimePath(subwayPath);
    }

    @DisplayName("도착 시간을 알 수 있다.")
    @Test
    void 도착시간을_알아낸다() {
        // when
        LocalDateTime arrivalTime = timePath.getArrivalTime(
            LocalDateTime.of(2020, 8, 24, 14, 0));

        //then
        assertThat(arrivalTime)
            .isEqualTo(LocalDateTime.of(2020, 8, 24, 14, 4));
    }
}
