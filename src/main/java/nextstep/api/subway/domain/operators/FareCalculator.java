package nextstep.api.subway.domain.operators;

import java.util.HashMap;
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

	// 노선별 추가 요금을 관리하는 맵 (임시 하드코딩)
	private static final Map<Long, Integer> LINE_ADDITIONAL_FARES = new HashMap<>();

	static {
		LINE_ADDITIONAL_FARES.put(1L, 900);
		// 다른 노선에 대한 추가 요금도 이곳에 추가...
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

	// 노선별 추가 요금 계산
	private int calculateAdditionalLineFare(Long lineId) {
		return LINE_ADDITIONAL_FARES.getOrDefault(lineId, 0);
	}
}
