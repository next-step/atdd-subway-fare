package nextstep.api.subway.domain.operators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * @author : Rene Choi
 * @since : 2024/02/25
 */
@Component
public class FareCalculator {

	private static final int BASIC_FARE = 1250;
	private static final int FIRST_THRESHOLD = 10; // km
	private static final int SECOND_THRESHOLD = 50; // km
	private static final int FIRST_INCREMENT = 5; // km
	private static final int SECOND_INCREMENT = 8; // km
	private static final int ADDITIONAL_FARE = 100; // won

	/**
	 * 노선별 추가 요금을 관리하는 맵 -> 하드코딩으로 구현
	 */
	private static final Map<Long, Integer> LINE_ADDITIONAL_FARES = new HashMap<>();

	static {
		LINE_ADDITIONAL_FARES.put(1L, 200);
		LINE_ADDITIONAL_FARES.put(2L, 500);
		LINE_ADDITIONAL_FARES.put(3L, 900);
		LINE_ADDITIONAL_FARES.put(4L, 1200);
	}

	public int calculateFareWithLineCharges(long distance, List<Long> lineIds) {
		int fare = calculateFare(distance);
		int highestAdditionalFare = findHighestAdditionalFare(lineIds);
		return fare + highestAdditionalFare;
	}

	public  int calculateFare(long distance) {
		if (distance <= FIRST_THRESHOLD) {
			return BASIC_FARE;
		}
		if (distance <= SECOND_THRESHOLD) {
			return BASIC_FARE + calculateIncrementalFare(distance - FIRST_THRESHOLD, FIRST_INCREMENT);
		}
		return BASIC_FARE
			+ calculateIncrementalFare(SECOND_THRESHOLD - FIRST_THRESHOLD, FIRST_INCREMENT)
			+ calculateIncrementalFare(distance - SECOND_THRESHOLD, SECOND_INCREMENT);
	}

	private int calculateIncrementalFare(long distance, int increment) {
		return (int) ((Math.ceil((double) distance / increment)) * ADDITIONAL_FARE);
	}

	private int findHighestAdditionalFare(List<Long> lineIds) {
		return lineIds.stream()
			.mapToInt(this::calculateAdditionalLineFare)
			.max()
			.orElse(0);
	}

	private int calculateAdditionalLineFare(Long lineId) {
		return LINE_ADDITIONAL_FARES.getOrDefault(lineId, 0);
	}

}
