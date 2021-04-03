package nextstep.subway.path.domain;

public class Fare {

    private FareCalculationStrategy fareCalculationStrategy;
    private int fare;

    public Fare(FareCalculationStrategy fareCalculationStrategy) {
        this.fareCalculationStrategy = fareCalculationStrategy;
    }

    public Fare(int fare) {
        this.fare = fare;
    }

//    public int get() {
//        return fareCalculationStrategy.calculate();
//    }

    public int getFare() {
        return fare;
    }
}
