package nextstep.line.domain.exception;

public class IllegalSectionOperationException extends RuntimeException {
    public static final String ALREADY_INCLUDED = "이미 존재하는 구간입니다";
    public static final String NOT_ENOUGH_SECTION = "구간이 하나 이하일 땐 삭제할 수 없습니다.";
    public static final String CANT_COMBINE = "하행역과 상행역이 겹치는 구간끼리만 합칠 수 있습니다.";
    public static final String INVALID_DISTANCE = "구간의 길이는 1보다 작을 수 없습니다.";
    public static final String INVALID_DURATION = "구간의 소요시간은 1보다 작을 수 없습니다.";
    public static final String CANT_SUBTRACT = "상행역과 하행역중 하나가 겹치는 구간끼리만 뺄 수 있습니다.";

    public IllegalSectionOperationException(String message) {
        super(message);
    }
}
