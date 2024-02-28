package nextstep.api.subway.domain.operators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import nextstep.api.auth.domain.dto.UserPrincipal;

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

	private static final int AGE_DISCOUNT_DEDUCTION = 350; // won
	private static final double YOUTH_DISCOUNT_RATE = 0.8;
	private static final double CHILD_DISCOUNT_RATE = 0.5;
	private static final int YOUTH_AGE_LOWER_BOUND = 13;
	private static final int YOUTH_AGE_UPPER_BOUND = 19;
	private static final int CHILD_AGE_LOWER_BOUND = 6;
	private static final int CHILD_AGE_UPPER_BOUND = 13;

	/**
	 * 노선별 추가 요금을 관리하는 맵 -> 하드코딩으로 구현
	 */
	private static final Map<Long, Integer> LINE_ADDITIONAL_FARES = new HashMap<>();

	// todo -> 로그인 사용자에 대한 조건을 설정하는 하드코딩 구현

	static {
		LINE_ADDITIONAL_FARES.put(1L, 200);
		LINE_ADDITIONAL_FARES.put(2L, 500);
		LINE_ADDITIONAL_FARES.put(3L, 900);
		LINE_ADDITIONAL_FARES.put(4L, 1200);
	}



	public int calculateFareWithLineChargesWithAuthUser(long distance, List<Long> lineIds, UserPrincipal userPrincipal) {
		int fare = calculateFare(distance);
		int highestAdditionalFare = findHighestAdditionalFare(lineIds);
		fare += highestAdditionalFare;
		return applyAgeDiscount(fare, userPrincipal.getAge());
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

	private int applyAgeDiscount(int fare, int age) {
		if (age >= YOUTH_AGE_LOWER_BOUND && age < YOUTH_AGE_UPPER_BOUND) {
			return (int) Math.round((fare - AGE_DISCOUNT_DEDUCTION) * YOUTH_DISCOUNT_RATE);
		} else if (age >= CHILD_AGE_LOWER_BOUND && age < CHILD_AGE_UPPER_BOUND) {
			return (int) Math.round((fare - AGE_DISCOUNT_DEDUCTION) * CHILD_DISCOUNT_RATE);
		}
		return fare;
	}

}
