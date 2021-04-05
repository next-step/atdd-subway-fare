package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.EmptyMember;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PathServiceTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineService lineService;

    @Autowired
    private PathService pathService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    private Station 강남역;
    private Station 역삼역;
    private Station 선릉역;
    private Station 한티역;
    private Station 도곡역;
    private Station 남부터미널역;
    private Station 양재역;
    private Station 교대역;
    private Station 매봉역;
    private Station 신촌역;

    private static final String E_MAIL = "email";
    private static final String PASSWORD = "password";
    private static final int AGE = 15;

    @BeforeEach
    void setUp() {

        databaseCleanup.execute();

        //역 등록
        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));
        선릉역 = stationRepository.save(new Station("선릉역"));
        한티역 = stationRepository.save(new Station("한티역"));
        도곡역 = stationRepository.save(new Station("도곡역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));
        양재역 = stationRepository.save(new Station("양재역"));
        교대역 = stationRepository.save(new Station("교대역"));
        매봉역 = stationRepository.save(new Station("매봉역"));
        신촌역 = stationRepository.save(new Station("신촌역"));

        //노선 등록
        //추가요금 0원
        LineResponse 이호선 = lineService.saveLine(new LineRequest("2호선", "green", 교대역.getId(), 강남역.getId(), 4, 2));
        //추가요금 100원
        LineResponse 삼호선 = lineService.saveLine(new LineRequest("3호선", "orange", 교대역.getId(), 남부터미널역.getId(), 6, 3));
        //추가요금 200원
        LineResponse 분당선 = lineService.saveLine(new LineRequest("분당선", "yellow", 한티역.getId(), 도곡역.getId(), 4, 2));
        //추가요금 300원
        LineResponse 신분당선 = lineService.saveLine(new LineRequest("신분당선", "red", 강남역.getId(), 양재역.getId(), 6, 3));

        //섹션 등록
        lineService.addSection(이호선.getId(), new SectionRequest(강남역.getId(), 역삼역.getId(), 4, 2));
        lineService.addSection(이호선.getId(), new SectionRequest(역삼역.getId(), 선릉역.getId(), 4, 2));
        lineService.addSection(분당선.getId(), new SectionRequest(선릉역.getId(), 한티역.getId(), 4, 2));
        lineService.addSection(삼호선.getId(), new SectionRequest(남부터미널역.getId(), 양재역.getId(), 6, 3));
        lineService.addSection(삼호선.getId(), new SectionRequest(양재역.getId(), 매봉역.getId(), 4, 2));
        lineService.addSection(삼호선.getId(), new SectionRequest(매봉역.getId(), 도곡역.getId(), 4, 2));
        lineService.addSection(이호선.getId(), new SectionRequest(신촌역.getId(), 교대역.getId(), 50, 25));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        //when
        PathResponse pathResponse = pathService.findPath(new EmptyMember(), 강남역.getId(), 양재역.getId());

        //then
        assertThat(pathResponse.getTotalFare()).isEqualTo(1550); //총 6km, 기본요금 1250원 + 노선추가요금 300원
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다(10km 이상)")
    @Test
    void findPathByDistanceWithDistanceOver10Km() {
        //when
        PathResponse pathResponse = pathService.findPath(new EmptyMember(), 강남역.getId(), 도곡역.getId());

        //then
        assertThat(pathResponse.getTotalFare()).isEqualTo(1650); //총 14km, 기본요금 1250원 + 노선추가요금 300원 + 거리추가요금 100원
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다(50km 이상)")
    @Test
    void findPathByDistanceWithDistanceOver50Km() {
        //when
        PathResponse pathResponse = pathService.findPath(new EmptyMember(), 신촌역.getId(), 양재역.getId());

        //then
        assertThat(pathResponse.getTotalFare()).isEqualTo(2550); //총 60km, 기본요금 1250원 + 노선추가요금 300원 + 거리추가요금 1000원
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다(청소년 + 50km)")
    @Test
    void findPathByDurationWithDistanceOver50KmAndTeen() {
        //given
        MemberResponse memberResponse = memberService.createMember(new MemberRequest(E_MAIL, PASSWORD, AGE));
        LoginMember loginMember = new LoginMember(memberResponse.getId(), E_MAIL, PASSWORD, AGE);
        //when
        PathResponse pathResponse = pathService.findPath(loginMember, 신촌역.getId(), 양재역.getId());

        //then
        //총 60km, (기본요금 1250원 + 노선추가요금 300원 + 거리추가요금 1000원 - 공제액 350원)*(1- 할인율 0.2)
        assertThat(pathResponse.getTotalFare()).isEqualTo(1760);
    }
}
