package nextstep.subway.domain.fare;

import nextstep.subway.domain.Fare;

public class OverFiftyKiloFareHandler extends FareHandler {
    public OverFiftyKiloFareHandler(FareHandler fareHandler) {
        super(fareHandler);
    }

    @Override
    public Fare handle(int distance) {
        if (distance <= 0) {
            return Fare.ZERO.add(super.handle(distance));
        }
        int fare = (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
        Fare finalFare = Fare.of(fare).add(super.handle(distance));
        return finalFare;
    }
}
