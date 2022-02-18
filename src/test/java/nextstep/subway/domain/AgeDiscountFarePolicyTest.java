package nextstep.subway.domain;

import nextstep.subway.domain.fare.AgeDiscountFarePolicy;
import nextstep.subway.domain.fare.Fare;
import nextstep.subway.domain.fare.FarePolicy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeDiscountFarePolicyTest {

	@ParameterizedTest
	@CsvSource({"6, 1000, 325", "12, 2000, 825", "18, 2500, 1720", "100, 1000, 1000", "1, 100, 100"})
	void calculateDiscountAmount(int age, int fare, int expectedDiscountFare) {
		FarePolicy ageDiscountFarePolicy = new AgeDiscountFarePolicy(age);
		Fare discountFare = ageDiscountFarePolicy.calculate(Fare.valueOf(fare));

		assertThat(discountFare).isEqualTo(Fare.valueOf(expectedDiscountFare));
	}
}