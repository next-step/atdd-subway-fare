package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import support.auth.userdetails.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PathServiceMockTest {

    private Station 교대역;
    private Station 강남역;
    private Station 남부터미널역;
    private Station 양재역;
    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;

    @InjectMocks
    private PathService pathService;

    @Mock
    private StationService stationService;

    @Mock
    private LineService lineService;

    /**
     * 교대역   --- *2호선*(5) ---   강남역
     * |                            |
     * *3호선(3)*                 *신분당선*(5)
     * |                            |
     * 남부터미널역 --- *3호선*(2) --- 양재역
     */
    @BeforeEach
    public void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        남부터미널역 = new Station("남부터미널역");
        양재역 = new Station("양재역");

        신분당선 = new Line("신분당선", "red");
        신분당선.addSection(강남역, 양재역, 5, 10);
        이호선 = new Line("이호선", "green");
        이호선.addSection(교대역, 강남역, 5, 10);
        삼호선 = new Line("삼호선", "orange");
        삼호선.addSection(교대역, 양재역, 5, 3);
        삼호선.addSection(남부터미널역, 양재역, 2, 2);
    }

    @Test
    void 최단_경로를_탐색한다() {
        // given
        given(stationService.findById(1L)).willReturn(교대역);
        given(stationService.findById(4L)).willReturn(양재역);
        given(lineService.findLines()).willReturn(List.of(이호선, 삼호선, 신분당선));
        PathRequest pathRequest = new PathRequest(1L, 4L, PathType.DISTANCE);

        // when
        PathResponse pathResponse = pathService.findPath(pathRequest, 20);

        // then
        assertAll(
                () -> assertThat(pathResponse.getStations()).hasSize(3),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(5),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1_250)
        );
    }

    @Test
    void 최소_시간_경로를_탐색한다() {
        // given
        given(stationService.findById(1L)).willReturn(교대역);
        given(stationService.findById(4L)).willReturn(양재역);
        given(lineService.findLines()).willReturn(List.of(이호선, 삼호선, 신분당선));
        PathRequest pathRequest = new PathRequest(1L, 4L, PathType.DURATION);

        // when
        PathResponse pathResponse = pathService.findPath(pathRequest, 20);

        // then
        assertAll(
                () -> assertThat(pathResponse.getStations()).hasSize(3),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(5),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1_250)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1250",
            "6, 450",
            "13, 720",
            "20, 1250"
    })
    void 나이에_따른_요금을_계산한다(int age, int result) {
        // given
        given(stationService.findById(1L)).willReturn(교대역);
        given(stationService.findById(4L)).willReturn(양재역);
        given(lineService.findLines()).willReturn(List.of(이호선, 삼호선, 신분당선));
        PathRequest pathRequest = new PathRequest(1L, 4L, PathType.DURATION);

        // when
        PathResponse pathResponse = pathService.findPath(pathRequest, age);

        // then
        assertThat(pathResponse.getFare()).isEqualTo(result);
    }

    @Test
    void 출발역과_도착역이_같은_경우_예외를_반환한다() {
        // given
        PathRequest pathRequest = new PathRequest(1L, 1L, PathType.DISTANCE);

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->
                pathService.findPath(pathRequest, 10)
        );
    }

    @Test
    void 존재하지_않는_출발역이나_도착역을_조회한_경우_예외를_발생시킨다() {
        // given
        final Station 신논현역 = new Station("신논현역");
        given(stationService.findById(5L)).willReturn(신논현역);
        given(stationService.findById(3L)).willReturn(강남역);
        given(lineService.findLines()).willReturn(List.of(이호선, 삼호선, 신분당선));
        PathRequest pathRequest = new PathRequest(5L, 3L, PathType.DISTANCE);


        // when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                pathService.findPath(pathRequest, 20)
        );
    }
}