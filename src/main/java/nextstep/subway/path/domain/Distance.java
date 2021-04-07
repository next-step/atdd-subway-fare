package nextstep.subway.path.domain;

public class Distance {

    private int distance;

    public Distance(int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException("거리는 0이상 이어야합니다.");
        }
        this.distance = distance;
    }

    public int toInt() {
        return distance;
    }

    public Distance minus(Distance operand){
        int result = (distance - operand.toInt() < 0) ? 0 : (distance - operand.toInt());
        return new Distance(result);
    }

    public boolean isLessThan(Distance operand){
        return distance < operand.toInt();
    }
}
