package nextstep.subway.maps.map.domain;

public class FareContext {
    private final int distance;

    public FareContext(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return this.distance;
    }
}
