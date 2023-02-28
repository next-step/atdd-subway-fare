package nextstep.subway.unit;

import nextstep.common.exception.NoPathConnectedException;
import nextstep.common.exception.NoRegisterStationException;
import nextstep.common.exception.SameStationException;
import nextstep.member.domain.LoginMember;
import nextstep.subway.acceptance.ApplicationContextTest;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.common.error.SubwayError.NO_FIND_SAME_SOURCE_TARGET_STATION;
import static nextstep.common.error.SubwayError.NO_PATH_CONNECTED;
import static nextstep.common.error.SubwayError.NO_REGISTER_LINE_STATION;
import static nextstep.subway.acceptance.LineSteps.추가요금;
import static nextstep.subway.domain.SearchType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("최소 경로 찾기의 대한 테스트")
@Transactional
class PathServiceTest extends ApplicationContextTest {

    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private PathService pathService;

    private LoginMember 비로그인_사용자;
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 부평역;
    private Station 검암역;
    private Station 몽촌토성역;

    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;
    private Line 일호선;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {

        비로그인_사용자 = LoginMember.GUEST;
        교대역 = createStation("교대역");
        강남역 = createStation("강남역");
        양재역 = createStation("양재역");
        남부터미널역 = createStation("남부터미널역");
        부평역 = createStation("부평역");
        검암역 = createStation("검암역");
        몽촌토성역 = createStation("몽촌토성역");

        이호선 = createLine("2호선", "green", 추가요금(100));
        이호선.addSection(교대역, 강남역, 10, 20);
        신분당선 = createLine("신분당선", "red", 추가요금(300));
        신분당선.addSection(강남역, 양재역, 10, 20);
        삼호선 = createLine("3호선", "orange", 추가요금(500));
        삼호선.addSection(교대역, 남부터미널역, 2, 4);
        삼호선.addSection(남부터미널역, 양재역, 3, 6);
        일호선 = createLine("1호선", "blue", 추가요금(700));
        일호선.addSection(부평역, 검암역, 5, 10);
    }

    @DisplayName("로그인 사용자의 최적의 경로 중 추가 요금이 가장 큰 금액으로 요금을 계산한다.")
    @ParameterizedTest(name = "최적의 경로 중 추가요금을 포함한 총 1750원 금액의 {0} 세는 {1} 원의 요금을 낸다.")
    @CsvSource(value = {"6,1050", "12,1050", "13,1470", "18,1470", "19,1750", "20,1750"})
    void find_route_extra_fare_by_login(final int age, final String fare) {

        final LoginMember 로그인_사용자 = new LoginMember(1L, "email@email.com", age);

        final PathResponse pathResponse = pathService.findPath(로그인_사용자, 교대역.getId(), 양재역.getId(), DISTANCE.name());

        final BigDecimal 운임금액 = new BigDecimal(fare);
        final List<String> stationNames = pathResponse.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(pathResponse.getStations()).hasSize(3),
                () -> assertThat(stationNames).containsExactly("교대역", "남부터미널역", "양재역"),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(5),
                () -> assertThat(pathResponse.getDuration()).isEqualTo(10),
                () -> assertThat(pathResponse.getFare()).isEqualTo(운임금액)
        );
    }

    @DisplayName("비로그인 사용자의 최적의 경로를 찾고 요금도 계산한다.")
    @Test
    void find_route_by_logout() {

        final PathResponse pathResponse = pathService.findPath(비로그인_사용자, 교대역.getId(), 양재역.getId(), DISTANCE.name());

        final BigDecimal 운임금액 = new BigDecimal(1750);
        final List<String> stationNames = pathResponse.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(pathResponse.getStations()).hasSize(3),
                () -> assertThat(stationNames).containsExactly("교대역", "남부터미널역", "양재역"),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(5),
                () -> assertThat(pathResponse.getDuration()).isEqualTo(10),
                () -> assertThat(pathResponse.getFare()).isEqualTo(운임금액)
        );
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않아서 조회가 불가능합니다.")
    @Test
    void error_notConnectedSourceNTarget() {

        final LoginMember 로그인_사용자 = new LoginMember(1L, "email@email.com", 10);

        assertThatThrownBy(() -> pathService.findPath(로그인_사용자, 교대역.getId(), 검암역.getId(), DISTANCE.name()))
                .isInstanceOf(NoPathConnectedException.class)
                .hasMessage(NO_PATH_CONNECTED.getMessage());
    }

    @DisplayName("출발역과 도착역이 같아서 조회가 불가능합니다.")
    @Test
    void error_sameSourceNTarget() {

        final LoginMember 로그인_사용자 = new LoginMember(1L, "email@email.com", 10);

        assertThatThrownBy(() -> pathService.findPath(로그인_사용자, 교대역.getId(), 교대역.getId(), DISTANCE.name()))
                .isInstanceOf(SameStationException.class)
                .hasMessage(NO_FIND_SAME_SOURCE_TARGET_STATION.getMessage());
    }

    @DisplayName("요청한 역이 노선의 등록되어 있지 않아서 조회가 불가능합니다.")
    @Test
    void error_noRegisterStation() {

        final LoginMember 로그인_사용자 = new LoginMember(1L, "email@email.com", 10);

        assertThatThrownBy(() -> pathService.findPath(로그인_사용자, 교대역.getId(), 몽촌토성역.getId(), DISTANCE.name()))
                .isInstanceOf(NoRegisterStationException.class)
                .hasMessage(NO_REGISTER_LINE_STATION.getMessage());
    }

    private Station createStation(final String station) {
        return stationRepository.save(new Station(station));
    }

    private Line createLine(final String name, final String color, final BigDecimal fare) {
        return lineRepository.save(new Line(name, color, fare));
    }
}
