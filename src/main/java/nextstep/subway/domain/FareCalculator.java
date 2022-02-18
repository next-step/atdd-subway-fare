package nextstep.subway.domain;

public interface FareCalculator {

    int calculateFare(int fareDistance, Sections sections, int age);

}
