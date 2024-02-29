package nextstep.api.subway.domain.model.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Value;
import nextstep.api.subway.domain.model.entity.Line;
import nextstep.api.subway.domain.model.entity.Station;

/**
 * @author : Rene Choi
 * @since : 2024/02/09
 */
@Value
@AllArgsConstructor(staticName = "of")
public class Path {
	List<Station> stations;
	List<Line> lines;

	Long distance;

	Long duration;

	public int size() {
		return stations.size();
	}

}
