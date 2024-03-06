package nextstep.subway.applicaion;


import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.PathSearchType;
import nextstep.subway.domain.vo.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.ui.BusinessException;
import nextstep.utils.FixtureUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

  @Mock
  private SectionService sectionService;
  @Mock
  private StationService stationService;
  @Mock
  private MemberService memberService;

  @InjectMocks
  private PathService pathService;

  Line 신분당선;
  Station 강남역;
  Station 역삼역;
  Station 서면역;
  Station 남포역;
  List<Section> 구간_목록;
  Path 경로;
  Member 멤버;

  @BeforeEach
  public void setUp() {
    신분당선 = FixtureUtil.getBuilder(Line.class)
        .set("name", "신분당선")
        .set("color", "빨강")
        .set("sections", null)
        .sample();
    강남역 = FixtureUtil.getFixture(Station.class);
    역삼역 = FixtureUtil.getFixture(Station.class);
    서면역 = FixtureUtil.getFixture(Station.class);
    남포역 = FixtureUtil.getFixture(Station.class);
    구간_목록 = List.of(
        구간을_생성(신분당선, 강남역, 역삼역, 10, 10),
        구간을_생성(신분당선, 서면역, 남포역, 5, 5)
    );
    경로 = FixtureUtil.getFixture(Path.class);
    멤버 = FixtureUtil.getBuilder(Member.class)
        .set(javaGetter(Member::getAge), 20)
        .sample();
  }

  @DisplayName("경로 탐색 성공")
  @Test
  void 경로_탐색_성공() {
    // given
    when(stationService.getStation(강남역.getId())).thenReturn(Optional.of(강남역));
    when(stationService.getStation(역삼역.getId())).thenReturn(Optional.of(역삼역));
    when(sectionService.findAll()).thenReturn(구간_목록);
    when(memberService.findMemberByEmail(멤버.getEmail())).thenReturn(Optional.of(멤버));

    // when
    final var result = pathService.findPath(강남역.getId(), 역삼역.getId(), PathSearchType.DISTANCE, 멤버.getEmail());

    // then
    assertThat(
        result.getStations().stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList())
    ).containsExactly(강남역.getId(), 역삼역.getId());
    assertThat(result.getDistance()).isEqualTo(10);
  }

  @DisplayName("연결되지 않는 경로")
  @Test
  void 연결되지_않는_경로() {
    // given
    when(stationService.getStation(강남역.getId())).thenReturn(Optional.of(강남역));
    when(stationService.getStation(서면역.getId())).thenReturn(Optional.of(서면역));
    when(sectionService.findAll()).thenReturn(구간_목록);

    // when
    final var throwable = catchThrowable(() -> pathService.findPath(강남역.getId(), 서면역.getId(), PathSearchType.DISTANCE, 멤버.getEmail()));

    // then
    assertThat(throwable).isInstanceOf(BusinessException.class)
        .hasMessageContaining("경로를 찾을 수 없습니다.");
  }

  @DisplayName("등록되지 않은 출발역")
  @Test
  void 등록되지_않은_출발역() {
    // given
    when(stationService.getStation(강남역.getId())).thenReturn(Optional.empty());

    // when
    final var throwable = catchThrowable(() -> pathService.findPath(강남역.getId(), 역삼역.getId(), PathSearchType.DISTANCE, 멤버.getEmail()));

    // then
    assertThat(throwable).isInstanceOf(BusinessException.class)
        .hasMessageContaining("출발역 정보를 찾을 수 없습니다.");
  }

  @DisplayName("등록되지 않은 도착역")
  @Test
  void 등록되지_않은_도착역() {
    // given
    when(stationService.getStation(강남역.getId())).thenReturn(Optional.of(강남역));
    when(stationService.getStation(역삼역.getId())).thenReturn(Optional.empty());

    // when
    final var throwable = catchThrowable(() -> pathService.findPath(강남역.getId(), 역삼역.getId(), PathSearchType.DISTANCE, 멤버.getEmail()));

    // then
    assertThat(throwable).isInstanceOf(BusinessException.class)
        .hasMessageContaining("도착역 정보를 찾을 수 없습니다.");
  }

  @DisplayName("출발역과 도착역이 같음")
  @Test
  void 출발역과_도착역이_같음() {


    // when
    final var throwable = catchThrowable(() -> pathService.findPath(강남역.getId(), 강남역.getId(), PathSearchType.DISTANCE, 멤버.getEmail()));

    // then
    assertThat(throwable).isInstanceOf(BusinessException.class)
        .hasMessageContaining( "출발역과 도착역이 같습니다.");
  }

  private static Section 구간을_생성(Line line, Station upStation, Station downStation, int distance, int duration) {
    return new Section(line, upStation, downStation, distance, duration);
  }
}