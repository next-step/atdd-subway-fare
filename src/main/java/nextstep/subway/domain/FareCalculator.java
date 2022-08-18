package nextstep.subway.domain;


public interface FareCalculator {

    public void nextFareChain(FareCalculator nextCalculator);

    public int calculate(int totalFare, int totalDistance);

}