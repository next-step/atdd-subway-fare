package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.domain.Fare;
import nextstep.subway.exception.ErrorMessage;
import nextstep.subway.exception.NegativeDistanceException;

class FareTest {

	public static Stream<Arguments> providerOfPathWithinTenKM() {
		return Stream.of(
			Arguments.arguments(1, 1_250),
			Arguments.arguments(9, 1_250),
			Arguments.arguments(10, 1_250)
		);
	}

	public static Stream<Arguments> providerOfPathExceedingTenKMAndLessThanFiftyKM() {
		return Stream.of(
			Arguments.arguments(11, 1_350),
			Arguments.arguments(16, 1_450),
			Arguments.arguments(21, 1_550),
			Arguments.arguments(26, 1_650),
			Arguments.arguments(31, 1_750),
			Arguments.arguments(36, 1_850),
			Arguments.arguments(41, 1_950),
			Arguments.arguments(46, 2_050),
			Arguments.arguments(49, 2_050),
			Arguments.arguments(50, 2_050)
		);
	}

	public static Stream<Arguments> providerOfPathExceedingFiftyKM() {
		return Stream.of(
			Arguments.arguments(51, 2_150),
			Arguments.arguments(57, 2_150),
			Arguments.arguments(58, 2_250),
			Arguments.arguments(59, 2_250)
		);
	}

	@DisplayName("경로 10KM 이내일 경우, 요금을 계산한다.")
	@MethodSource("providerOfPathWithinTenKM")
	@ParameterizedTest
	void calculateFare_WITH_IN_10_KM(int distance, int fare) {
		assertThat(Fare.of(distance).getFare()).isEqualTo(fare);
	}

	@DisplayName("경로 10KM 이내일 경우, 요금을 계산한다.")
	@MethodSource("providerOfPathExceedingTenKMAndLessThanFiftyKM")
	@ParameterizedTest
	void calculateFare_EXCEEDING_10_KM_AND_LESS_THAN_50_KM(int distance, int fare) {
		assertThat(Fare.of(distance).getFare()).isEqualTo(fare);
	}

	@DisplayName("경로 10KM 이내일 경우, 요금을 계산한다.")
	@MethodSource("providerOfPathExceedingFiftyKM")
	@ParameterizedTest
	void calculateFare_EXCEEDING_50_KM(int distance, int fare) {
		assertThat(Fare.of(distance).getFare()).isEqualTo(fare);
	}

	@DisplayName("거리가 양수가 아니면 예외를 던진다.")
	@ValueSource(ints = {0, -1})
	@ParameterizedTest
	void WHEN_DISTANCE_IS_NEGATIVE_THEN_THROW_EXCEPTION(int distance) {
		assertThatThrownBy(() -> Fare.of(distance).getFare())
			.isInstanceOf(NegativeDistanceException.class)
			.hasMessage(ErrorMessage.SHOULD_BE_POSITIVE.getMessage());
	}
}
