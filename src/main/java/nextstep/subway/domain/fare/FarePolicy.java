package nextstep.subway.domain.fare;

public interface FarePolicy {

	Fare calculate(Fare fare);
}
