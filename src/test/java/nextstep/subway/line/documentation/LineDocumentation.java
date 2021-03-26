package nextstep.subway.line.documentation;

import io.restassured.RestAssured;
import nextstep.subway.Documentation;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_생성_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class LineDocumentation extends Documentation {

    @MockBean
    private LineService lineService;

    @Test
    @DisplayName("지하철 노선 등록")
    void addLine() {
        LineResponse lineResponse = new LineResponse(
            1L,
            "신분당선",
            "red",
            Arrays.asList(
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                new StationResponse(2L, "광교역", LocalDateTime.now(), LocalDateTime.now())
            ),
            900,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(lineService.saveLine(any(LineRequest.class))).thenReturn(lineResponse);


        지하철_노선_생성_요청(
            RestAssured.given(spec).log().all().filter(
                document(
                    "lines",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())
                )
            ),
            new LineRequest(
                lineResponse.getName(),
                lineResponse.getColor(),
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()).getId(),
                new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now()).getId(),
                10,
                10,
                900
            )
        );

    }
}
