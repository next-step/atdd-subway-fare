package nextstep.common;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authorization.secured.RoleAuthenticationException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<Void> handleConversionFailedException(ConversionFailedException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        ErrorCode errorCode = GlobalErrorCode.UNAUTHORIZED;
        return ExceptionHandlerUtils.createErrorResponse(errorCode, e);
    }

    @ExceptionHandler(RoleAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleRoleAuthenticationException(RoleAuthenticationException e) {
        ErrorCode errorCode = GlobalErrorCode.FORBIDDEN;
        return ExceptionHandlerUtils.createErrorResponse(errorCode, e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ValidationErrorResponse.ValidationError> validationErrors = toValidationErrors(e);
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(validationErrors));
    }

    private List<ValidationErrorResponse.ValidationError> toValidationErrors(MethodArgumentNotValidException e) {
        return e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(it -> new ValidationErrorResponse.ValidationError(it.getField(), it.getDefaultMessage()))
                .collect(Collectors.toList());
    }
}
