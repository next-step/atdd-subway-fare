package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.LineService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_제거_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class SectionDocumentation extends Documentation {

    private static final String ADD_SECTION = "addSection";
    private static final String DELETE_SECTION = "deleteSection";
    @MockBean
    private LineService lineService;

    @DisplayName("지하철 노선에 구간을 등록")
    @Test
    void addLineSection() {
        doNothing().when(lineService).addSection(anyLong(), any());

        final RequestSpecification requestSpec = documentRequestSpecification()
                .filter(document(ADD_SECTION,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("upStationId").type(JsonFieldType.NUMBER).description("상행역 ID"),
                                fieldWithPath("downStationId").type(JsonFieldType.NUMBER).description("하행역 ID"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"),
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요 시간")
                        )));
        final ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_생성_요청(requestSpec, 1L, 지하철_노선_등록_파라미터(1L, 2L, 7, 5));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선에 구간을 제거")
    @Test
    void removeLineSection() {
        doNothing().when(lineService).deleteSection(anyLong(), anyLong());

        final RequestSpecification requestSpec = documentRequestSpecification()
                .filter(document(DELETE_SECTION,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
        final ExtractableResponse<Response> response = 지하철_노선에_지하철_구간_제거_요청(requestSpec, 1L, 3L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private Map<String, Object> 지하철_노선_등록_파라미터(final long upStationId, final long downStationId, final int distance, final int duration) {
        final Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        params.put("duration", duration);
        return params;
    }
}
