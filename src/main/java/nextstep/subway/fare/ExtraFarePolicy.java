package nextstep.subway.fare;

@FunctionalInterface
public interface ExtraFarePolicy {
    int addExtraFee(int extra);
}
