package nextstep.common;

import org.springframework.http.ResponseEntity;

public class ExceptionHandlerUtils {

    private ExceptionHandlerUtils() {
    }

    public static ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode, Exception e) {
        ErrorResponse body = new ErrorResponse(errorCode.status(), errorCode.code(), e.getMessage());
        return ResponseEntity.status(errorCode.status()).body(body);
    }
}
