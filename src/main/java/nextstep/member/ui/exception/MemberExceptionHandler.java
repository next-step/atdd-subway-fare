package nextstep.member.ui.exception;

import nextstep.common.ErrorCode;
import nextstep.common.ErrorResponse;
import nextstep.common.ExceptionHandlerUtils;
import nextstep.member.domain.exception.DuplicateEmailException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmailException(DuplicateEmailException e) {
        ErrorCode errorCode = MemberErrorCode.DUPLICATE_EMAIL;
        return ExceptionHandlerUtils.createErrorResponse(errorCode, e);
    }
}
