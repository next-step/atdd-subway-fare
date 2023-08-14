package nextstep.subway.documentation;

import nextstep.subway.acceptance.LineSteps;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LineDocumentation extends Documentation {

    @MockBean
    private LineService lineService;

    @Test
    void lineCreate() {
        //given
        LineResponse response = new LineResponse(
                1L,
                "2호선",
                "green",
                List.of(new StationResponse(1L, "노원역"),
                        new StationResponse(2L, "마들역")
                )
        );

        //when
        when(lineService.saveLine(any())).thenReturn(response);

        //then
        Map<String, String> params = LineSteps.노선_생성_요청값_생성("2호선", "green", 1L, 2L, 10, 90, 0);
        LineSteps.지하철_노선_생성_요청(getRequestSpecification("line"), params);
    }

    @Test
    void sectionAdd() {
        //then
        LineSteps.지하철_노선에_지하철_구간_생성_요청(getRequestSpecification("section"),
                1L, LineSteps.구간_생성_요청값_생성(1L, 2L, 10, 90));
    }
}
