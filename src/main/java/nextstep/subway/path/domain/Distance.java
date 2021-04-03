package nextstep.subway.path.domain;

public class Distance {
    private int distance;

    public Distance(int distance) {
        if(distance < 0) throw new IllegalArgumentException("거리는 0이상 이어야합니다.");
        this.distance = distance;
    }

    public int toIntValue() {
        return distance;
    }

    public Distance minus(Distance d){
        int result = (distance - d.toIntValue() < 0) ? 0 : (distance - d.toIntValue());
        return new Distance(result);
    }

}
