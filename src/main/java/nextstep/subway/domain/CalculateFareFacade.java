package nextstep.subway.domain;

public class CalculateFareFacade {

    private final int distance;

    public CalculateFareFacade(int distance) {
        this.distance = distance;
    }

    public int calculateFare() {
        int calculateOverTen = new CalculateOverTen().calculate(distance);
        int calculateOverFifty = new CalculateOverFifty().calculate(distance);
        return calculateOverTen + calculateOverFifty;
    }
}
