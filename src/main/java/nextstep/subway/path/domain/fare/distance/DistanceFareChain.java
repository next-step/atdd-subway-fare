package nextstep.subway.path.domain.fare.distance;

public abstract class DistanceFareChain implements FareChain {

    @Override
    public int calculate(int distance) {
        return calculateFare(distance);
    }

    protected abstract int calculateFare(int distance);

    protected int calculateOverFare(int distance, int offset) {
        return (int) ((Math.ceil((distance - 1) / offset) + 1) * 100);
    }
}
