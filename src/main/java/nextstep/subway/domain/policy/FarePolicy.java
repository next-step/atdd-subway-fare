package nextstep.subway.domain.policy;

public interface FarePolicy {

    int calculate(CalculateConditions conditions);

}
