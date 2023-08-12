package nextstep.api.documentation.subway;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static nextstep.api.acceptance.subway.line.LineSteps.지하철노선_단일조회_요청;
import static nextstep.api.acceptance.subway.line.LineSteps.지하철노선_생성_요청;
import static nextstep.api.acceptance.subway.line.LineSteps.지하철노선_수정_요청;
import static nextstep.api.acceptance.subway.line.LineSteps.지하철노선_전체조회_요청;
import static nextstep.api.acceptance.subway.line.LineSteps.지하철노선_제거_요청;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import nextstep.api.documentation.Documentation;
import nextstep.api.subway.applicaion.line.LineService;
import nextstep.api.subway.applicaion.line.dto.request.LineCreateRequest;
import nextstep.api.subway.applicaion.line.dto.request.LineUpdateRequest;
import nextstep.api.subway.applicaion.line.dto.response.LineResponse;
import nextstep.api.subway.applicaion.station.dto.StationResponse;

class LineDocumentation extends Documentation {
    @MockBean
    private LineService lineService;

    private final String name = "신분당선";
    private final String color = "bg-red-600";
    private final Long upStationId = 1L;
    private final Long downStationId = 2L;
    private final LineResponse response = new LineResponse(
            1L, name, color, List.of(
            new StationResponse(upStationId, "강남역"),
            new StationResponse(downStationId, "삼성역")
    ));

    @Test
    void showLines() {
        when(lineService.findAllLines()).thenReturn(List.of(response));

        지하철노선_전체조회_요청(makeRequestSpec("line-show"));
    }

    @Test
    void findLine() {
        when(lineService.findLine(anyLong())).thenReturn(response);

        지하철노선_단일조회_요청(1L, makeRequestSpec("line-find"));
    }

    @Test
    void createLine() {
        when(lineService.saveLine(any())).thenReturn(response);

        지하철노선_생성_요청(new LineCreateRequest(name, color, upStationId, downStationId, 10, 10), makeRequestSpec("line-create"));
    }

    @Test
    void updateLine() {
        doNothing().when(lineService).updateLine(anyLong(), any());

        지하철노선_수정_요청(1L, new LineUpdateRequest(name, color), makeRequestSpec("line-update"));
    }

    @Test
    void deleteLine() {
        doNothing().when(lineService).deleteLine(anyLong());

        지하철노선_제거_요청(1L, makeRequestSpec("line-delete"));
    }
}
