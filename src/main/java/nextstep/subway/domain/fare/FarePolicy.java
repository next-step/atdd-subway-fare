package nextstep.subway.domain.fare;

public interface FarePolicy {
    Fare calculateFare(Fare fare, FareBasis fareBasis);
}
