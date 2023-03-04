package nextstep.subway.domain.exception;

import lombok.extern.slf4j.Slf4j;
import nextstep.common.exception.CommonErrorResponse;
import nextstep.common.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SubwayExceptionHandler {

    @ExceptionHandler(NotFoundLineException.class)
    public ResponseEntity<CommonErrorResponse> handleNotFoundLineException(NotFoundLineException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        loggingSubwayException(errorCode);

        return ResponseEntity.badRequest()
                .body(CommonErrorResponse.of(errorCode));
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<CommonErrorResponse> handleSubwayException(SubwayException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        loggingSubwayException(errorCode);

        return ResponseEntity.badRequest()
                .body(CommonErrorResponse.of(errorCode));
    }

    private void loggingSubwayException(ErrorCode errorCode) {
        log.info("노선 예외 발생 이유: {}, 코드: {}", errorCode.getMessage(), errorCode.getCode());
    }
}
