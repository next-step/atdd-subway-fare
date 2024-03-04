package nextstep.path.application.fare.extra.distance;

import nextstep.path.exception.FareApplyingException;

public class FareChain {
    private final BaseFareHandler basePathFareHandler;

    public FareChain() {
        basePathFareHandler = new BaseFareHandler();
    }

    public FareChain addNext(final PathFareHandler pathFareHandler) {
        validate(pathFareHandler);
        basePathFareHandler.next(pathFareHandler);
        return this;
    }

    private void validate(final PathFareHandler pathFareHandler) {
        final int standardDistance = pathFareHandler.getStandardDistance();
        final int fareInterval = pathFareHandler.getFareInterval();
        if (fareInterval < 1) {
            throw new FareApplyingException("fareInterval must be grater than 0");
        }
        if (standardDistance < 1) {
            throw new FareApplyingException("standardDistance must be grater than 0");
        }
        final PathFareHandler tail = basePathFareHandler.getTail();
        if (!tail.equals(basePathFareHandler) && tail.getStandardDistance() > standardDistance) {
            throw new FareApplyingException(String.format("standardDistance must be grater than previous standardDistance %d", standardDistance));
        }
    }

    public long calculate(final int distance) {
        return basePathFareHandler.calculate(distance);
    }

}
