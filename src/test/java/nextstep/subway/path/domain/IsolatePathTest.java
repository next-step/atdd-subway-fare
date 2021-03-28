package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

public class IsolatePathTest {
    protected Station 교대역;
    protected Station 강남역;
    protected Station 양재역;
    protected Station 남부터미널역;
    protected Line 이호선;
    protected Line 신분당선;
    protected Line 삼호선;

    protected PathResult 강남역_양재역_경로;
    protected PathResult 양재역_교대역_경로;
    protected PathResult 교대역_남부터미널역_경로;
    protected PathResult 교대역_강남역_경로;
    protected PathResult 남부터미널역_강남역_경로;


    @BeforeEach
    void setup() {
        교대역 =  new Station("교대역");
        ReflectionTestUtils.setField(교대역, "id", 1L);
        강남역 =  new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 2L);
        양재역 =  new Station("양재역");
        ReflectionTestUtils.setField(양재역, "id", 3L);
        남부터미널역 =  new Station("남부터미널역");
        ReflectionTestUtils.setField(남부터미널역, "id", 4L);


        이호선 = new Line("2호선", "green", 0L);
        ReflectionTestUtils.setField(이호선, "id", 1L);
        신분당선 = new Line("신분당선", "green", 900L);
        ReflectionTestUtils.setField(신분당선, "id", 2L);
        삼호선 = new Line("3호선", "green", 500L);
        ReflectionTestUtils.setField(삼호선, "id", 3L);

        이호선.addSection(교대역, 강남역, 60, 70);
        신분당선.addSection(강남역,양재역,7,5);
        삼호선.addSection(교대역,남부터미널역,16,17);
        삼호선.addSection(남부터미널역,양재역,43,30);



        강남역_양재역_경로 = new PathResult(createStations(강남역, 양재역),
                createSections(new Section(신분당선, 강남역, 양재역, 7, 5)));

        양재역_교대역_경로 = new PathResult(createStations(양재역, 남부터미널역, 교대역),
                createSections(new Section(삼호선, 남부터미널역, 양재역, 43,40),
                        new Section(삼호선, 교대역, 남부터미널역, 16, 16)));

        교대역_남부터미널역_경로 = new PathResult(createStations(교대역, 남부터미널역),
                createSections(new Section(삼호선, 교대역, 남부터미널역, 16, 16)));

        교대역_강남역_경로 = new PathResult(createStations(교대역, 강남역),
                createSections(new Section(이호선, 교대역, 강남역, 60, 70)));

        남부터미널역_강남역_경로 = new PathResult(createStations(남부터미널역, 양재역, 강남역),
                createSections(new Section(삼호선, 남부터미널역, 양재역, 43, 30),
                        new Section(신분당선, 양재역, 강남역, 7, 5)));
    }

    private Stations createStations(Station... stations) {
        return new Stations(Arrays.asList(stations));
    }

    private Sections createSections(Section... sections) {
        return new Sections(Arrays.asList(sections));
    }
}
