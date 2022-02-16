package nextstep.subway.unit;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class PathServiceTest extends ServiceTestFixture {
    @Autowired
    private PathService pathService;

    @DisplayName("DISTANCE 타입으로 경로 조회")
    @Test
    void findPathDistance() {
        // when
        PathResponse path = pathService.findPath2(교대역.getId(), 양재역.getId(), "DISTANCE");

        // then
        assertThat(path.getStations().get(0).getName()).isEqualTo(교대역.getName());
        assertThat(path.getStations().get(1).getName()).isEqualTo(남부터미널역.getName());
        assertThat(path.getStations().get(2).getName()).isEqualTo(양재역.getName());
        assertThat(path.getDistance()).isEqualTo(8);
        assertThat(path.getDuration()).isEqualTo(12);
    }

    @DisplayName("DURATION 타입으로 경로 조회")
    @Test
    void findPathDuration() {
        // when
        PathResponse path = pathService.findPath2(교대역.getId(), 양재역.getId(), "DURATION");

        // then
        assertThat(path.getStations().get(0).getName()).isEqualTo(교대역.getName());
        assertThat(path.getStations().get(1).getName()).isEqualTo(강남역.getName());
        assertThat(path.getStations().get(2).getName()).isEqualTo(양재역.getName());
        assertThat(path.getDistance()).isEqualTo(15);
        assertThat(path.getDuration()).isEqualTo(10);
    }

}
