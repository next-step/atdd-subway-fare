package nextstep.subway.documentation;

import static nextstep.subway.documentation.steps.PathDocumentationSteps.경로_조회_요청;
import static nextstep.subway.documentation.steps.PathDocumentationSteps.경로_조회_요청_문서_데이터_생성;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import nextstep.subway.documentation.steps.PathDocumentationSteps.PathInformation;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("경로 조회 API 문서")
public class PathDocumentation extends Documentation {

    @Test
    @DisplayName("[성공] 경로 조회 요청 문서")
    void 경로_조회_요청_문서() {
        // Given
        PathInformation 경로 = 경로_조회_요청_문서_데이터_생성();

        // When
        spec.filter(document("path",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestParameters(
                parameterWithName("source").description("경로 출발역"),
                parameterWithName("target").description("경로 도착역"),
                parameterWithName("type").description("경로 조회 타입")
            ),
            responseFields(
                fieldWithPath("stations").description("경로의 지하철역 리스트"),
                fieldWithPath("stations.[].id").description("아이디"),
                fieldWithPath("stations.[].name").description("이름"),
                fieldWithPath("distance").description("경로의 총 거리"),
                fieldWithPath("duration").description("경로의 총 소요시간")
            )));

        경로_조회_요청(spec, 경로.출발역, 경로.도착역, PathType.DISTANCE);
    }

}