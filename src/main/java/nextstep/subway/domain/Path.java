package nextstep.subway.domain;

public class Path {
    private final int distance;
    private final int duration;

    public Path(int distance, int duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
