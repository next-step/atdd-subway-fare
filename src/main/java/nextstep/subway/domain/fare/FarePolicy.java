package nextstep.subway.domain.fare;

public interface FarePolicy {
    Fare addFare(Fare fare, FareBasis fareBasis);
}
