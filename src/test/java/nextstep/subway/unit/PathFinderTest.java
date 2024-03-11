package nextstep.subway.unit;

import nextstep.subway.Exception.SubwayException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.application.dto.PathResponse;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathFinderTest {
    private final Station 교대역 = new Station(1L, "교대역");
    private final Station 강남역 = new Station(2L, "강남역");
    private final Station 양재역 = new Station(3L, "양재역");
    private final Station 남부터미널역 = new Station(4L, "남부터미널역");
    private final Station 미금역 = new Station(5L, "미금역");
    private final Station 정자역 = new Station(6L, "정자역");
    private final Line 이호선 = new Line(1L, "이호선", "green", 교대역, 강남역, 10L, 3L);
    private final Line 신분당선 = new Line(2L, "신분당선", "red", 강남역, 양재역, 14L, 5L);
    private final Line 삼호선 = new Line(3L, "삼호선", "orange", 양재역, 교대역, 23L, 10L);
    private final Line 분당선 = new Line(4L, "분당선", "yellow", 미금역, 정자역, 15L, 7L);
    private final List<Line> LINES = List.of(이호선, 신분당선, 삼호선, 분당선);

    @BeforeEach
    void setUp() {
        삼호선.addSection(new Section(삼호선, 양재역, 남부터미널역, 5L, 6L));
    }

    @DisplayName("최단 시간 경로 조회")
    @Test
    void shortestDistancePath() {
        PathResponse pathResponse = new Path(LINES).shortestPath(교대역, 양재역, PathType.DURATION);

        assertThat(pathResponse.getStations()).containsExactly(교대역, 강남역, 양재역);
        assertThat(pathResponse.getDistance()).isEqualTo(24L);
        assertThat(pathResponse.getDuration()).isEqualTo(8L);
    }

    @DisplayName("최단 거리 경로 조회")
    @Test
    void shortestDurationPath() {
        PathResponse pathResponse = new Path(LINES).shortestPath(교대역, 양재역, PathType.DISTANCE);

        assertThat(pathResponse.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(pathResponse.getDistance()).isEqualTo(23L);
        assertThat(pathResponse.getDuration()).isEqualTo(10L);
    }

    @DisplayName("에러_최단 거리 경로 조회_출발역 도착역 같음")
    @Test
    void error_shortestPath_target_source_same() {
        assertThatThrownBy(() -> new Path(LINES).shortestPath(교대역, 교대역, PathType.DISTANCE))
                .isInstanceOf(SubwayException.class)
                .hasMessage("출발역과 도착역이 같습니다.");
    }

    @DisplayName("에러_최단 거리 경로 조회_출발역 도착역 연결되지 않음")
    @Test
    void error_shortestPath_target_source_not_connected() {
        assertThatThrownBy(() -> new Path(LINES).shortestPath(교대역, 미금역, PathType.DISTANCE))
                .isInstanceOf(SubwayException.class)
                .hasMessage("연결되지 않은 역 정보입니다.");
    }
}
