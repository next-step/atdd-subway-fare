package nextstep.subway.domain;

public interface FarePolicy {
    int getAdditionalFee();

    int getDiscountFee();

    double getDiscountPercent();
}
