package nextstep.common;

import nextstep.common.exception.EmailInputException;
import nextstep.common.exception.MissingTokenException;
import nextstep.common.exception.NoPathConnectedException;
import nextstep.common.exception.NoRegisterStationException;
import nextstep.common.exception.NotFoundException;
import nextstep.common.exception.PasswordMatchException;
import nextstep.common.exception.SameStationException;
import nextstep.common.exception.ValidateTokenException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NoPathConnectedException.class)
    protected ResponseEntity<ErrorResponse> handleNoPathConnectedException(NoPathConnectedException e) {
        final ErrorResponse errorResponse = ErrorResponse.from(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(NoRegisterStationException.class)
    protected ResponseEntity<ErrorResponse> handleNoRegisterStationException(NoRegisterStationException e) {
        final ErrorResponse errorResponse = ErrorResponse.from(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(SameStationException.class)
    protected ResponseEntity<ErrorResponse> handleSameStationException(SameStationException e) {
        final ErrorResponse errorResponse = ErrorResponse.from(e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        final ErrorResponse errorResponse = ErrorResponse.from(e.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(PasswordMatchException.class)
    protected ResponseEntity<ErrorResponse> handlePasswordMatchException(PasswordMatchException e) {
        final ErrorResponse errorResponse = ErrorResponse.from(e.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MissingTokenException.class)
    protected ResponseEntity<ErrorResponse> handleMissingTokenException(MissingTokenException e) {
        final ErrorResponse errorResponse = ErrorResponse.from(e.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ValidateTokenException.class)
    protected ResponseEntity<ErrorResponse> handleValidateTokenException(ValidateTokenException e) {
        final ErrorResponse errorResponse = ErrorResponse.from(e.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(EmailInputException.class)
    protected ResponseEntity<ErrorResponse> handleEmailInputException(EmailInputException e) {
        final ErrorResponse errorResponse = ErrorResponse.from(e.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        final List<String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        final ErrorResponse errorResponse = new ErrorResponse(errors);
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }
}
