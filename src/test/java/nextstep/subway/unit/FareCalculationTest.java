package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.domain.FareCalculation;

public class FareCalculationTest {

	private FareCalculation fareCalculation = new FareCalculation();

	@DisplayName("거리가 0이하라면 에러 발생")
	@ParameterizedTest
	@ValueSource(ints = {0, -1})
	void failFareCalculation(int distance) {
		assertThrows(IllegalArgumentException.class, () -> fareCalculation.fareCalculation(distance));
	}

	@DisplayName("거리 <= 10km, 기본 요금")
	@ParameterizedTest
	@MethodSource("defaultDistance")
	void fareCalculation10mkUnder(int distance, int fare) {
		assertThat(fareCalculation.fareCalculation(distance)).isEqualTo(fare);
	}

	@DisplayName("10km < 거리 <= 50km, 기본 요금 + 5km 마다 100원 추가")
	@ParameterizedTest
	@MethodSource("underLongDistance")
	void fareCalculation50mkUnder(int distance, int fare) {
		assertThat(fareCalculation.fareCalculation(distance)).isEqualTo(fare);
	}

	@DisplayName("거리 > 50km, 기본 요금 + 8km 마다 100원 추가")
	@ParameterizedTest
	@MethodSource("overLongDistance")
	void fareCalculation50mkOver(int distance, int fare) {
		assertThat(fareCalculation.fareCalculation(distance)).isEqualTo(fare);
	}

	private static Stream<Arguments> defaultDistance() {
		return Stream.of(
			Arguments.of(1, 1250),
			Arguments.of(5, 1250),
			Arguments.of(10, 1250)
		);
	}

	private static Stream<Arguments> underLongDistance() {
		return Stream.of(
			Arguments.of(15, 1350),
			Arguments.of(20, 1450),
			Arguments.of(25, 1550),
			Arguments.of(30, 1650),
			Arguments.of(35, 1750),
			Arguments.of(40, 1850),
			Arguments.of(45, 1950),
			Arguments.of(49, 2050),
			Arguments.of(50, 2050)
		);
	}

	private static Stream<Arguments> overLongDistance() {
		return Stream.of(
			Arguments.of(57, 1850),
			Arguments.of(58, 1850),
			Arguments.of(64, 1950),
			Arguments.of(72, 2050),
			Arguments.of(80, 2150),
			Arguments.of(84, 2250),
			Arguments.of(88, 2250)
		);
	}
}
