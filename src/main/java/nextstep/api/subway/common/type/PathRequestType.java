package nextstep.api.subway.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/02/24
 */
@Getter
@RequiredArgsConstructor
public enum PathRequestType {
	DISTANCE("거리"),
	DURATION("소요 시간");

	private final String description;

	public static boolean isDistance(PathRequestType type) {
		return type == DISTANCE;
	}
}
