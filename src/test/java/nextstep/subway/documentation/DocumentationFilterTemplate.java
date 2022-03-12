package nextstep.subway.documentation;

import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public final class DocumentationFilterTemplate {

    private static final String PATH_ID = "path";
    private static final String STATION_SAVE_ID = "stationsSave";
    private static final String STATION_LIST_ID = "stationsList";
    private static final String STATION_DELETE_ID = "stationsDelete";
    private static final String LINE_SAVE_ID = "lineSave";

    public static RestDocumentationFilter 경로_조회_템플릿() {
        return document(PATH_ID,
                requestParameters(
                        parameterWithName("source").description("출발역"),
                        parameterWithName("target").description("도착역"),
                        parameterWithName("pathType").description("경로 조회 유형")
                ),
                responseFields(
                        fieldWithPath("stations").description("역 목록"),
                        fieldWithPath("stations[].id").description("역 고유번호"),
                        fieldWithPath("stations[].name").description("역 이름"),
                        fieldWithPath("stations[].createdDate").description("생성 시간"),
                        fieldWithPath("stations[].modifiedDate").description("최근 수정 시간"),
                        fieldWithPath("distance").description("이동 거리"),
                        fieldWithPath("duration").description("소요 시간"),
                        fieldWithPath("fare").description("운임")
                )
        );
    }

    public static RestDocumentationFilter 역_등록_템플릿() {
        return document(STATION_SAVE_ID,
                responseFields(
                        fieldWithPath("id").description("역 고유번호"),
                        fieldWithPath("name").description("역 이름"),
                        fieldWithPath("createdDate").description("생성 시간"),
                        fieldWithPath("modifiedDate").description("최근 수정 시간")
                )
        );
    }

    public static RestDocumentationFilter 역_목록_템플릿() {
        return document(STATION_LIST_ID,
                responseFields(
                        fieldWithPath("[].id").description("역 고유번호"),
                        fieldWithPath("[].name").description("역 이름"),
                        fieldWithPath("[].createdDate").description("생성 시간"),
                        fieldWithPath("[].modifiedDate").description("최근 수정 시간")
                )
        );
    }

    public static RestDocumentationFilter 역_삭제_템플릿() {
        return document(STATION_DELETE_ID);
    }

    public static RestDocumentationFilter 노선_등록_템플릿() {
        return document(LINE_SAVE_ID,
                requestFields(
                        fieldWithPath("name").description("노선 이름"),
                        fieldWithPath("color").description("노선 색상"),
                        fieldWithPath("upStationId").description("상행역 고유 번호"),
                        fieldWithPath("downStationId").description("하행역 고유 번호"),
                        fieldWithPath("distance").description("이동 거리"),
                        fieldWithPath("duration").description("소요 시간"),
                        fieldWithPath("extraCharge").description("추가 요금")
                ),
                responseFields(
                        fieldWithPath("id").description("노선 고유번호"),
                        fieldWithPath("name").description("노선 이름"),
                        fieldWithPath("color").description("노선 색상"),
                        fieldWithPath("extraCharge").description("추가 요금"),
                        fieldWithPath("stations[].id").description("역 고유번호"),
                        fieldWithPath("stations[].name").description("역 이름"),
                        fieldWithPath("stations[].createdDate").description("생성 시간"),
                        fieldWithPath("stations[].modifiedDate").description("최근 수정 시간"),
                        fieldWithPath("createdDate").description("생성 시간"),
                        fieldWithPath("modifiedDate").description("최근 수정 시간")
                )
        );
    }
}
