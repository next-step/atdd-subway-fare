package nextstep.subway.path.domain;

public class Fare10To50 extends Fare {

    private static final int FROM_DISTANCE = 10;
    private static final int TO_DISTANCE = 50;
    private static final int ADD_DISTANCE = 5;

    private final PathResult pathResult;

    public Fare10To50(PathResult pathResult) {
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
        if (totalDistance > TO_DISTANCE) {
            return TO_DISTANCE - FROM_DISTANCE;
        }
        return totalDistance - FROM_DISTANCE;
    }
}
