package nextstep.subway.station.acceptance;

import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import static nextstep.subway.utils.AcceptanceTest.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class StationDocumentSteps {

    private static final String DOCUMENT_IDENTIFIER_STATION = "station/{method-name}";

    public static RequestSpecification 지하철_역_생성_문서화_요청() {
        return givenAndCreateDocumentForFields(DOCUMENT_IDENTIFIER_STATION, getStationDocumentCreateStationRequestBody(), getStationDocumentCreateStationResponseBody());
    }

    private static RequestFieldsSnippet getStationDocumentCreateStationRequestBody() {
        return requestFields(
                fieldWithPath("name").type(JsonFieldType.STRING).description("역 이름")
        );
    }

    private static ResponseFieldsSnippet getStationDocumentCreateStationResponseBody() {
        return responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("역 ID"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("역 이름"),
                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("역 등록 날짜"),
                fieldWithPath("modifiedDate").type(JsonFieldType.STRING).description("역 최종수정 날짜")
        );
    }

    public static RequestSpecification 지하철_역_목록_조회_문서화_요청() {
        return givenAndCreateDocumentForEmptyRequest(DOCUMENT_IDENTIFIER_STATION);
    }

    public static RequestSpecification 지하철_역_제거_문서화_요청() {
        return givenAndCreateDocumentForEmptyRequest(DOCUMENT_IDENTIFIER_STATION);
    }
}
