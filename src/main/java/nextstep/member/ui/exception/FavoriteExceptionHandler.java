package nextstep.member.ui.exception;

import nextstep.common.ErrorCode;
import nextstep.common.ErrorResponse;
import nextstep.common.ExceptionHandlerUtils;
import nextstep.member.domain.exception.DuplicateFavoriteException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FavoriteExceptionHandler {

    @ExceptionHandler(DuplicateFavoriteException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateFavoriteException(DuplicateFavoriteException e) {
        ErrorCode errorCode = FavoriteErrorCode.DUPLICATE_FAVORITE;
        return ExceptionHandlerUtils.createErrorResponse(errorCode, e);
    }
}
