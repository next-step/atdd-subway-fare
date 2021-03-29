package nextstep.subway.path.domain;

public abstract class Fare {

    public static final int BASE_FARE = 1250;
    public static final int ADD_FARE = 100;

    abstract int calculate();

    public static int getTotalFare(PathResult pathResult) {
        Fare fare10To50 = new Fare10To50(pathResult);
        Fare fareOver50 = new FareOver50(pathResult);
        return BASE_FARE + fare10To50.calculate() + fareOver50.calculate();
    }
}
