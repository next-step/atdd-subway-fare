package nextstep.subway.path.domain.policy;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DistancePolicyTest {

	private static final int DEFAULT = 1250;

	@DisplayName("10km 이내면 기본운임 부과")
	@ParameterizedTest
	@ValueSource(ints = {1, 3, 7, 10})
	void fareDefault(int distance) {
		// given
		List<DistancePolicy> distancePolicies = Arrays.asList(new DefaultDistancePolicy(), new FirstOverDistancePolicy());

		// when
		int reduce = distancePolicies.stream()
			.filter(distancePolicy -> distancePolicy.isSupport(distance))
			.map(distancePolicy -> distancePolicy.getFare(distance))
			.reduce(Integer::sum).orElse(0);

		// then
		assertThat(reduce).isEqualTo(DEFAULT);
	}

	@DisplayName("10km 초과 ~ 50km 까지 5km 마다 100원 부과")
	@ParameterizedTest
	@CsvSource(value = {
		"11, 100", "20, 200", "25, 300", "26, 400", "50, 800"
	})
	void fareOver10(int distance, int expectedFare) {
		// given
		List<DistancePolicy> distancePolicies = Arrays.asList(new DefaultDistancePolicy(), new FirstOverDistancePolicy());

		// when
		int reduce = distancePolicies.stream()
			.filter(distancePolicy -> distancePolicy.isSupport(distance))
			.map(distancePolicy -> distancePolicy.getFare(distance))
			.reduce(Integer::sum).orElse(0);

		// then
		assertThat(reduce).isEqualTo(DEFAULT + expectedFare);
	}

	@DisplayName("50km 초과시 8km 마다 100원 부과")
	@ParameterizedTest
	@CsvSource(value = {
		"51, 900", "59, 1000", "63, 1000", "67, 1100"
	})
	void fareOver50(int distance, int expectedFare) {
		// given
		List<DistancePolicy> distancePolicies = Arrays.asList(new DefaultDistancePolicy(), new FirstOverDistancePolicy(), new SecondOverDistancePolicy());

		// when
		int reduce = distancePolicies.stream()
			.filter(distancePolicy -> distancePolicy.isSupport(distance))
			.map(distancePolicy -> distancePolicy.getFare(distance))
			.reduce(Integer::sum).orElse(0);

		// then
		assertThat(reduce).isEqualTo(DEFAULT + expectedFare);
	}
}