package nextstep.path;

public abstract class DistanceFare {
    protected final int distance;

    protected DistanceFare(int distance) {
        this.distance = distance;
    }

    public abstract int calculateFare();

}
