package nextstep.subway.domain.fare;

import nextstep.subway.domain.Fare;

public class DefaultFareHandler extends FareHandler {
    private static final int MAX_DISTANCE = 10;
    private static final Fare DEFAULT_FARE = Fare.of(1250);

    public DefaultFareHandler(FareHandler fareHandler) {
        super(fareHandler);
    }

    @Override
    public Fare handle(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("요금 계산시 거리는 1 이상 이어야 합니다.");
        }
        Fare fare = DEFAULT_FARE.add(super.handle(distance - MAX_DISTANCE));
        return fare;
    }

}
