package nextstep.subway.domain.policy.fare;

public interface FarePolicy {

    boolean supports(PathByFare pathByFare);

    int fare(PathByFare pathByFare);
}
