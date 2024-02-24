package nextstep.path.application;

public class SecondExtraFareHandler extends PathFareHandler {

    @Override
    public long calculate(final int distance) {
        final int targetDistance = getTargetDistance(distance);

        int extraFare = 0;
        if (targetDistance > getStandardDistance()) {
            extraFare += calculateOverFare(targetDistance - getStandardDistance());
        }

        return extraFare + calculateNext(distance);
    }

    @Override
    protected int getStandardDistance() {
        return 50;
    }

    @Override
    protected int getStandardInterval() {
        return 8;
    }


}


