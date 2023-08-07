package nextstep.subway.applicaion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
class LineServiceTest {

    @InjectMocks
    LineService lineService;

    @Mock
    LineRepository lineRepository;

    @Mock
    StationService stationService;

    @Test
    void saveLine() {
        // given
        LineRequest lineRequest = getMockLineRequest("신분당선", "red", 15, 1L, 2L, 42 * 60);
        Line line = new Line("신분당선", "red");
        when(lineRepository.save(any())).thenReturn(line);
        Station sinsaStation = new Station("신사역");
        when(stationService.findById(1L)).thenReturn(sinsaStation);
        Station gwanggyoStation = new Station("광교역");
        when(stationService.findById(2L)).thenReturn(gwanggyoStation);

        // when
        LineResponse lineResponse = lineService.saveLine(lineRequest);

        // then
        Assertions.assertAll(
                () -> assertThat(lineResponse.getName()).isEqualTo("신분당선"),
                () -> assertThat(lineResponse.getColor()).isEqualTo("red"),
                () -> assertThat(lineResponse.getName()).isEqualTo("신분당선"),
                () -> assertThat(lineResponse.getStations()).usingRecursiveComparison().isEqualTo(
                        List.of(StationResponse.of(sinsaStation), StationResponse.of(gwanggyoStation)))
        );
    }

    private static LineRequest getMockLineRequest(String name, String color, int distance, long upStationId,
            long downStationId, int duration) {
        LineRequest lineRequest = mock(LineRequest.class);
        when(lineRequest.getName()).thenReturn(name);
        when(lineRequest.getColor()).thenReturn(color);
        when(lineRequest.getDistance()).thenReturn(distance);
        when(lineRequest.getUpStationId()).thenReturn(upStationId);
        when(lineRequest.getDownStationId()).thenReturn(downStationId);
        when(lineRequest.getDuration()).thenReturn(duration);
        return lineRequest;
    }

    //         var 신사역_아이디 = 지하철역_생성_요청("신사역").jsonPath().getLong("id");
    //        var 광교역_아이디 = 지하철역_생성_요청("광교역").jsonPath().getLong("id");
    //        int 신사역에서_광교역까지의_거리 = 15;
    //        int 신사역에서_광교역까지_걸리는_시간 = 42 * 60;

    //    @Transactional
    //    public LineResponse saveLine(LineRequest request) {
    //        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
    //        if (request.getUpStationId() != null && request.getDownStationId() != null && request.getDistance() != 0) {
    //            Station upStation = stationService.findById(request.getUpStationId());
    //            Station downStation = stationService.findById(request.getDownStationId());
    //            line.addSection(upStation, downStation, request.getDistance());
    //        }
    //        return LineResponse.of(line);
    //    }
}
