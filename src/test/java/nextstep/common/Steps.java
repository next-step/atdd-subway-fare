package nextstep.common;

import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class Steps {
	public static void 응답_확인(ExtractableResponse<Response> response, HttpStatus status) {
		assertThat(response.statusCode()).isEqualTo(status.value());
	}
}
