package nextstep.subway.acceptance;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathSearchType;

public class PathSteps {

    public static ExtractableResponse<Response> 지하철_경로_조회(RequestSpecification spec, Long source, Long target, PathSearchType pathSearchType) {
        return RestAssured
            .given(spec).log().all()
            .filter(document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", source)
            .queryParam("target", target)
            .queryParam("type", pathSearchType)
            .when().get("/paths")
            .then().log().all().extract();
    }
}
