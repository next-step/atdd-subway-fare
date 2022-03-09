package nextstep.subway.domain;

public class DistanceFarePolicy implements FarePolicy {

    @Override
    public int fare(int age, int requestFare, Path path) {
        return path.distanceFare();
    }
}
