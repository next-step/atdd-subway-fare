package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SubwayMapTest {

    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;

    @BeforeEach
    public void setUp() {
        이호선 = new Line();
        신분당선 = new Line();
        삼호선 = new Line();

        교대역 = new Station();
        강남역 = new Station();
        양재역 = new Station();
        남부터미널역 = new Station();

        이호선.addSection(교대역, 강남역, 10, 5);
        신분당선.addSection(강남역, 양재역, 10, 5);
        삼호선.addSection(교대역, 남부터미널역, 2, 5);
        삼호선.addSection(남부터미널역, 양재역, 3, 6);
    }

    @Test
    void findPathByDistance() {
        //given
        SubwayMap subwayMap = new SubwayMap(List.of(이호선, 신분당선, 삼호선), SubwayMap.Type.DISTANCE);

        //when
        Path path = subwayMap.findPath(교대역, 양재역);

        //then
        Assertions.assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
    }

    @Test
    void findPathByDuration() {
        //given
        SubwayMap subwayMap = new SubwayMap(List.of(이호선, 신분당선, 삼호선), SubwayMap.Type.DURATION);

        //when
        Path path = subwayMap.findPath(교대역, 양재역);

        //then
        Assertions.assertThat(path.getStations()).containsExactly(교대역, 강남역, 양재역);
    }
}
