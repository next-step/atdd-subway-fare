package nextstep.subway.station.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.station.dto.StationRequest;
import org.springframework.http.MediaType;

import static nextstep.subway.utils.BaseDocumentSteps.givenDefault;

public class StationRequestSteps {

    public static final String 강남역 = "강남역";
    public static final String 역삼역 = "역삼역";

    public static ExtractableResponse<Response> 지하철_역_등록_됨(String name) {
        return 지하철_역_생성_요청(givenDefault(), name);
    }

    public static ExtractableResponse<Response> 지하철_역_생성_요청(RequestSpecification given, String name) {
        return given
                .body(new StationRequest(name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_역_목록_조회_요청(RequestSpecification given) {
        return given
                .when()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get("/stations")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_역_제거_요청(RequestSpecification given, String uri) {
        return given
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }
}
