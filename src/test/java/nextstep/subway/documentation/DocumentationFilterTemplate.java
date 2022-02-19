package nextstep.subway.documentation;

import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public final class DocumentationFilterTemplate {

    private static final String PATH_IDENTIFIER = "path";

    public static RestDocumentationFilter 경로_조회_템플릿() {
        return document(PATH_IDENTIFIER,
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
                        fieldWithPath("duration").description("소요 시간")
                )
        );
    }
}
