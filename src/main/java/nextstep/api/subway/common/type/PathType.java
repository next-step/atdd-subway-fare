package nextstep.api.subway.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/02/24
 */
@Getter
@RequiredArgsConstructor
public enum PathType {
	DISTANCE("거리"),
	DURATION("소요 시간");

	private final String description;

	public static boolean isDistance(PathType type) {
		return type!=null && DISTANCE.name().equals(type.name());
	}
}
