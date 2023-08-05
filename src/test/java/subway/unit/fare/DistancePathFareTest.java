package subway.unit.fare;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.acceptance.path.PathFixture;
import subway.line.domain.Section;
import subway.path.application.fare.PathFareChain;
import subway.path.application.fare.DistancePathFare;
import subway.path.application.dto.PathFareCalculationInfo;
import subway.station.domain.Station;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("PathFareDistanceTest (경로 총 거리 운임 계산) 단위 테스트")
public class DistancePathFareTest {

    private final static long BASE_FARE = 1250L;
    private PathFareChain pathFare;
    private List<Section> sections;

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
        sections = PathFixture.단위_테스트_인스턴스_생성();
        DistancePathFare distancePathFare = new DistancePathFare();
        pathFare = PathFareChain.chain(distancePathFare);
    }

    /**
     * Given 구간을 가진 노선이 있고
     * When 기본 운임을 넘지 않는 경로의 운임을 조회하면
     * Then 초과운임이 발생하지 않는 경로의 계산된 운임이 출력된다.
     */
    @DisplayName("초과운임이 발생하지 않는 경로의 계산된 운임 계산")
    @Test
    void fareByDefaultDistance() {
        // given
        Station 건대역 = Station.builder().id(5L).name("건대역").build();
        Station 왕십리역 = Station.builder().id(7L).name("왕십리역").build();
        PathFareCalculationInfo calcInfo = PathFareCalculationInfo.builder()
                .fare(BASE_FARE)
                .wholeSections(sections)
                .sourceStation(건대역)
                .targetStation(왕십리역)
                .build();

        // when
        PathFareCalculationInfo calcInfoResponse = pathFare.calculateFare(calcInfo);

        // then
        assertThat(calcInfoResponse.getFare()).isEqualTo(1250L);
    }

    /**
     * Given 구간을 가진 노선이 있고
     * When 기본 운임을 넘고 최단거리의 총합이 50 이하인 경로의 운임을 조회하면
     * Then 첫번째 과금 구간의 초과운임이 발생하는 경로의 계산된 운임이 출력된다.
     */
    @DisplayName("첫번째 과금 구간의 초과운임이 발생하는 경로의 계산된 운임 계산")
    @Test
    void fareByFirstSurchargeDistance() {
        // given
        Station 건대역 = Station.builder().id(5L).name("건대역").build();
        Station 강변역 = Station.builder().id(8L).name("강변역").build();
        PathFareCalculationInfo calcInfo = PathFareCalculationInfo.builder()
                .fare(BASE_FARE)
                .wholeSections(sections)
                .sourceStation(건대역)
                .targetStation(강변역)
                .build();

        // when
        PathFareCalculationInfo calcInfoResponse = pathFare.calculateFare(calcInfo);

        // then
        assertThat(calcInfoResponse.getFare()).isEqualTo(1650L);
    }

    /**
     * Given 구간을 가진 노선이 있고
     * When 최단거리의 총합이 50을 초과하는 경로의 운임을 조회하면
     * Then 두번째 과금 구간의 초과운임이 발생하는 경로의 계산된 운임이 출력된다.
     */
    @DisplayName("두번째 과금 구간의 초과운임이 발생하는 경로의 계산된 운임 계산")
    @Test
    void fareBySecondSurchargeDistance() {
        // given
        Station 건대역 = Station.builder().id(5L).name("건대역").build();
        Station 잠실역 = Station.builder().id(9L).name("잠실역").build();
        PathFareCalculationInfo calcInfo = PathFareCalculationInfo.builder()
                .fare(BASE_FARE)
                .wholeSections(sections)
                .sourceStation(건대역)
                .targetStation(잠실역)
                .build();

        // when
        PathFareCalculationInfo calcInfoResponse = pathFare.calculateFare(calcInfo);

        // then
        assertThat(calcInfoResponse.getFare()).isEqualTo(2150L);
    }

    /**
     * Given 구간을 가진 노선이 있고
     * When 최소시간경로와 최단거리경로가 다른 최소시간경로의 운임을 조회하면
     * Then 최단거리경로를 기준으로 하는 경로의 계산된 운임이 출력된다.
     */
    @DisplayName("최단거리경로를 기준으로 하는 경로의 계산된 운임 계산")
    @Test
    void fareByShortestDistance() {
        // given
        Station 교대역 = Station.builder().id(2L).name("교대역").build();
        Station 양재역 = Station.builder().id(4L).name("양재역").build();
        PathFareCalculationInfo calcInfo = PathFareCalculationInfo.builder()
                .fare(BASE_FARE)
                .wholeSections(sections)
                .sourceStation(교대역)
                .targetStation(양재역)
                .build();

        // when
        PathFareCalculationInfo calcInfoResponse = pathFare.calculateFare(calcInfo);

        // then
        assertThat(calcInfoResponse.getFare()).isEqualTo(1250L);
    }
}
