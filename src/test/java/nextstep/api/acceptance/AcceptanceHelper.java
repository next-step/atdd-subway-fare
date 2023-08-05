package nextstep.api.acceptance;

import java.util.List;

import org.springframework.http.HttpStatus;

import io.restassured.response.ValidatableResponse;
import nextstep.api.ExceptionResponse;

public class AcceptanceHelper {

    public static ExceptionResponse asExceptionResponse(final ValidatableResponse response) {
        return response.extract().as(ExceptionResponse.class);
    }

    public static <T> T asResponse(final ValidatableResponse response, final Class<T> clazz) {
        return response.extract().as(clazz);
    }

    public static <T> List<T> asResponses(final ValidatableResponse response, final Class<T> clazz) {
        return response.extract()
                .jsonPath()
                .getList("", clazz);
    }

    public static void statusCodeShouldBe(final ValidatableResponse response, final HttpStatus expected) {
        response.statusCode(expected.value());
    }
}
