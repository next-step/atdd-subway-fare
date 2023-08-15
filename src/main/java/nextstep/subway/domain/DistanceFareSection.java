package nextstep.subway.domain;

public enum DistanceFareSection {
    SECOND(50, 8, 100),
    FIRST(10, 5, 100);

    private final int limit;
    private final int distance;
    private final int additionalFare;

    DistanceFareSection(int limit, int distance, int additionalFare) {
        this.limit = limit;
        this.distance = distance;
        this.additionalFare = additionalFare;
    }

    public int calculateOverFare(int distance) {
        return (int) Math.ceil((double) (distance - this.limit) / this.distance) * additionalFare;
    }

    public int remainDistance(int distance) {
        return distance - (distance % this.limit);
    }

    public boolean inRange(int distance) {
        return distance > this.limit;
    }
}
