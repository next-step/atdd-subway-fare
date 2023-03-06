package nextstep.member.domain.exception;

import lombok.extern.slf4j.Slf4j;
import nextstep.common.exception.CommonErrorResponse;
import nextstep.common.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity<CommonErrorResponse> handleNotFoundMemberException(NotFoundMemberException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        loggingMemberException(errorCode);

        return ResponseEntity.status(exception.getHttpStatus())
                .body(CommonErrorResponse.of(errorCode));
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<CommonErrorResponse> handleMemberException(MemberException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        loggingMemberException(errorCode);

        return ResponseEntity.status(exception.getHttpStatus())
                .body(CommonErrorResponse.of(errorCode));
    }

    private void loggingMemberException(ErrorCode errorCode) {
        log.info("회원 예외 발생 이유: {} 코드: {}", errorCode.getMessage(), errorCode.getCode());
    }
}
