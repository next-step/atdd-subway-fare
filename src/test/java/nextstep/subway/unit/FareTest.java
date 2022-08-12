package nextstep.subway.unit;

import nextstep.subway.domain.fare.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {
	@DisplayName("거리 별로 요금이 다르게 나오는 것을 확인한다.")
	@CsvSource(value = {"10:1250", "50:2050", "51:2150", "100:2750"}, delimiter = ':')
	@ParameterizedTest
	void fareTest(int distance, int fare) {
		assertThat(Fare.calculate(distance)).isEqualTo(fare);
	}
}