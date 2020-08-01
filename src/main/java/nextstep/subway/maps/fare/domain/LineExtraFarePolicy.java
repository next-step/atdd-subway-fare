package nextstep.subway.maps.fare.domain;

public class LineExtraFarePolicy implements FarePolicy {
    @Override
    public void calculate(FareContext fareContext) {
        int extraFare = fareContext.getExtraFare();

        Fare fare = fareContext.getFare();
        fare.plusFare(extraFare);
    }
}
