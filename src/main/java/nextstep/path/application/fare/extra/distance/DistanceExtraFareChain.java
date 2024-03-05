package nextstep.path.application.fare.extra.distance;

import nextstep.path.exception.FareApplyingException;

public class DistanceExtraFareChain {
    private final BaseDistanceExtraFareHandler basePathFareHandler;

    public DistanceExtraFareChain() {
        basePathFareHandler = new BaseDistanceExtraFareHandler();
    }

    public DistanceExtraFareChain addNext(final DistanceExtraFareHandler distanceExtraFareHandler) {
        validate(distanceExtraFareHandler);
        basePathFareHandler.next(distanceExtraFareHandler);
        return this;
    }

    private void validate(final DistanceExtraFareHandler distanceExtraFareHandler) {
        final int standardDistance = distanceExtraFareHandler.getStandardDistance();
        final int fareInterval = distanceExtraFareHandler.getFareInterval();
        if (fareInterval < 1) {
            throw new FareApplyingException("fareInterval must be grater than 0");
        }
        if (standardDistance < 1) {
            throw new FareApplyingException("standardDistance must be grater than 0");
        }
        final DistanceExtraFareHandler tail = basePathFareHandler.getTail();
        if (!tail.equals(basePathFareHandler) && tail.getStandardDistance() > standardDistance) {
            throw new FareApplyingException(String.format("standardDistance must be grater than previous standardDistance %d", standardDistance));
        }
    }

    public long calculate(final int distance) {
        return basePathFareHandler.calculate(distance);
    }

}
