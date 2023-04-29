package nextstep.subway.domain.fare;

import nextstep.subway.domain.Fare;

public class TenKiloToFiftyKiloFareHandler extends FareHandler {
    private static final int MAX_DISTANCE = 40;
    private static final Fare MAX_FARE = Fare.of((int) ((Math.ceil((MAX_DISTANCE - 2) / 5) + 1) * 100));

    public TenKiloToFiftyKiloFareHandler(FareHandler fareHandler) {
        super(fareHandler);
    }

    @Override
    public Fare handle(int distance) {
        if (distance <= 0) {
            return Fare.ZERO.add(super.handle(distance));
        }
        if (distance < MAX_DISTANCE) {
            int fare = (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
            Fare finalFare = Fare.of(fare).add(super.handle(distance - MAX_DISTANCE));
            return finalFare;
        }
        Fare fare = MAX_FARE.add(super.handle(distance - MAX_DISTANCE));
        return fare;
    }

}
