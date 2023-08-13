package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class LineDocumentation extends Documentation {


    @MockBean
    private LineService lineService;

    @Test
    void createLine() {

        // given
        RequestSpecification requestSpecification = RestAssured.given(spec).log().all()
                .filter(document("line",
                                 preprocessRequest(prettyPrint()),
                                 preprocessResponse(prettyPrint()),
                                 requestFields(
                                         fieldWithPath("name").description("노선 이름"),
                                         fieldWithPath("color").description("노선 색깔"),
                                         fieldWithPath("distance").description("거리"),
                                         fieldWithPath("duration").description("소요시간"),
                                         fieldWithPath("additionalFare").description("추가요금")))
                );

        when(lineService.saveLine(any(LineRequest.class))).thenReturn(new LineResponse(1L, "강남역", "red", 900, List.of()));

        지하철_노선_생성_요청(requestSpecification, "강남역", "red", 5, 6, 900);
    }
}
