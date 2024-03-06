package nextstep.subway.unit;

import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;

public class PathFinderTest {
    protected Station 교대역;
    protected Station 강남역;
    protected Station 양재역;
    protected Station 부천역;
    protected Station 남부터미널역;
    protected Line 이호선;
    protected Line 신분당선;
    protected Line 삼호선;
    @BeforeEach
    public void setUp() {
        교대역 = new Station(1L, "교대역");
        강남역 = new Station(2L, "강남역");
        양재역 = new Station(3L, "양재역");
        남부터미널역 = new Station(4L, "남부터미널역");

        이호선 = new Line(1L, "2호선", "green", 교대역, 강남역, 10, 2);
        신분당선 = new Line(2L, "신분당선", "red", 강남역, 양재역, 10, 2);
        삼호선 = new Line(3L, "3호선", "orange", 교대역, 남부터미널역, 2, 10);
        삼호선.addSection(남부터미널역, 양재역, 3, 10);

        부천역 = new Station(5L, "부천역");
    }

    protected void verifyPathResponse(final PathResponse actualPathResponse, PathResponse expectedPathResponse) {
        assertThat(actualPathResponse).isEqualTo(expectedPathResponse);
    }
}
