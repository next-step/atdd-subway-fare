package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.domain.AgeDiscountCalculator;

public class AgeDiscountCalculatorTest {
	private final int 해당사항없음 = -1;
	private final int 어린이 = 8;
	private final int 청소년 = 15;
	private final int 어른  = 25;
	private final int 기본요금 = 1250;

	@DisplayName("어린이 할인 요금")
	@Test
	void 어린이_할인() {
		// when
		int 요금 = AgeDiscountCalculator.calculate(기본요금, 어린이);

		// then
		assertThat(요금).isEqualTo(450);
	}

	@DisplayName("청소년 할인 요금")
	@Test
	void 청소년_할인() {
		// when
		int 요금 = AgeDiscountCalculator.calculate(기본요금, 청소년);

		// then
		assertThat(요금).isEqualTo(720);
	}

	@DisplayName("어른 할인 요금")
	@Test
	void 어른_할인() {
		// when
		int 요금 = AgeDiscountCalculator.calculate(기본요금, 어른);

		// then
		assertThat(요금).isEqualTo(기본요금);
	}
}
