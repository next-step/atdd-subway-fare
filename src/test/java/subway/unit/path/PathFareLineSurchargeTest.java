package subway.unit.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.acceptance.path.PathFixture;
import subway.line.domain.Section;
import subway.path.application.PathFareDistance;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("PathFareLineSurcharge (노선 기본 운임 계산) 단위 테스트")
public class PathFareLineSurchargeTest {


    private final static long BASE_FARE = 1250L;
    private PathFareDistance pathFareDistance;
    private List<Section> sections;

    /**
     * 2호선 : 100
     * 3호선 : 500
     * 신분당선 : 1200
     * A호선 : 0
     */

    @BeforeEach
    void beforeEach() {
//        Station 강남역 = Station.builder().id(1L).name("강남역").build();
//        Station 교대역 = Station.builder().id(2L).name("교대역").build();
//        Station 남부터미널역 = Station.builder().id(3L).name("남부터미널역").build();
//        Station 양재역 = Station.builder().id(4L).name("양재역").build();
//        Station 건대역 = Station.builder().id(5L).name("건대역").build();
//        Station 성수역 = Station.builder().id(6L).name("성수역").build();
//        Station 왕십리역 = Station.builder().id(7L).name("왕십리역").build();
//        Station 강변역 = Station.builder().id(8L).name("강변역").build();
//        Station 잠실역 = Station.builder().id(9L).name("잠실역").build();
//
//        Line 이호선 = Line.builder().id(1L).name("2호선").surcharge(100L).color("bg-green-600").build();
//        Line 삼호선 = Line.builder().id(2L).name("3호선").surcharge(500L).color("bg-amber-600").build();
//        Line 신분당선 = Line.builder().id(3L).name("신분당선").surcharge(1200L).color("bg-hotpink-600").build();
//        Line A호선 = Line.builder().id(4L).name("A호선").surcharge(0L).color("bg-grey-600").build();
//
//        Section 강남_교대_구간 = Section.builder().id(1L).line(이호선).distance(10L).duration(5L).upStation(교대역).downStation(강남역).build();
//        Section 교대_남부터미널_구간 = Section.builder().id(2L).line(삼호선).distance(2L).duration(3L).upStation(교대역).downStation(남부터미널역).build();
//        Section 남터_양재_구간 = Section.builder().id(3L).line(삼호선).distance(3L).duration(15L).upStation(남부터미널역).downStation(양재역).build();
//        Section 양재_강남_구간 = Section.builder().id(4L).line(신분당선).distance(10L).duration(5L).upStation(양재역).downStation(강남역).build();
//        Section 건대_성수_구간 = Section.builder().id(5L).line(A호선).distance(7L).duration(1L).upStation(건대역).downStation(성수역).build();
//        Section 성수_왕십리_구간 = Section.builder().id(6L).line(A호선).distance(3L).duration(4L).upStation(성수역).downStation(왕십리역).build();
//        Section 왕십리_강변_구간 = Section.builder().id(7L).line(A호선).distance(17L).duration(8L).upStation(왕십리역).downStation(강변역).build();
//        Section 강변_잠실_구간 = Section.builder().id(8L).line(A호선).distance(25L).duration(8L).upStation(강변역).downStation(잠실역).build();
//
//        이호선.addSection(강남_교대_구간);
//        삼호선.addSection(교대_남부터미널_구간);
//        삼호선.addSection(남터_양재_구간);
//        신분당선.addSection(양재_강남_구간);
//        A호선.addSection(건대_성수_구간);
//        A호선.addSection(성수_왕십리_구간);
//        A호선.addSection(왕십리_강변_구간);
//        A호선.addSection(강변_잠실_구간);

        sections = PathFixture.단위_테스트_인스턴스_생성();
        pathFareDistance = new PathFareDistance();
    }

    /**
     * Given 구간을 가진 노선이 있고
     * When 한 노선을 지나는 경로를 조회 했을 때
     * Then 해당 노선의 추가요금이 계산된 운임이 출력된다.
     */
    @DisplayName("노선의 추가요금 운임 계산")
    @Test
    void fareByLineSurcharge() {

    }

    /**
     * Given 구간을 가진 노선이 있고
     * When 여러 노선을 지나는 경로를 조회 했을 때
     * Then 가장 높은 기본요금을 가진 노선의 추가요금이 계산된 운임이 출력된다.
     */
    @DisplayName("가장 높은 기본요금을 가진 노선의 추가요금 운임 계산")
    @Test
    void fareByMaximumLineSurcharge() {

    }


}
