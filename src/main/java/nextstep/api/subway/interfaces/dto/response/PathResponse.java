package nextstep.api.subway.interfaces.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextstep.api.subway.domain.dto.outport.FareInfo;
import nextstep.api.subway.domain.dto.outport.PathInfo;
import nextstep.common.mapper.ModelMapperBasedObjectMapper;

/**
 * @author : Rene Choi
 * @since : 2024/02/07
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class PathResponse {
	private List<StationResponse> stations;
	private Long distance;
	private Long duration;
	private int fareAmount; // won


	public static PathResponse of(PathInfo pathInfo, FareInfo fareInfo) {
		PathResponse pathResponse = ModelMapperBasedObjectMapper.convert(pathInfo, PathResponse.class);
		pathResponse.setFareAmount(fareInfo.getFareAmount());
		return pathResponse;
	}
}
