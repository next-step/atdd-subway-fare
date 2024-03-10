package nextstep.subway.unit;

import nextstep.subway.domain.PathByDistanceFinder;
import nextstep.subway.domain.PathByDurationFinder;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.Section;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathByDurationFinderTest {
	private static final Long 첫번째_역 = 1L;
	private static final Long 두번째_역 = 2L;
	private static final Long 세번째_역 = 3L;
	private PathFinder pathFinder;

	@BeforeEach
	void setup() {
		Line 노선1 = new Line("노선1", "파란색", 첫번째_역, 세번째_역, 10, 5);
		Line 노선2 = new Line("노선2", "빨간색", 첫번째_역, 두번째_역, 5, 5);

		pathFinder = new PathByDurationFinder(List.of(
				new Section(노선1, 첫번째_역, 세번째_역, 10, 10)
				,new Section(노선2, 첫번째_역, 두번째_역, 5, 3)
				,new Section(노선2, 두번째_역, 세번째_역, 4, 4)
		));
	}
	@Test
	@DisplayName("경로 조회")
	void getPath() {
		assertThat(pathFinder.getPath(첫번째_역, 세번째_역)).containsExactly(첫번째_역, 두번째_역, 세번째_역);
	}

	@Test
	@DisplayName("최단 거리 조회")
	void getDistance() {
		assertThat(pathFinder.getWieght(첫번째_역, 세번째_역)).isEqualTo(7);
	}
}
