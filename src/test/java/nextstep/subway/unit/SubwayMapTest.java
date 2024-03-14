package nextstep.subway.unit;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.SubwayMapByDistance;
import nextstep.subway.domain.SubwayMapByDuration;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.Section;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayMapTest {
	private final Line 노선1 = new Line("노선1", "파란색", 1L, 3L, 10, 5);
	private final Line 노선2 = new Line("노선2", "빨간색", 1L, 2L, 5, 5);
	private final Section 구간1 = new Section(노선1, 1L, 3L, 10, 7);
	private final Section 구간2 = new Section(노선2, 1L, 2L, 5, 5);
	private final Section 구간3 = new Section(노선2, 2L, 3L, 4, 4);
	private SubwayMap subwayMap;

	@Test
	@DisplayName("최단 거리 경로 조회")
	void getShortestPath() {
		subwayMap = new SubwayMapByDistance(List.of(구간1, 구간2, 구간3));
		Path path = subwayMap.getShortesPath(1L, 3L);
		assertThat(path.getStations()).containsExactly(1L, 2L, 3L);
		assertThat(path.getDistance()).isEqualTo(9);
		assertThat(path.getDuration()).isEqualTo(9);
	}

	@Test
	@DisplayName("최단 시간 경로 조회")
	void getDistance() {
		subwayMap = new SubwayMapByDuration(List.of(구간1, 구간2, 구간3));
		Path path = subwayMap.getShortesPath(1L, 3L);
		assertThat(path.getStations()).containsExactly(1L, 3L);
		assertThat(path.getDistance()).isEqualTo(10);
		assertThat(path.getDuration()).isEqualTo(7);
	}
}
