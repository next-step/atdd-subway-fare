package nextstep.subway.domain;

import nextstep.subway.domain.fare.DistanceFarePolicy;
import nextstep.subway.domain.fare.Fare;
import nextstep.subway.domain.fare.FarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class DistanceFarePolicyTest {


	@DisplayName("10km 이내 기본 운임 1,250원")
	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3, 5, 7, 10})
	void calculateFareWithin10km(int distance) {
		Fare fare = calculateFare(distance);

		// then
		assertThat(fare).isEqualTo(Fare.valueOf(1250));
	}

	private Fare calculateFare(int distance) {
		FarePolicy distanceFarePolicy = new DistanceFarePolicy(distance);
		return distanceFarePolicy.calculate(Fare.valueOf(0));
	}

	@DisplayName("10km 초과 50km 까지, 5km 마다 100원")
	@ParameterizedTest
	@CsvSource({"11,1350", "12,1350", "20,1450", "49,2050", "50,2050"})
	void calculateFareGreaterThan10kmLessThanOrEqualTo50km(int distance, int expectedFare) {
		Fare fare = calculateFare(distance);

		// then
		assertThat(fare).isEqualTo(Fare.valueOf(expectedFare));
	}

	@DisplayName("50km 초과, 8km 마다 100원")
	@ParameterizedTest
	@CsvSource({"51,2150", "58,2150", "59,2250", "100,2750", "686,10050"})
	void calculateFareGreaterThan50km(int distance, int expectedFare) {
		Fare fare = calculateFare(distance);

		// then
		assertThat(fare).isEqualTo(Fare.valueOf(expectedFare));
	}

}