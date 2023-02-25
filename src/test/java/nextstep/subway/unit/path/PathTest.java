package nextstep.subway.unit.path;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

public class PathTest {

	private Station 교대역;
	private Station 강남역;
	private Station 역삼역;
	private Line 이호선;
	private Path path;

	/**
	 *     (di:10,dr:2)   (di:5,dr:1)
	 * 교대역 --- --- 강남역 --- --- 역삼역
	 * */
	@BeforeEach
	void setup() {
		교대역 = new Station("교대역");
		강남역 = new Station("강남역");
		역삼역 = new Station("역삼역");

		이호선 = new Line("2호선", "green");
		이호선.addSection(교대역, 강남역, 10, 2);
		이호선.addSection(강남역, 역삼역, 5, 1);

		path = new Path(new Sections(이호선.getSections()));
	}

	@DisplayName("경로의 총 거리")
	@Test
	void extractDistanceTest() {
		assertThat(path.extractDistance()).isEqualTo(15);
	}

	@DisplayName("경로의 총 소요시간")
	@Test
	void extractDurationTest() {
		assertThat(path.extractDuration()).isEqualTo(3);
	}
}
