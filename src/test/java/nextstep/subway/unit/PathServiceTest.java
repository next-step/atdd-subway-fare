package nextstep.subway.unit;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.FindType;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class PathServiceTest extends PathServiceTestData {
    @Autowired
    private PathService pathService;

    @DisplayName("DISTANCE 타입으로 경로 조회")
    @Test
    void findPathDistance() {
        // when
        PathResponse path = pathService.findPath(교대역.getId(), 양재역.getId(), FindType.DISTANCE);

        // then
        경로_조회됨(path, new Station[]{교대역, 남부터미널역, 양재역}, 8, 12);
    }

    @DisplayName("DURATION 타입으로 경로 조회")
    @Test
    void findPathDuration() {
        // when
        PathResponse path = pathService.findPath(교대역.getId(), 양재역.getId(), FindType.DURATION);

        // then
        경로_조회됨(path, new Station[]{교대역, 강남역, 양재역}, 15, 10);
    }

    private void 경로_조회됨(PathResponse path, Station[] stations, int distance, int duration) {
        assertThat(path.getStations().get(0).getName()).isEqualTo(stations[0].getName());
        assertThat(path.getStations().get(1).getName()).isEqualTo(stations[1].getName());
        assertThat(path.getStations().get(2).getName()).isEqualTo(stations[2].getName());
        assertThat(path.getDistance()).isEqualTo(distance);
        assertThat(path.getDuration()).isEqualTo(duration);
    }
}
