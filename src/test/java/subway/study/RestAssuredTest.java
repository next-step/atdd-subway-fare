package subway.study;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.utils.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class RestAssuredTest extends AcceptanceTest {

    public static final String GOOGLE_BASE_ADDRESS = "https://google.com";

    @DisplayName("구글 페이지 접근 테스트")
    @Test
    @Disabled
    void accessGoogle() {
        ExtractableResponse<Response> response = RestAssured.given().baseUri(GOOGLE_BASE_ADDRESS).port(443).log().all()
                .when().get()
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}