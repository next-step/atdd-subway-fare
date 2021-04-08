package nextstep.subway.path.domain.fare.policy;

public interface FarePolicy {

    int DEFAULT_FARE = 1250;

    int calculate(int fare, int distance);
}
