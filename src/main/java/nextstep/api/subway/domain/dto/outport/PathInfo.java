package nextstep.api.subway.domain.dto.outport;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextstep.api.subway.domain.model.vo.Path;
import nextstep.common.mapper.ModelMapperBasedObjectMapper;

/**
 * @author : Rene Choi
 * @since : 2024/02/27
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class PathInfo {

	private List<StationInfo> stations;
	private Long distance;
	private Long duration;

	public static PathInfo from(Path path) {
		return ModelMapperBasedObjectMapper.convert(path, PathInfo.class);
	}
}