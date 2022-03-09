package nextstep.subway.domain;

public interface FarePolicy {

    int fare(int age, int requestFare, Path path);
}
