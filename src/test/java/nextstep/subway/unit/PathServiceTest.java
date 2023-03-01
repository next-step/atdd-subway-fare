package nextstep.subway.unit;

import nextstep.member.domain.LoginMember;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.utils.ApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Transactional
public class PathServiceTest extends ApplicationTest {
    private static final String CHILD_EMAIL = "child6@email.com";
    private static final String TEENAGER_EMAIL = "teenager13@email.com";
    private static final String ADULT_EMAIL = "adult19@email.com";

    private static final int MAX_EXTRA_LINE_FARE = 2000;

    @Autowired
    private PathService pathService;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;


    /**
     * 10km, 5min
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*(2km, 100min)   *신분당선*(10km, 10min)
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     * 3km, 100min
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = stationRepository.save(new Station("교대역"));
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));

        이호선 = lineRepository.save(new Line("2호선", "green", 1200));
        신분당선 = lineRepository.save(new Line("신분당선", "red", MAX_EXTRA_LINE_FARE));
        삼호선 = lineRepository.save(new Line("3호선", "orange", 900));

        이호선.addSection(교대역, 강남역, 10, 5);
        신분당선.addSection(강남역, 양재역, 10, 10);
        삼호선.addSection(교대역, 남부터미널역, 2, 100);
        삼호선.addSection(남부터미널역, 양재역, 3, 100);
    }

    @DisplayName("로그인 한 유저는 최적 경로 조회시 경로 정보와 거리, 노선, 연령 할인 정책을 통해 책정된 요금 정보를 확인할 수 있다.")
    @MethodSource("fareProvider")
    @ParameterizedTest(name = "사용자 유형: {2}, 요금: {1}")
    void findPathWithLogin(String email, int expected, String ageType) {
        final Long id = memberRepository.findByEmail(email).get().getId();
        final LoginMember loginMember = new LoginMember(id, Collections.emptyList());

        final PathResponse pathResponse = pathService.findPath(loginMember, 교대역.getId(), 양재역.getId(), PathType.DURATION);
        final List<Long> 역_아이디_목록 = pathResponse.getStations()
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(역_아이디_목록).containsExactly(교대역.getId(), 강남역.getId(), 양재역.getId());
        assertThat(pathResponse.getDistance()).isEqualTo(20);
        assertThat(pathResponse.getDuration()).isEqualTo(15);
        assertThat(pathResponse.getFare()).isEqualTo(expected);
    }

    private static Stream<Arguments> fareProvider() {
        return Stream.of(
                Arguments.of(CHILD_EMAIL, (int) ((1250 + 200 + MAX_EXTRA_LINE_FARE - 350) * 0.5), "어린이"),
                Arguments.of(TEENAGER_EMAIL, (int) ((1250 + 200 + MAX_EXTRA_LINE_FARE - 350) * 0.8), "청소년"),
                Arguments.of(ADULT_EMAIL, (1250 + 200 + MAX_EXTRA_LINE_FARE), "성인")
        );
    }

    @DisplayName("로그인하지 않은 사용자는 최적 경로 조회시 거리, 노선의 가격 정책에 따른 요금 정보를 확인할 수 있다.")
    @Test
    void findPathWithNoLogin() {
        final LoginMember loginMember = new LoginMember(null, Collections.emptyList());

        final PathResponse pathResponse = pathService.findPath(loginMember, 교대역.getId(), 양재역.getId(), PathType.DURATION);
        final List<Long> 역_아이디_목록 = pathResponse.getStations()
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(역_아이디_목록).containsExactly(교대역.getId(), 강남역.getId(), 양재역.getId());
        assertThat(pathResponse.getDistance()).isEqualTo(20);
        assertThat(pathResponse.getDuration()).isEqualTo(15);
        assertThat(pathResponse.getFare()).isEqualTo((1250 + 200 + MAX_EXTRA_LINE_FARE));
    }
}
