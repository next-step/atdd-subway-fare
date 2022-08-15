package nextstep.subway.domain.policy;

public interface FarePolicy {

    boolean supports(PathByFare pathByFare);

    int fare(PathByFare pathByFare);
}
