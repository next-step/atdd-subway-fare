package nextstep.subway.domain.policy;

public interface FarePolicy {

    boolean supports(int distance);

    int fare(int distance);
}
