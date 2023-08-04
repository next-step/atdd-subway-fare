package subway.unit.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.station.domain.Station;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("PathFareDistanceTest (경로 총 거리 운임 계산) 단위 테스트")
public class PathFareDistanceTest {

    /**
     * 1~10  : 1250 -- 1구간..
     * 11~15 : 1350
     * 16~20 : 1450
     * 21~25 : 1550
     * 26~30 : 1660
     * 31~35 : 1750
     * 36~40 : 1850
     * 41~45 : 1950
     * 46~50 : 2050
     * 51~58 : 2150 -- 2구간..
     * 59~.. : 2250
     */

    @BeforeEach
    void beforeEach() {

        Station 강남역 = Station.builder().id(1L).name("강남역").build();
        Station 교대역 = Station.builder().id(2L).name("교대역").build();
        Station 남부터미널역 = Station.builder().id(3L).name("남부터미널역").build();
        Station 양재역 = Station.builder().id(4L).name("양재역").build();
        Station 건대역 = Station.builder().id(5L).name("건대역").build();
        Station 성수역 = Station.builder().id(6L).name("성수역").build();
        Station 왕십리역 = Station.builder().id(7L).name("왕십리역").build();
        Station 강변역 = Station.builder().id(8L).name("강변역").build();
        Station 잠실역 = Station.builder().id(9L).name("잠실역").build();

        Line 이호선 = Line.builder().id(1L).name("2호선").surcharge(100L).color("bg-green-600").build();
        Line 삼호선 = Line.builder().id(2L).name("3호선").surcharge(500L).color("bg-amber-600").build();
        Line 신분당선 = Line.builder().id(3L).name("신분당선").surcharge(1200L).color("bg-hotpink-600").build();
        Line A호선 = Line.builder().id(4L).name("A호선").surcharge(0L).color("bg-grey-600").build();

        Section 강남_교대_구간 = Section.builder().id(1L).line(이호선).distance(10L).duration(5L).upStation(교대역).downStation(강남역).build();
        Section 교대_남부터미널_구간 = Section.builder().id(2L).line(삼호선).distance(2L).duration(3L).upStation(교대역).downStation(남부터미널역).build();
        Section 남터_양재_구간 = Section.builder().id(3L).line(삼호선).distance(3L).duration(15L).upStation(남부터미널역).downStation(양재역).build();
        Section 양재_강남_구간 = Section.builder().id(4L).line(신분당선).distance(10L).duration(5L).upStation(양재역).downStation(강남역).build();
        Section 건대_성수_구간 = Section.builder().id(5L).line(A호선).distance(7L).duration(1L).upStation(건대역).downStation(성수역).build();
        Section 성수_왕십리_구간 = Section.builder().id(6L).line(A호선).distance(3L).duration(4L).upStation(성수역).downStation(왕십리역).build();
        Section 왕십리_강변_구간 = Section.builder().id(7L).line(A호선).distance(17L).duration(8L).upStation(왕십리역).downStation(강변역).build();
        Section 강변_잠실_구간 = Section.builder().id(8L).line(A호선).distance(25L).duration(8L).upStation(강변역).downStation(잠실역).build();

        이호선.addSection(강남_교대_구간);
        삼호선.addSection(교대_남부터미널_구간);
        삼호선.addSection(남터_양재_구간);
        신분당선.addSection(양재_강남_구간);
        A호선.addSection(건대_성수_구간);
        A호선.addSection(성수_왕십리_구간);
        A호선.addSection(왕십리_강변_구간);
        A호선.addSection(강변_잠실_구간);

//        System.out.println(이호선.getName() + " / " + 이호선.getSurcharge() + " / " + 이호선.getStations()); TODO : 제거
//        System.out.println(삼호선.getName() + " / " + 삼호선.getSurcharge() + " / " + 삼호선.getStations());
//        System.out.println(신분당선.getName() + " / " + 신분당선.getSurcharge() + " / " + 신분당선.getStations());
//        System.out.println(A호선.getName() + " / " + A호선.getSurcharge() + " / " + A호선.getStations());

    }

    /**
     * Given 구간을 가진 노선이 있고
     * When 기본 운임을 넘지 않는 경로의 운임을 조회하면
     * Then 초과운임이 발생하지 않는 경로의 계산된 운임이 출력된다.
     */
    @DisplayName("초과운임이 발생하지 않는 경로의 계산된 운임 계산")
    @Test
    void fareByDefaultDistance(){

    }

    /**
     * Given 구간을 가진 노선이 있고
     * When 기본 운임을 넘고 최단거리의 총합이 50 이하인 경로의 운임을 조회하면
     * Then 첫번째 과금 구간의 초과운임이 발생하는 경로의 계산된 운임이 출력된다.
     */
    @DisplayName("첫번째 과금 구간의 초과운임이 발생하는 경로의 계산된 운임 계산")
    @Test
    void fareByFirstSurchargeDistance(){

    }

    /**
     * Given 구간을 가진 노선이 있고
     * When 최단거리의 총합이 50을 초과하는 경로의 운임을 조회하면
     * Then 두번째 과금 구간의 초과운임이 발생하는 경로의 계산된 운임이 출력된다.
     */
    @DisplayName("두번째 과금 구간의 초과운임이 발생하는 경로의 계산된 운임 계산")
    @Test
    void fareBySecondSurchargeDistance(){

    }

    /**
     * Given 구간을 가진 노선이 있고
     * When 최소시간경로와 최단거리경로가 다른 최소시간경로의 운임을 조회하면
     * Then 최단거리경로를 기준으로 하는 경로의 계산된 운임이 출력된다.
     */
    @DisplayName("최단거리경로를 기준으로 하는 경로의 계산된 운임 계산")
    @Test
    void fareByShortestDistance(){

    }
}
