package nextstep.subway.domain.policy;

public class ExtraLineFare implements FarePolicy {

    @Override
    public boolean supports(PathByFare pathByFare) {
        return true;
    }

    @Override
    public int fare(PathByFare pathByFare) {
        return 0;
    }
}
