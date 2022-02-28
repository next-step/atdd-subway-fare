package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.domain.FareCalculator;

public class FareCalculatorTest {
	private final int 기본_운임_비용 = 1250;

	private final int 팔키로 = 8;
	private final int 십이키로 = 12;
	private final int 오십구키로 = 59;

	@DisplayName("거리 <= 10km 운임비용")
	@Test
	void 십키로_이하_거리_요금() {
		// when
		int 운임비용 = FareCalculator.calculate(팔키로);

		// then
		assertThat(운임비용).isEqualTo(기본_운임_비용);
	}

	@DisplayName("10km < 거리 <= 50km 운임비용")
	@Test
	void 십키로_초과_오십키로_이하_거리_요금() {
		// when
		int 운임비용 = FareCalculator.calculate(십이키로);

		// then
		assertThat(운임비용).isEqualTo(기본_운임_비용 + 100);
	}

	@DisplayName("50km < 거리 운임비용")
	@Test
	void 오십키로_초과_거리_요금() {
		// when
		int 운임비용 = FareCalculator.calculate(오십구키로);

		// then
		assertThat(운임비용).isEqualTo(기본_운임_비용 + 1000);
	}
}
