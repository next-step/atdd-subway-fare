package nextstep.subway.documentation;

import static nextstep.subway.documentation.PathDocumentationSteps.PathInformation;
import static nextstep.subway.documentation.PathDocumentationSteps.경로_조회_요청_문서_데이터_생성;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class PathDocumentation extends Documentation {

    @Test
    @DisplayName("[성공] 경로를 조회 요청 문서")
    void 경로_조회_요청_문서() {
        PathInformation 경로_정보 = 경로_조회_요청_문서_데이터_생성();
        Long 교대역 = 경로_정보.출발역;
        Long 양재역 = 경로_정보.도착역;

        RestAssured
            .given(spec).log().all()
            .filter(document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 교대역)
            .queryParam("target", 양재역)
            .when().get("/paths")
            .then().log().all().extract();
    }

}