package nextstep.subway.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountTest {

	@ParameterizedTest
	@CsvSource({"6, 1000, 325", "12, 2000, 825", "18, 2500, 1720"})
	void calculateDiscountAmount(int age, int fare, int expectedDiscountFare) {
		int discountFare = Discount.calculateDiscountAmount(fare, age);

		assertThat(discountFare).isEqualTo(expectedDiscountFare);
	}
}