package nextstep.api.documentation.subway;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static nextstep.api.acceptance.subway.line.SectionSteps.지하철구간_등록_요청;
import static nextstep.api.acceptance.subway.line.SectionSteps.지하철구간_제거_요청;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import nextstep.api.documentation.Documentation;
import nextstep.api.subway.applicaion.line.LineService;
import nextstep.api.subway.applicaion.line.dto.request.SectionRequest;
import nextstep.api.subway.applicaion.line.dto.response.LineResponse;
import nextstep.api.subway.applicaion.station.dto.StationResponse;

class SectionDocumentation extends Documentation {
    @MockBean
    private LineService lineService;

    private final Long upStationId = 1L;
    private final Long downStationId = 2L;
    private final LineResponse response = new LineResponse(
            1L, "신분당선", "bg-red-600", List.of(
            new StationResponse(upStationId, "강남역"),
            new StationResponse(downStationId, "삼성역")
    ));

    @Test
    void appendSection() {
        when(lineService.appendSection(anyLong(), any())).thenReturn(response);

        지하철구간_등록_요청(1L, new SectionRequest(upStationId, downStationId, 10, 10), makeRequestSpec("section-create"));
    }

    @Test
    void removeSection() {
        doNothing().when(lineService).removeSection(anyLong(), anyLong());

        지하철구간_제거_요청(1L, downStationId, makeRequestSpec("section-delete"));
    }
}
