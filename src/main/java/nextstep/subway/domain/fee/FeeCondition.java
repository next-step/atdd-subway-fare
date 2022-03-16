package nextstep.subway.domain.fee;

public interface FeeCondition {

    int DEFAULT_FEE = 1250;
    int EXTRA_FEE = 100;
    int MIN_REFERENCE_DISTANCE = 10;
    int MAX_REFERENCE_DISTANCE = 50;

    boolean isInclude(int distance);
    int calculateFee(int distance);
}
