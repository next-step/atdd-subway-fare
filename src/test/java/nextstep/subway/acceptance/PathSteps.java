package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_타입에따른_경로_조회를_요청(Long source, Long target, String type) {

        return 두_역의_경로_조회를_요청_docs(source, target, type, new RequestSpecBuilder().build());
    }

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청_docs(Long source, Long target,
        String type, RequestSpecification spec) {

        return RestAssured
            .given(spec).log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_타입에따른_경로_조회를_요청(Long source, Long target, String type, String accessToken) {

        return 두_역의_경로_조회를_요청_docs(source, target, type, accessToken, new RequestSpecBuilder().build());
    }

    public static ExtractableResponse<Response> 두_역의_경로_조회를_요청_docs(Long source, Long target,
        String type, String accessToken, RequestSpecification spec) {

        return RestAssured
            .given(spec).log().all()
            .auth().oauth2(accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
            .then().log().all().extract();
    }

}
