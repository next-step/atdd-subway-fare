package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.jayway.jsonpath.PathNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.entity.Line;
import nextstep.subway.line.domain.entity.LineSection;
import nextstep.subway.line.domain.entity.LineSections;
import nextstep.subway.path.application.PathFinder;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.application.dto.PathResponse;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PathServiceMockTest {

    @Mock
    private PathFinder pathFinder;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private PathService pathService;

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private List<Line> lines;
    private List<LineSection> sections;
    private Long sourceId;
    private Long targetId;
    private String email = "test@example.com";
    private String password = "password";
    private Member member;
    private LoginMember childLoginMember;


    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @BeforeEach
    void setUp() {
        교대역 = new Station(1L, "교대역");
        강남역 = new Station(2L, "강남역");
        양재역 = new Station(3L, "양재역");
        남부터미널역 = new Station(4L, "남부터미널역");

        Line 이호선 = new Line("이호선", "bg-red-600", new LineSections(), 0L);
        Line 신분당선 = new Line("신분당선", "bg-green-600", new LineSections(), 0L);
        Line 삼호선 = new Line("삼호선", "bg-orange-600", new LineSections(), 0L);

        LineSection 교대역강남역 = new LineSection(이호선, 교대역, 강남역, 10L, 2L);
        LineSection 강남역양재역 = new LineSection(신분당선, 강남역, 양재역, 10L, 2L);
        LineSection 교대역남부터미널역 = new LineSection(삼호선, 교대역, 남부터미널역, 2L, 2L);
        LineSection 남부터미널역양재역 = new LineSection(삼호선, 남부터미널역, 양재역, 10L, 2L);

        이호선.addSection(교대역강남역);
        신분당선.addSection(강남역양재역);
        삼호선.addSection(교대역남부터미널역);
        삼호선.addSection(남부터미널역양재역);

        lines = Arrays.asList(이호선, 신분당선, 삼호선);
        sections = Arrays.asList(교대역남부터미널역, 남부터미널역양재역);

        sourceId = 1L;
        targetId = 2L;

        member = new Member(email, password, 30);
        childLoginMember = new LoginMember(email, 20);
    }

    @Test
    @DisplayName("유효한 출발역과 도착역 ID가 주어지면 최단 경로를 반환한다")
    void it_returns_shortest_path() {
        // given
        when(stationRepository.findByIdOrThrow(sourceId)).thenReturn(교대역);
        when(stationRepository.findByIdOrThrow(targetId)).thenReturn(양재역);
        when(lineRepository.findAll()).thenReturn(lines);
        when(pathFinder.find(lines, 교대역, 양재역, PathType.DISTANCE)).thenReturn(sections);

        // when
        PathResponse actualPathResponse = pathService.findShortestPath(sourceId, targetId, PathType.DISTANCE,
            Optional.of(childLoginMember.getAge()));

        // then
        assertThat(actualPathResponse.getDistance()).isEqualTo(12L);
        assertThat(actualPathResponse.getDuration()).isEqualTo(4L);
        assertThat(actualPathResponse.getFare()).isEqualTo(1350L);
    }

    @Test
    @DisplayName("청소년 사용자의 경우 할인된 요금이 적용된다")
    void teenager_user_pays_discounted_fare() {
        // given
        LoginMember teenagerLoginMember = new LoginMember(email, 15);

        when(stationRepository.findByIdOrThrow(sourceId)).thenReturn(교대역);
        when(stationRepository.findByIdOrThrow(targetId)).thenReturn(양재역);
        when(lineRepository.findAll()).thenReturn(lines);
        when(pathFinder.find(lines, 교대역, 양재역, PathType.DISTANCE)).thenReturn(sections);

        // when
        PathResponse actualPathResponse = pathService.findShortestPath(sourceId, targetId, PathType.DISTANCE,
            Optional.of(teenagerLoginMember.getAge()));

        // then
        assertThat(actualPathResponse.getFare()).isEqualTo(800);
    }

    @Test
    @DisplayName("어린이 사용자의 경우 더 많이 할인된 요금이 적용된다")
    void child_user_pays_more_discounted_fare() {
        // given
        LoginMember childLoginMember = new LoginMember(email, 10);

        when(stationRepository.findByIdOrThrow(sourceId)).thenReturn(교대역);
        when(stationRepository.findByIdOrThrow(targetId)).thenReturn(양재역);
        when(lineRepository.findAll()).thenReturn(lines);
        when(pathFinder.find(lines, 교대역, 양재역, PathType.DISTANCE)).thenReturn(sections);

        // when
        PathResponse actualPathResponse = pathService.findShortestPath(sourceId, targetId, PathType.DISTANCE,
            Optional.of(childLoginMember.getAge()));

        // then
        assertThat(actualPathResponse.getFare()).isEqualTo(500);
    }

    @Test
    @DisplayName("존재하지 않는 출발역 ID가 주어지면 StationNotFoundException을 던진다")
    void it_throws_StationNotFoundException1() {
        // given
        Long nonExistentSourceId = 999L;

        when(stationRepository.findByIdOrThrow(nonExistentSourceId))
            .thenThrow(new StationNotFoundException(nonExistentSourceId));

        // when, then
        assertThatThrownBy(() -> pathService.findShortestPath(nonExistentSourceId, targetId, PathType.DISTANCE,
                Optional.of(childLoginMember.getAge())))
            .isInstanceOf(StationNotFoundException.class)
            .hasMessageContaining(String.valueOf(nonExistentSourceId));
    }

    @Test
    @DisplayName("존재하지 않는 도착역 ID가 주어지면 StationNotFoundException을 던진다")
    void it_throws_StationNotFoundException2() {
        // given
        Long nonExistentTargetId = 999L;

        when(stationRepository.findByIdOrThrow(sourceId)).thenReturn(교대역);
        when(stationRepository.findByIdOrThrow(nonExistentTargetId))
            .thenThrow(new StationNotFoundException(nonExistentTargetId));

        // when, then
        assertThatThrownBy(() -> pathService.findShortestPath(sourceId, nonExistentTargetId, PathType.DISTANCE,
                Optional.of(childLoginMember.getAge())))
            .isInstanceOf(StationNotFoundException.class)
            .hasMessageContaining(String.valueOf(nonExistentTargetId));
    }

    @Test
    @DisplayName("출발역과 도착역 사이에 경로가 없으면 PathNotFoundException을 던진다")
    void it_throws_PathNotFoundException_when_no_path_between_stations() {
        // given
        Station disconnectedStation = new Station("공덕역");
        Long disconnectedStation_id = 5L;

        when(stationRepository.findByIdOrThrow(sourceId)).thenReturn(교대역);
        when(stationRepository.findByIdOrThrow(disconnectedStation_id)).thenReturn(
            disconnectedStation);
        when(lineRepository.findAll()).thenReturn(lines);
        when(pathFinder.find(lines, 교대역, disconnectedStation, PathType.DISTANCE))
            .thenThrow(new PathNotFoundException());

        // when, then
        assertThatThrownBy(() -> pathService.findShortestPath(sourceId, disconnectedStation_id, PathType.DISTANCE,
                Optional.of(childLoginMember.getAge())))
            .isInstanceOf(PathNotFoundException.class);
    }
}
