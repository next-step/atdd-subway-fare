package nextstep.subway.line.acceptance.documentation;

import io.restassured.specification.RequestSpecification;
import nextstep.subway.utils.BaseDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public class LineDocumentation extends BaseDocumentation {

    public LineDocumentation(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public PathParametersSnippet initDocumentRequestPathVariable() {
        return pathParameters();
    }

    @Override
    public RequestParametersSnippet initDocumentRequestParameters() {
        return requestParameters();
    }

    @Override
    public RequestFieldsSnippet initDocumentRequestBody() {
        return requestFields(
                fieldWithPath("name").type(JsonFieldType.STRING).description("노선 이름"),
                fieldWithPath("color").type(JsonFieldType.STRING).description("노선 색상"),
                fieldWithPath("upStationId").type(JsonFieldType.NUMBER).description("노선 상행역 ID"),
                fieldWithPath("downStationId").type(JsonFieldType.NUMBER).description("노선 하행역 ID"),
                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("노선 거리"),
                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("노선 소요시간"),
                fieldWithPath("extraCharge").type(JsonFieldType.NUMBER).description("노선 추가요금")
        );
    }

    @Override
    public ResponseFieldsSnippet initDocumentResponseBody() {
        return initDocumentCommonResponseBody();
    }

    public static ResponseFieldsSnippet initDocumentCommonResponseBody() {
        return responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("노선 ID"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("노선 이름"),
                fieldWithPath("color").type(JsonFieldType.STRING).description("노선 색상"),
                fieldWithPath("extraCharge").type(JsonFieldType.NUMBER).description("노선 추가요금"),
                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("노선에 포함된 역 목록"),
                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("노선에 포함된 역 ID"),
                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("노선에 포함된 역 이름"),
                fieldWithPath("stations[].createdDate").type(JsonFieldType.STRING).description("노선에 포함된 역 등록 날짜"),
                fieldWithPath("stations[].modifiedDate").type(JsonFieldType.STRING).description("노선에 포함된 역 최종수정 날짜"),
                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("노선 등록 날짜"),
                fieldWithPath("modifiedDate").type(JsonFieldType.STRING).description("노선 최종수정 날짜")
        );
    }
}
