package nextstep.subway.exception.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import nextstep.subway.exception.SubwayException;
import nextstep.subway.exception.dto.ErrorResponse;

@ControllerAdvice
public class SubwayExceptionHandler {

    @ExceptionHandler(SubwayException.class)
    protected ResponseEntity<ErrorResponse> handleSubwayException(SubwayException e) {
        return ResponseEntity
            .status(e.getStatusCode())
            .body(new ErrorResponse(e.getStatusCode(), e.getMessage()));
    }

}
