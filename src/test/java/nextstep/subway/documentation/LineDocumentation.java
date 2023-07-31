package nextstep.subway.documentation;

import nextstep.subway.acceptance.LineSteps;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LineDocumentation extends Documentation {

    @MockBean
    private LineService lineService;

    @Test
    void lineCreate() {
        //given
        LineResponse response = new LineResponse(1L, "2호선", "green", 90, List.of());

        //when
        when(lineService.saveLine(any())).thenReturn(response);

        //then
        LineSteps.지하철_노선_생성_요청(getRequestSpecification("line"), "2호선", "green");
    }
}
