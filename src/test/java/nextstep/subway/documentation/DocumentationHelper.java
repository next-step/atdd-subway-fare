package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public final class DocumentationHelper {

    public static void 경로_조회_요청(RequestSpecification spec, PathType pathType) {
        RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역"),
                                parameterWithName("target").description("도착역"),
                                parameterWithName("pathType").description("경로 조회 유형")
                        )
                ))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("pathType", pathType)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
