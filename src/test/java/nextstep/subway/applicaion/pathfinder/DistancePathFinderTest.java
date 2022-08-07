package nextstep.subway.applicaion.pathfinder;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DistancePathFinderTest {

    private DistancePathFinder target = new DistancePathFinder();

    @Test
    void 타입검사() {
        final boolean result = target.supports(PathType.DISTANCE);
        assertThat(result).isTrue();
    }

    @Test
    void 경로검사() {
        Station 교대역 = createStation(1L, "교대역");
        Station 강남역 = createStation(2L, "강남역");
        Station 양재역 = createStation(3L, "양재역");
        Station 남부터미널역 = createStation(4L, "남부터미널역");

        Line 신분당선 = new Line("신분당선", "red");
        Line 이호선 = new Line("2호선", "red");
        Line 삼호선 = new Line("3호선", "red");

        신분당선.addSection(강남역, 양재역, 3, 1);
        이호선.addSection(교대역, 강남역, 3, 2);
        삼호선.addSection(교대역, 남부터미널역, 5, 3);
        삼호선.addSection(남부터미널역, 양재역, 5, 3);

        final Path result = target.findPath(
                List.of(신분당선, 이호선, 삼호선),
                강남역,
                남부터미널역);

        assertThat(result).isNotNull();
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }

}