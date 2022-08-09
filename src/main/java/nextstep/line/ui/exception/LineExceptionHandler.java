package nextstep.line.ui.exception;

import nextstep.common.ErrorCode;
import nextstep.common.ErrorResponse;
import nextstep.common.ExceptionHandlerUtils;
import nextstep.line.domain.exception.IllegalSectionOperationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LineExceptionHandler {

    @ExceptionHandler(IllegalSectionOperationException.class)
    public ResponseEntity<ErrorResponse> handleIllegalSectionOperationException(IllegalSectionOperationException e) {
        ErrorCode errorCode = LineErrorCode.ILLEGAL_SECTION_OPERATION;
        return ExceptionHandlerUtils.createErrorResponse(errorCode, e);
    }
}
