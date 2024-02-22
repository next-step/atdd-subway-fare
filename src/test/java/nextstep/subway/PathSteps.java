package nextstep.subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

public class PathSteps {

    private static final String PREFIX_PATH = "/paths";

    public static PathSteps.PatahRequestBuilder 경로_요청을_구성한다() {
        return new PathSteps().new PatahRequestBuilder();
    }

    public class PatahRequestBuilder {
        private RequestSpecification spec;
        private int statusCode = OK.value();

        public PatahRequestBuilder() {
            this.spec = RestAssured.given().log().all();
        }

        public PathSteps.PatahRequestBuilder Response_HTTP_상태_코드(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }
        public ExtractableResponse<Response> 경로_조회_요청을_보낸다(Map<String, ?> queryParams) {
            RequestSpecification requestSpecification = RestAssured.given().log().all();

            if (queryParams != null && !queryParams.isEmpty()) {
                queryParams.forEach(requestSpecification::queryParam);
            }

            return requestSpecification
                    .when().get(PREFIX_PATH)
                    .then().log().all()
                    .statusCode(statusCode)
                    .extract();
        }
    }
}
