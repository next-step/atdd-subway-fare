package nextstep.subway.fare;

public class LineExtraFarePolicy implements ExtraFarePolicy {
    @Override
    public int addExtraFee(int extra) {
        return extra;
    }
}
