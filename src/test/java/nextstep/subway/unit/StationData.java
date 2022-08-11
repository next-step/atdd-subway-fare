package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

public class StationData {
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
	}

	public static Station createStation(long id, String name) {
		Station station = new Station(name);
		ReflectionTestUtils.setField(station, "id", id);

		return station;
	}
}
