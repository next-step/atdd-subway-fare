package nextstep.subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

public class PathSteps {

    private static final String PREFIX_PATH = "/paths";

    public static PathSteps.PatahRequestBuilder 경로_요청을_구성한다() {
        return new PathSteps().new PatahRequestBuilder();
    }

    public class PatahRequestBuilder {
        private RequestSpecification spec;
        private String accessToken;
        private int statusCode = OK.value();

        public PatahRequestBuilder() {
            this.spec = RestAssured.given().log().all();
        }

        public PathSteps.PatahRequestBuilder 로그인을_한다(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public PathSteps.PatahRequestBuilder Response_HTTP_상태_코드(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public ExtractableResponse<Response> 경로_조회_요청을_보낸다(Map<String, ?> queryParams) {
            setAuthorization();
            if (queryParams != null && !queryParams.isEmpty()) {
                queryParams.forEach(spec::queryParam);
            }

            return spec
                    .when().get(PREFIX_PATH)
                    .then().log().all()
                    .statusCode(statusCode)
                    .extract();
        }

        private void setAuthorization() {
            if (this.accessToken != null && !this.accessToken.isEmpty()) {
                this.spec.header(AUTHORIZATION, "Bearer " + this.accessToken);
            }
        }
    }
}
