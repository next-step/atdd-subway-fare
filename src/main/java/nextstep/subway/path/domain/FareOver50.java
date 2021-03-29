package nextstep.subway.path.domain;

public class FareOver50 extends Fare {

    private static final int FROM_DISTANCE = 50;
    private static final int ADD_DISTANCE = 8;

    private final PathResult pathResult;

    public FareOver50(PathResult pathResult) {
        this.pathResult = pathResult;
    }

    @Override
    public int calculate() {
        int totalDistance = pathResult.getTotalDistance();
        if (totalDistance <= FROM_DISTANCE) {
            return 0;
        }
        return (int) ((Math.ceil((getFareDistance(totalDistance) - 1) / ADD_DISTANCE) + 1) * ADD_FARE);
    }

    private int getFareDistance(int totalDistance) {
        return totalDistance - FROM_DISTANCE;
    }
}
