package nextstep.subway.path;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.applicaion.dto.SearchType;

@Getter
@AllArgsConstructor
public class PathRequest {
	private Long source;
	private Long target;
	private SearchType type;
}
