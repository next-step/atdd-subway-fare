package nextstep.station.ui.exception;

import nextstep.common.ErrorCode;
import nextstep.common.ErrorResponse;
import nextstep.common.ExceptionHandlerUtils;
import nextstep.station.domain.exception.CantDeleteStationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StationExceptionHandler {

    @ExceptionHandler(CantDeleteStationException.class)
    public ResponseEntity<ErrorResponse> handleCantDeleteStationException(CantDeleteStationException e) {
        ErrorCode errorCode = StationErrorCode.CANT_DELETE_STATION;
        return ExceptionHandlerUtils.createErrorResponse(errorCode, e);
    }
}
