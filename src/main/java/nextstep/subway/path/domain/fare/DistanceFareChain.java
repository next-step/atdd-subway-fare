package nextstep.subway.path.domain.fare;

public abstract class DistanceFareChain implements FareChain {

    protected FareChain nextChain;

    @Override
    public void setFareChain(FareChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public int calculate(int distance, int fare) {
        fare = calculateFare(distance, fare);

        if(nextChain == null) {
            return fare;
        }
        return nextChain.calculate(distance, fare);
    }

    public abstract int calculateFare(int distance, int fare);

    protected int calculateOverFare(int distance, int offset) {
        return (int) ((Math.ceil((distance - 1) / offset) + 1) * 100);
    }
}
