package nextstep.subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import nextstep.auth.principal.UserPrincipal;
import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.application.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
class PathServiceTest {

    @InjectMocks
    PathService pathService;
    @Mock
    LineService lineService;
    @Mock
    private StationService stationService;

    Station gyodaeStation;
    Station gangnameStation;
    Station yangjaeStation;
    Station southTerminalStation;
    Line no2Line;
    Line shinbundantLine;
    Line no3Line;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        gyodaeStation = new Station("교대역");
        gangnameStation = new Station("강남역");
        yangjaeStation = new Station("양재역");
        southTerminalStation = new Station("남부터미널역");

        no2Line = new Line("2호선", "green");
        no2Line.addSection(gyodaeStation, gangnameStation, 10, 10);

        shinbundantLine = new Line("신분당선", "red");
        shinbundantLine.addSection(gangnameStation, yangjaeStation, 10, 5);

        no3Line = new Line("3호선", "orange");
        no3Line.addSection(gyodaeStation, southTerminalStation, 2, 20);
        no3Line.addSection(southTerminalStation, yangjaeStation, 3, 2);
    }

    @DisplayName("최단 거리의 경로를 찾는다")
    @Test
    void findPathByDistance() {
        // given
        when(stationService.findById(1L)).thenReturn(gyodaeStation);
        when(stationService.findById(2L)).thenReturn(yangjaeStation);
        when(lineService.findLines()).thenReturn(
                List.of(no2Line, shinbundantLine, no3Line)
        );

        // when
        PathResponse pathResponse = pathService.findPath(1L, 2L, "DISTANCE", UserPrincipal.createAnonymous());

        // then
        Assertions.assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(5),
                () -> assertThat(pathResponse.getStations()).usingRecursiveComparison()
                        .isEqualTo(
                                List.of(StationResponse.of(gyodaeStation),
                                        StationResponse.of(southTerminalStation),
                                        StationResponse.of(yangjaeStation))
                        )
        );
    }
}
