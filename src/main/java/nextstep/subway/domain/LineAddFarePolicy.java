package nextstep.subway.domain;

public class LineAddFarePolicy implements FarePolicy {

    @Override
    public int fare(int age, int requestFare, Path path) {
        return path.maxLineAdditionFare() + requestFare;
    }
}
