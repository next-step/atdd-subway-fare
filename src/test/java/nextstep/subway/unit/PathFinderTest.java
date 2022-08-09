package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.PathDistanceFinder;
import nextstep.subway.domain.path.PathDurationFinder;
import nextstep.subway.domain.path.PathFinder;

public class PathFinderTest {

	private Station 교대역;
	private Station 강남역;
	private Station 양재역;
	private Station 남부터미널역;
	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;

	@BeforeEach
	void setUp() {
		/**
		 * 교대역    --- *2호선* ---   강남역
		 * |                        |
		 * *3호선*                   *신분당선*
		 * |                        |
		 * 남부터미널역  --- *3호선* ---   양재
		 */
		교대역 = createStation(1L, "교대역");
		강남역 = createStation(2L, "강남역");
		양재역 = createStation(3L, "양재역");
		남부터미널역 = createStation(4L, "남부터미널역");

		신분당선 = new Line("신분당선", "red");
		이호선 = new Line("2호선", "red");
		삼호선 = new Line("3호선", "red");

		신분당선.addSection(강남역, 양재역, 3, 10);
		이호선.addSection(교대역, 강남역, 3, 10);
		삼호선.addSection(교대역, 남부터미널역, 5, 4);
		삼호선.addSection(남부터미널역, 양재역, 5, 1);
	}

	@Test
	void findPathBaseOnDistance() {
		// given
		List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
		PathFinder pathFinder = new PathDistanceFinder(lines);

		// when
		Path path = pathFinder.findPath(교대역, 양재역);

		// then
		assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 양재역));
	}

	@Test
	void findPathOppositelyBaseOnDistance() {
		// given
		List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
		PathFinder pathFinder = new PathDistanceFinder(lines);

		// when
		Path path = pathFinder.findPath(양재역, 교대역);

		// then
		assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 강남역, 교대역));
	}

	@Test
	void findPathBaseOnDuration() {
		// given
		List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
		PathFinder pathFinder = new PathDurationFinder(lines);

		// when
		Path path = pathFinder.findPath(교대역, 양재역);

		// then
		assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(교대역, 남부터미널역, 양재역));
	}

	@Test
	void findPathOppositelyBaseOnDuration() {
		// given
		List<Line> lines = Lists.newArrayList(신분당선, 이호선, 삼호선);
		PathFinder pathFinder = new PathDurationFinder(lines);

		// when
		Path path = pathFinder.findPath(양재역, 교대역);

		// then
		assertThat(path.getStations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 남부터미널역, 교대역));
	}

	private Station createStation(long id, String name) {
		Station station = new Station(name);
		ReflectionTestUtils.setField(station, "id", id);

		return station;
	}
}
