package nextstep.subway.path.exception;

public class InvalidFareAmountException extends RuntimeException {
    public InvalidFareAmountException(String message){
        super(message);
    }
}
