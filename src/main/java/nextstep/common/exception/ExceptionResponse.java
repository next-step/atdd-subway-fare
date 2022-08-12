package nextstep.common.exception;

public class ExceptionResponse {

  public String message;

  public ExceptionResponse(String message) {
    this.message = message;
  }

  public static ExceptionResponse getInstance(String message) {
    return new ExceptionResponse(message);
  }
}
