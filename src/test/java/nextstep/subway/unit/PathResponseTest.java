package nextstep.subway.unit;

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

public class PathResponseTest {
    // 인자로 받은 경로에 대한 응답 데이터를 올바르게 생성하는지 확인
    private final Station 교대역 = new Station(1L, "교대역");
    private final Station 강남역 = new Station(2L, "강남역");
    private final Station 양재역 = new Station(3L, "양재역");
    private final Station 남부터미널역 = new Station(4L, "남부터미널역");
    private final Line 이호선 = new Line(1L, "이호선", "green", 교대역, 강남역, 10, 3);
    private final Line 신분당선 = new Line(2L, "신분당선", "red", 강남역, 양재역, 14, 5);
    private final Line 삼호선 = new Line(3L, "삼호선", "orange", 양재역, 교대역, 23, 10);
    private final List<Line> LINES = List.of(이호선, 신분당선, 삼호선);

    @BeforeEach
    void setUp() {
        삼호선.addSection(new Section(삼호선, 양재역, 남부터미널역, 5, 6));
    }

    @DisplayName("최단 시간 경로 조회")
    @Test
    void shortestDistancePath() {
        PathResponse pathResponse = new PathResponse(new Path(LINES).shortestPath(교대역, 양재역, PathType.DURATION));

        assertThat(pathResponse.getStations()).containsExactly(교대역, 강남역, 양재역);
        assertThat(pathResponse.getDistance()).isEqualTo(24L);
        assertThat(pathResponse.getDuration()).isEqualTo(8L);
    }

    @DisplayName("최단 거리 경로 조회")
    @Test
    void shortestDurationPath() {
        PathResponse pathResponse = new PathResponse(new Path(LINES).shortestPath(교대역, 양재역, PathType.DISTANCE));

        assertThat(pathResponse.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(pathResponse.getDistance()).isEqualTo(23);
        assertThat(pathResponse.getDuration()).isEqualTo(10);
    }
}
