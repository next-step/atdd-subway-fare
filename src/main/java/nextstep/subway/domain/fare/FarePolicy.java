package nextstep.subway.domain.fare;

public interface FarePolicy {
    boolean support(FarePolicyContext context);
    Fare invoke(FarePolicyContext context, Fare fare);
}
