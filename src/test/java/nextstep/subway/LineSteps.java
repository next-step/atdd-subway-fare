package nextstep.subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.controller.dto.LineCreateRequest;
import nextstep.subway.controller.dto.SectionCreateRequest;
import org.springframework.http.MediaType;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;

public class LineSteps {

    private static final String PREFIX_PATH = "/lines";

    public static LineSteps.LineRequestBuilder 노선_요청을_구성한다() {
        return new LineSteps().new LineRequestBuilder();
    }

    public class LineRequestBuilder {
        private RequestSpecification spec;
        private String accessToken;
        private LineCreateRequest lineCreateRequest;
        private SectionCreateRequest sectionCreateRequest;
        private int statusCode = OK.value();

        public LineRequestBuilder() {
            this.spec = RestAssured.given().log().all();
        }

        public LineRequestBuilder 로그인을_한다(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public LineRequestBuilder Response_HTTP_상태_코드(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public LineRequestBuilder 노선_생성_정보를_설정한다(LineCreateRequest request) {
            this.lineCreateRequest = request;
            return this;
        }

        public LineRequestBuilder 구간_생성_정보를_설정한다(SectionCreateRequest request) {
            this.sectionCreateRequest = request;
            return this;
        }

        public ExtractableResponse<Response> 노선_생성_요청을_보낸다() {
            setAuthorization();
            return this.spec.contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(this.lineCreateRequest)
                    .when().post(PREFIX_PATH)
                    .then().log().all()
                    .statusCode(statusCode)
                    .extract();
        }

        public ExtractableResponse<Response> 구간_생성_요청을_보낸다(String lineId) {
            setAuthorization();
            return this.spec.contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(this.sectionCreateRequest)
                    .when()
                    .post(PREFIX_PATH + "/{lineId}/sections", lineId)
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
