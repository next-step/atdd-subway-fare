package nextstep.subway.domain.fare;

public interface FarePolicy {
    int getAdditionalFee();

    int getDiscountFee();

    double getDiscountPercent();
}
