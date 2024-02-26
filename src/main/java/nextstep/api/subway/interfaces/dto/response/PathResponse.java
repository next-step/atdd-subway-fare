package nextstep.api.subway.interfaces.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextstep.api.subway.domain.model.vo.Path;
import nextstep.api.subway.util.FareCalculator;
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
	private int fare; // won

	public static PathResponse from(Path path) {
		PathResponse pathResponse = ModelMapperBasedObjectMapper.convert(path, PathResponse.class);
		pathResponse.setFare(FareCalculator.calculateFare(pathResponse.getDistance()));
		return pathResponse;
	}
}
