package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import nextstep.subway.domain.FareCalculator;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

public class FareCalculatorTest {
	private final int 기본_운임_비용 = 1250;

	private final int 팔키로 = 8;
	private final int 십이키로 = 12;
	private final int 오십구키로 = 59;

	private final int 추가요금_300 = 300;
	private final int 추가요금_900 = 900;

	private Station 교대역;
	private Station 강남역;
	private Station 양재역;
	private Station 남부터미널역;
	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;

	@BeforeEach
	void setUp() {
		교대역 = createStation(1L, "교대역");
		강남역 = createStation(2L, "강남역");
		양재역 = createStation(3L, "양재역");
		남부터미널역 = createStation(4L, "남부터미널역");

		신분당선 = new Line("신분당선", "red", 추가요금_300);
		이호선 = new Line("2호선", "red", 추가요금_900);
	}

	@DisplayName("거리 <= 10km 운임비용")
	@Test
	void 십키로_이하_거리_요금() {
		// when
		int 운임비용 = FareCalculator.calculate(팔키로);

		// then
		assertThat(운임비용).isEqualTo(기본_운임_비용);
	}

	@DisplayName("10km < 거리 <= 50km 운임비용")
	@Test
	void 십키로_초과_오십키로_이하_거리_요금() {
		// when
		int 운임비용 = FareCalculator.calculate(십이키로);

		// then
		assertThat(운임비용).isEqualTo(기본_운임_비용 + 100);
	}

	@DisplayName("50km < 거리 운임비용")
	@Test
	void 오십키로_초과_거리_요금() {
		// when
		int 운임비용 = FareCalculator.calculate(오십구키로);

		// then
		assertThat(운임비용).isEqualTo(기본_운임_비용 + 1000);
	}

	private Station createStation(long id, String name) {
		Station station = new Station(name);
		ReflectionTestUtils.setField(station, "id", id);

		return station;
	}
}
