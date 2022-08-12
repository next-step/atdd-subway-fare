package nextstep.subway.unit;

import nextstep.subway.domain.fare.DiscountPolicy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountPolicyTest {

	@CsvSource(value = {"12:2350:1000", "13:2350:1600", "18:2350:1600", "19:2350:2350"}, delimiter = ':')
	@ParameterizedTest
	void 다양한_나이에_따른_요금을_검증한다(int age, int fare, int answer) {
		assertThat(DiscountPolicy.calculate(age, fare)).isEqualTo(answer);
	}
}