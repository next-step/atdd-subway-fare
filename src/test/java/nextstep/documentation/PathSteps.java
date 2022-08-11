package nextstep.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.path.domain.PathSearchType;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 최소_거리_경로를_조회한다(RequestSpecification spec, Long source, Long target) {
        return spec
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", PathSearchType.DISTANCE.name())
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 최소_시간_경로를_조회한다(RequestSpecification spec, Long source, Long target) {
        return spec
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", PathSearchType.DURATION.name())
                .when().get("/paths")
                .then().log().all().extract();
    }
}
