package nextstep.subway.unit;

import com.google.common.collect.Lists;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.fare.Fare;
import nextstep.subway.domain.fare.FareCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static nextstep.subway.unit.model.SectionBuilder.createSection;
import static org.assertj.core.api.Assertions.assertThat;

class SubwayMapTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 정자역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private static final int ADULT_AGE = 20;

    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");
        정자역 = createStation(5L, "정자역");

        신분당선 = new Line("신분당선", "red", 900);
        이호선 = new Line("2호선", "red");
        삼호선 = new Line("3호선", "red", 500);

        신분당선.addSection(() -> createSection(신분당선, 강남역, 양재역, 11111, 11111));
        신분당선.addSection(() -> createSection(신분당선, 양재역, 정자역, 12, 12));
        이호선.addSection(() -> createSection(이호선, 교대역, 강남역, 3, 3));
        삼호선.addSection(() -> createSection(삼호선, 교대역, 남부터미널역, 54, 54));
        삼호선.addSection(() -> createSection(삼호선, 남부터미널역, 양재역, 5, 5));
    }

    @EnumSource(PathType.class)
    @ParameterizedTest
    void findPath(PathType pathType) {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, pathType);

        // when
        Path path = subwayMap.findPath(교대역, 정자역);
        FareCalculator fareCalculator = new FareCalculator(path, ADULT_AGE);
        Fare fare = fareCalculator.getFare();

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 남부터미널역, 양재역, 정자역));
        assertThat(path.extractDistance()).isEqualTo(71);
        assertThat(path.extractDuration()).isEqualTo(71);
        추가_요금이_가장_비싼_노선에_대해서만_운임에_추가된다(fare.getFare());
    }

    @EnumSource(PathType.class)
    @ParameterizedTest
    void findPathOppositely(PathType pathType) {
        // given
        List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines, pathType);

        // when
        Path path = subwayMap.findPath(양재역, 교대역);
        FareCalculator fareCalculator = new FareCalculator(path, ADULT_AGE);
        Fare fare = fareCalculator.getFare();

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 남부터미널역, 교대역));
        assertThat(path.extractDistance()).isEqualTo(59);
        assertThat(path.extractDuration()).isEqualTo(59);
        assertThat(fare.getFare()).isEqualTo(2450);
    }

    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }

    private void 추가_요금이_가장_비싼_노선에_대해서만_운임에_추가된다(int fare) {
        assertThat(fare).isEqualTo(2950);
    }
}
