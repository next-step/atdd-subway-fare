package nextstep.subway.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.RestAssuredRequestBuilder;
import nextstep.subway.controller.dto.StationCreateRequest;
import org.springframework.http.MediaType;

import static org.springframework.http.HttpStatus.OK;

public class StationSteps {

    private static final String PREFIX_PATH = "/stations";

    public static StationSteps.StationRequestBuilder 지하철_요청을_구성한다() {
        return new StationSteps().new StationRequestBuilder();
    }

    public class StationRequestBuilder extends RestAssuredRequestBuilder {
        private StationCreateRequest body;
        private int statusCode = OK.value();

        public StationRequestBuilder() {
            this.spec = RestAssured.given().log().all();
        }

        public StationRequestBuilder 로그인을_한다(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public StationRequestBuilder Response_HTTP_상태_코드(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public StationRequestBuilder 지하철_생성_정보를_설정한다(String stationName) {
            this.body = new StationCreateRequest(stationName);
            return this;
        }

        public ExtractableResponse<Response> 지하철_생성_요청을_보낸다() {
            setAuthorization();
            return this.spec.contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(this.body)
                    .when().post(PREFIX_PATH)
                    .then().log().all()
                    .statusCode(statusCode)
                    .extract();
        }

    }
}
