package nextstep.subway.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;

import static nextstep.subway.documentation.DocumentationFilterTemplate.*;

public final class DocumentationHelper {

    public static void 경로_조회_요청(RequestSpecification spec, PathType pathType) {
        RestAssured
                .given(spec).log().all()
                .filter(경로_조회_템플릿())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("pathType", pathType)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
