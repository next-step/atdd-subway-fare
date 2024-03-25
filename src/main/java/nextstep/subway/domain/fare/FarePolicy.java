package nextstep.subway.domain.fare;

public interface FarePolicy {
    int applyAdditionalFare(int fare);

    int applyDiscountFare(int fare);

    double applyDiscountPercent(int fare);
}
