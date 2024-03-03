package nextstep.path.fare.distance;

public interface DistanceFareChain {
    DistanceFareChain addNext(DistanceFareChain nextFareChain);

    int calculate(int distance);
}
