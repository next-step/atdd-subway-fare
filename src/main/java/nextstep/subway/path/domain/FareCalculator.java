package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;

public class FareCalculator {

    private final Sections sections;

    public FareCalculator(Sections sections) {
        this.sections = sections;
    }

    private final int BASIC_FARE = 1250;

    public int getTotalFare() {
        int distance = sections.getTotalDistance();
        return BASIC_FARE + calculateOverFare(distance);
    }

    private int calculateOverFare(int distance) {
        int over10KmFare = calculateOverDistanceFare(FarePolicy.TENKM, distance);
        int over50KmFare = calculateOverDistanceFare(FarePolicy.FIFTYKM, distance);
        return over10KmFare + over50KmFare;
    }

    private int calculateOverDistanceFare(FarePolicy farePolicy, int distance){
        distance -= farePolicy.getOverChargeDistance();
        if(distance>0){
            int calculatedFare = (int) ((Math.ceil((distance - 1) / farePolicy.takeChargeEveryNKm()) + 1) * 100);
            return Math.min(farePolicy.getMaximumFare(), calculatedFare);
        }
        return 0;
    }
}
