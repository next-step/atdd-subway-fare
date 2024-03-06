package nextstep.path.domain;

import nextstep.path.DistanceFare;

import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class Fare implements Discountable {

    private int fare = 0;
    private final List<LineFare> lineFares;
    private final int distance;

    public Fare(List<LineFare> lineFares, int distance) {
        this.lineFares = lineFares;
        this.distance = distance;
    }


    public void calculateFare(List<DistanceFare> distanceFares, DiscountCondition discountCondition) {
        for (DistanceFare distanceFare : distanceFares) {
            fare += distanceFare.calculateFare(this);
        }
        addExtraFare();
        this.fare = discount(discountCondition);
    }

    private void addExtraFare() {
        HashSet<LineFare> fares = new HashSet<>(lineFares);
        for (LineFare lineFare : fares) {
            if (lineFare.hasExtraFare()) {
                addFare(lineFare.getExtraFare());
            }
        }
    }

    private void addFare(int extraFare) {
        this.fare += extraFare;
    }

    @Override
    public int discount(DiscountCondition discountCondition) {
        if (discountCondition.support()) {
            return discountCondition.discount(this.fare);
        }
        return this.fare;
    }


    public int getFare() {
        return fare;
    }


    public int getDistance() {
        return distance;
    }
}
