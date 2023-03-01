package nextstep.subway.ui;

import nextstep.exception.BadRequestException;
import nextstep.exception.InternalServerException;
import nextstep.exception.NotFoundException;
import nextstep.exception.UnAuthorizedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Void> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<Void> handleInternalServerException(InternalServerException e) {
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<Void> handleUnAuthorizedException(UnAuthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
