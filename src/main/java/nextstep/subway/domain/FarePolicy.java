package nextstep.subway.domain;

public interface FarePolicy {

    void setNext(FarePolicy next);
    int fare(int prevFare);
}
