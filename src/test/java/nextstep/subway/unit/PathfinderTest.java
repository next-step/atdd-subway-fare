package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.DistancePathFinder;
import nextstep.subway.domain.path.DurationPathFinder;
import nextstep.subway.domain.path.Path;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.fixture.LineFixture.삼호선;
import static nextstep.subway.fixture.LineFixture.신분당선;
import static nextstep.subway.fixture.LineFixture.이호선;
import static nextstep.subway.fixture.SectionFixture.강남_양재_구간;
import static nextstep.subway.fixture.SectionFixture.교대_강남_구간;
import static nextstep.subway.fixture.SectionFixture.교대_남부터미널_구간;
import static nextstep.subway.fixture.SectionFixture.남부터미널_양재_구간;
import static nextstep.subway.fixture.StationFixture.강남역;
import static nextstep.subway.fixture.StationFixture.교대역;
import static nextstep.subway.fixture.StationFixture.남부터미널역;
import static nextstep.subway.fixture.StationFixture.양재역;
import static nextstep.subway.unit.support.LineSupporter.역이_순서대로_정렬되어_있다;


@DisplayName("지하철 경로 조회 기능 단위 테스트")
class PathfinderTest {

    private Station 교대_역;
    private Station 강남_역;
    private Station 양재_역;
    private Station 남부터미널_역;
    private Line 신분당선라인;
    private Line 이호선라인;
    private Line 삼호선라인;

    @BeforeEach
    void setUp() {

        교대_역 = 교대역.엔티티_생성(1L);
        강남_역 = 강남역.엔티티_생성(2L);
        양재_역 = 양재역.엔티티_생성(3L);
        남부터미널_역 = 남부터미널역.엔티티_생성(4L);

        신분당선라인 = 신분당선.엔티티_생성();
        이호선라인 = 이호선.엔티티_생성();
        삼호선라인 = 삼호선.엔티티_생성();

        신분당선라인.addSection(강남_역, 양재_역, 강남_양재_구간.구간_거리(), 강남_양재_구간.구간_소요시간());
        이호선라인.addSection(교대_역, 강남_역, 교대_강남_구간.구간_거리(), 교대_강남_구간.구간_소요시간());
        삼호선라인.addSection(교대_역, 남부터미널_역, 교대_남부터미널_구간.구간_거리(), 교대_남부터미널_구간.구간_소요시간());
        삼호선라인.addSection(남부터미널_역, 양재_역, 남부터미널_양재_구간.구간_거리(), 남부터미널_양재_구간.구간_소요시간());
    }

    @DisplayName("구간 거리 기준으로 최단 경로를 조회한다")
    @Test
    void findPathWithDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선라인, 이호선라인, 삼호선라인);
        DistancePathFinder pathFinderWithDistance = new DistancePathFinder(lines);

        // when
        Path 최단_경로 = pathFinderWithDistance.findPath(교대_역, 양재_역);

        // then
        역이_순서대로_정렬되어_있다(최단_경로, 교대_역, 남부터미널_역, 양재_역);
    }

    @DisplayName("(reverse) 구간 거리 기준으로 최단 경로를 조회한다")
    @Test
    void findPathOppositelyWithDistance() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선라인, 이호선라인, 삼호선라인);
        DistancePathFinder pathFinderWithDuration = new DistancePathFinder(lines);

        // when
        Path 최단_경로 = pathFinderWithDuration.findPath(양재_역, 교대_역);

        // then
        역이_순서대로_정렬되어_있다(최단_경로, 양재_역, 남부터미널_역, 교대_역);
    }


    @DisplayName("구간 소요시간 기준으로 최단 경로를 조회한다")
    @Test
    void findPathWithDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선라인, 이호선라인, 삼호선라인);
        DurationPathFinder pathFinderWithDuration = new DurationPathFinder(lines);

        // when
        Path 최단_경로 = pathFinderWithDuration.findPath(교대_역, 양재_역);

        // then
        역이_순서대로_정렬되어_있다(최단_경로, 교대_역, 강남_역, 양재_역);
    }

    @DisplayName("(reverse) 구간 소요시간 기준으로 최단 경로를 조회한다")
    @Test
    void findPathOppositelyWithDuration() {
        // given
        List<Line> lines = Lists.newArrayList(신분당선라인, 이호선라인, 삼호선라인);
        DurationPathFinder pathFinderWithDuration = new DurationPathFinder(lines);

        // when
        Path 최단_경로 = pathFinderWithDuration.findPath(양재_역, 교대_역);

        // then
        역이_순서대로_정렬되어_있다(최단_경로, 양재_역, 강남_역, 교대_역);
    }
}
