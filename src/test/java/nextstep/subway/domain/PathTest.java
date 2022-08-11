package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Path 관련 테스트를 진행한다.")
class PathTest {
	@Test
	void 총_거리가_10km_이하_일_때_요금을_반환한다() {
		assertThat(Fare.calculate(10)).isEqualTo(1250);
	}

	@Test
	void 총_거리가_50km_이하_일_때_요금을_반환한다() {
		assertThat(Fare.calculate(50)).isEqualTo(2050);
	}

	@Test
	void 총_거리가_51km_일_때_요금을_반환한다() {
		assertThat(Fare.calculate(51)).isEqualTo(2150);
	}

	@Test
	void 총_거리가_50km_초과_일_때_요금을_반환한다() {
		assertThat(Fare.calculate(100)).isEqualTo(2750);
	}
}