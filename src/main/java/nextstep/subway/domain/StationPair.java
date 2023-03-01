package nextstep.subway.domain;

public class StationPair {
    private final Station source;
    private final Station target;

    public StationPair(Station source, Station target) {
        if (source == target) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }

        this.source = source;
        this.target = target;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
