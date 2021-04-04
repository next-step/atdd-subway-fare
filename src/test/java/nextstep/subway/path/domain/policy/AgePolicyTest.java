package nextstep.subway.path.domain.policy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.path.domain.Fare;

class AgePolicyTest {

	@DisplayName("어린이 할인 정책(6 ~ 12)")
	@ParameterizedTest
	@CsvSource(value = {
		"6, 1250, 800", "12, 1450, 900"
	})
	void childPolicy(int age, int fare, int expectedFare) {
		// given
		FarePolicy policy = AgePolicy.of(age);

		// when
		Fare calculateFare = policy.getFare(fare);

		// then
		assertThat(calculateFare.getFare()).isEqualTo(expectedFare);
	}

	@DisplayName("청소년 할인 정책(13 ~ 18)")
	@ParameterizedTest
	@CsvSource(value = {
		"13, 1250, 1070", "18, 1450, 1230"
	})
	void youthPolicy(int age, int fare, int expectedFare) {
		// given
		FarePolicy policy = AgePolicy.of(age);

		// when
		Fare calculateFare = policy.getFare(fare);

		// then
		assertThat(calculateFare.getFare()).isEqualTo(expectedFare);
	}

	@DisplayName("19세 이상은 할인 받지 못함")
	@ParameterizedTest
	@CsvSource(value = {
		"19, 1250, 1250", "33, 1450, 1450"
	})
	void defaultPolicy(int age, int fare, int expectedFare) {
		// given
		FarePolicy policy = AgePolicy.of(age);

		// when
		Fare calculateFare = policy.getFare(fare);

		// then
		assertThat(calculateFare.getFare()).isEqualTo(expectedFare);
	}

	@DisplayName("영유아 할인 정책(6세 미만)")
	@ParameterizedTest
	@CsvSource(value = {
		"5, 1250, 0", "1, 1450, 0"
	})
	void babyPolicy(int age, int fare, int expectedFare) {
		// given
		FarePolicy policy = AgePolicy.of(age);

		// when
		Fare calculateFare = policy.getFare(fare);

		// then
		assertThat(calculateFare.getFare()).isEqualTo(expectedFare);
	}
}