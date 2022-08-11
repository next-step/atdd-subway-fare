package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

public class UnitTestData {

	public static Station 교대역;
	public static Station 강남역;
	public static Station 양재역;
	public static Station 남부터미널역;
	public static Line 신분당선;
	public static Line 이호선;
	public static Line 삼호선;

	@BeforeEach
	void setUp() {
		교대역 = createStation(1L, "교대역");
		강남역 = createStation(2L, "강남역");
		양재역 = createStation(3L, "양재역");
		남부터미널역 = createStation(4L, "남부터미널역");

		신분당선 = new Line("신분당선", "red");
		이호선 = new Line("2호선", "red");
		삼호선 = new Line("3호선", "red");
	}

	public static Station createStation(long id, String name) {
		Station station = new Station(name);
		ReflectionTestUtils.setField(station, "id", id);

		return station;
	}

	public static void 삼호선에_남부터미널역_양재역_구간을_추가한다(int distance, int duration) {
		삼호선.addSection(남부터미널역, 양재역, distance, duration);
	}

	public static void 삼호선에_교대역_남부터미널역_구간을_추가한다(int distance, int duration) {
		삼호선.addSection(교대역, 남부터미널역, distance, duration);
	}

	public static void 이호선에_교대역_강남역_구간을_추가한다(int distance, int duration) {
		이호선.addSection(교대역, 강남역, distance, duration);
	}

	public static void 신분당선에_강남역_양재역_구간을_추가한다(int distance, int duration) {
		신분당선.addSection(강남역, 양재역, distance, duration);
	}
}
