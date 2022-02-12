package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class FareCalculatorTest {

	@DisplayName("10km 이내 기본 운임 1,250원")
	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3, 5, 7, 10})
	void calculateFareWithin10km() {
		// when
		int fare = FareCalculator.calculateFare(10);

		// then
		assertThat(fare).isEqualTo(1250);
	}

	@DisplayName("10km 초과 50km 까지, 5km 마다 100원")
	@ParameterizedTest
	@CsvSource({"11,1350", "12,1350", "20,1450", "49,2050", "50,2050"})
	void calculateFareGreaterThan10kmLessThanOrEqualTo50km(int distance, int expectedFare) {
		// when
		int fare = FareCalculator.calculateFare(distance);

		// then
		assertThat(fare).isEqualTo(expectedFare);
	}

	@DisplayName("50km 초과, 8km 마다 100원")
	@ParameterizedTest
	@CsvSource({"51,2150", "58,2150", "59,2250", "100,2750", "686,10050"})
	void calculateFareGreaterThan50km(int distance, int expectedFare) {
		// when
		int fare = FareCalculator.calculateFare(distance);

		// then
		assertThat(fare).isEqualTo(expectedFare);
	}

}