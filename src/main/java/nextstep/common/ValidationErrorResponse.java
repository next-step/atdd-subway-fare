package nextstep.common;

import java.util.List;

public class ValidationErrorResponse {
    private final List<ValidationError> errors;

    public ValidationErrorResponse(List<ValidationError> errors) {
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    static class ValidationError {
        private final String filedName;
        private final String message;

        public ValidationError(String filedName, String message) {
            this.filedName = filedName;
            this.message = message;
        }

        public String getFiledName() {
            return filedName;
        }

        public String getMessage() {
            return message;
        }
    }
}
