package nextstep.subway.domain;

abstract class FarePolicy {
    protected FarePolicy farePolicy = null;

    public FarePolicy setNext(FarePolicy farePolicy) {
        this.farePolicy = farePolicy;
        return farePolicy;
    }

    protected abstract int calculateFare(int fare);

    public int run(int fare) {
        int result = calculateFare(fare);
        if (farePolicy != null) {
            result = farePolicy.run(result);
        }
        return result;
    }
}
