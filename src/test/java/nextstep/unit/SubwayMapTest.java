package nextstep.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

import java.util.Arrays;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathWeight;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.farepolicy.Fare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SubwayMapTest {
  private Station 교대역;
  private Station 강남역;
  private Station 양재역;
  private Station 남부터미널역;
  private Station 고속터미널역;
  private Station 신사역;
  private Station 서울역;
  private Station 부산역;

  private Line 이호선;
  private Line 삼호선;
  private Line 사호선;
  private Line 신분당선;
  private Line 오호선;
  /**
   * 교대역    --- *2호선* ---   강남역
   * |                        |
   * *3호선*                   *신분당선*
   * |                        |
   * 남부터미널역  --- *3호선* --- 양재역
   *
   *
   * 고속 터미널역 --- *4호선* --- 신사역
   */
  @BeforeEach
  public void setup() {
    교대역 = new Station("교대역");
    강남역 = new Station("강남역");
    양재역 = new Station("양재역");
    남부터미널역 = new Station("남부터미널역");
    고속터미널역 = new Station("고속터미널역");
    신사역 = new Station("신사역");

    서울역 = new Station("서울역");
    부산역 = new Station("부산역");

    이호선 = new Line("2호선", "green");
    신분당선 = new Line("2호선", "green");
    삼호선 = new Line("3호선", "orange");
    사호선 = new Line("4호선", "blue");
    오호선 = new Line("5호선", "true");


    이호선.addSection(교대역, 강남역, 12,2);
    신분당선.addSection(강남역, 양재역, 10,3);
    삼호선.addSection(교대역, 남부터미널역, 2,4);
    사호선.addSection(고속터미널역, 신사역, 2,5);
    오호선.addSection(서울역, 부산역, 2,6);
    삼호선.addSection(남부터미널역, 양재역, 3, 12);
  }

  @DisplayName("제일 짧은 경로를 조회합니다.")
  @Test
  void getPath() {
    //When
    SubwayMap finder = new SubwayMap(Arrays.asList(이호선,신분당선,삼호선,사호선,삼호선), PathWeight.DISTANCE);
    //Then
    Path path = finder.findPath(교대역, 양재역);
    Fare fare = Fare.of(path);
    assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
    assertThat(path.extractDistance()).isEqualTo(5L);
    assertThat(path.extractDuration()).isEqualTo(16L);
    assertThat(fare.calculateFare(0)).isEqualTo(1250);
  }


  @DisplayName("제일 짧은 경로의 종합요금을 조회합니다.")
  @Test
  void getPathOverTen() {
    //When
    SubwayMap finder = new SubwayMap(Arrays.asList(이호선,신분당선,삼호선,사호선,삼호선));
    //Then
    Path path = finder.findPath(교대역, 강남역);
    Fare fare = Fare.of(path);
    assertThat(path.getStations()).containsExactly(교대역, 강남역);

    assertThat(path.extractDistance()).isEqualTo(12L);
    assertThat(path.extractDuration()).isEqualTo(2L);
    assertThat(fare.calculateFare(0)).isEqualTo(1350);
  }
  @DisplayName("오류케이스: 출발역과 도착역이 연결되지 않았을 때, 제일 짧은 경로 조회가 실패합니다")
  @Test
  void getPathThrowsNotConnectedError() {
    //When
    SubwayMap finder = new SubwayMap(Arrays.asList(이호선,신분당선,삼호선,사호선,삼호선,오호선));
    //Then
    Throwable thrown = catchThrowable(() -> finder.findPath(서울역, 교대역));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    assertThat(thrown.getMessage()).isEqualTo("연결되어 있지 않은 구간입니다.");
  }

  @DisplayName("오류케이스: 출발역과 도착역이 연결되지 않았을 때, 제일 짧은 경로 조회가 실패합니다")
  @Test
  void calculatePathFare() {
    //When
    SubwayMap finder = new SubwayMap(Arrays.asList(이호선,신분당선,삼호선,사호선,삼호선,오호선));
    //Then
    Throwable thrown = catchThrowable(() -> finder.findPath(서울역, 교대역));

    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    assertThat(thrown.getMessage()).isEqualTo("연결되어 있지 않은 구간입니다.");
  }
}
