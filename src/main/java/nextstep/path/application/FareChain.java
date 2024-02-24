package nextstep.path.application;

import nextstep.path.exception.FareApplyingException;

import java.util.Objects;

public class FareChain {
    private final PathFare basePathFare;

    public FareChain() {
        basePathFare = new PathFare(Integer.MAX_VALUE, 1);
    }

    public FareChain nextRange(final int standardDistance, final int fareInterval) {
        validate(standardDistance, fareInterval);
        basePathFare.addNext(new PathFare(standardDistance, fareInterval));
        return this;
    }

    private void validate(final int standardDistance, final int fareInterval) {
        if (fareInterval < 1) {
            throw new FareApplyingException("fareInterval must be grater than 0");
        }
        if (standardDistance < 1) {
            throw new FareApplyingException("standardDistance must be grater than 0");
        }
        final PathFare tail = basePathFare.getTail();
        if (!tail.equals(basePathFare) && tail.standardDistance > standardDistance) {
            throw new FareApplyingException(String.format("standardDistance must be grater than previous standardDistance %d", standardDistance));
        }
    }

    public long calculate(final int distance) {
        return basePathFare.calculate(distance);
    }

    private static class PathFare {
        private static final long BASE_FARE = 1250L;
        private static final long EXTRA_FARE_BASE = 100L;
        private final int standardDistance;
        private final int fareInterval;
        private PathFare nextPathFare;

        private PathFare(final int standardDistance, final int fareInterval) {
            this.standardDistance = standardDistance;
            this.fareInterval = fareInterval;
        }

        private long calculate(final int distance) {
            final int targetDistance = getTargetDistance(distance);

            int extraFare = 0;
            if (targetDistance > standardDistance) {
                extraFare += calculateOverFare(targetDistance - standardDistance);
            }

            return extraFare + calculateNext(distance);
        }

        private void addNext(final PathFare pathFare) {
            if (Objects.nonNull(nextPathFare)) {
                nextPathFare.addNext(pathFare);
                return;
            }
            nextPathFare = pathFare;
        }

        private long calculateNext(final int distance) {
            if (isTail()) {
                return BASE_FARE;
            }
            return nextPathFare.calculate(distance);
        }

        private int getNextStandardDistance() {
            if (isTail()) {
                return Integer.MAX_VALUE;
            }
            return nextPathFare.standardDistance;
        }


        private int calculateOverFare(final int distance) {
            return (int) ((Math.floor((double) (distance - 1) / fareInterval + 1)) * EXTRA_FARE_BASE);
        }


        private int getTargetDistance(final int distance) {
            return Math.min(distance, getNextStandardDistance());
        }

        private boolean isTail() {
            return Objects.isNull(nextPathFare);
        }


        public PathFare getTail() {
            if (isTail()) {
                return this;
            }
            return nextPathFare.getTail();
        }

        @Override
        public boolean equals(final Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            final PathFare pathFare = (PathFare) object;
            return standardDistance == pathFare.standardDistance && fareInterval == pathFare.fareInterval && Objects.equals(nextPathFare, pathFare.nextPathFare);
        }

        @Override
        public int hashCode() {
            return Objects.hash(standardDistance, fareInterval, nextPathFare);
        }
    }

}
