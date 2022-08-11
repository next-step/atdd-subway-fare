package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static nextstep.subway.domain.PathType.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Path 관련 테스트를 진행한다.")
class PathTest {

	private static final int 신분당선_추가요금 = 900;
	private static final int 이호선_추가요금 = 450;
	public static final int 삼호선_추가요금 = 500;

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

		신분당선 = new Line("신분당선", "red", 신분당선_추가요금);
		이호선 = new Line("2호선", "red", 이호선_추가요금);
		삼호선 = new Line("3호선", "red", 삼호선_추가요금);

		신분당선.addSection(강남역, 양재역, 3, 2);
		이호선.addSection(교대역, 강남역, 3, 2);
		삼호선.addSection(교대역, 남부터미널역, 5, 3);
		삼호선.addSection(남부터미널역, 양재역, 5, 3);
	}

	@Test
	void 총_거리가_10km_이하_일_때_요금을_반환한다() {
		assertThat(Fare.calculate(10)).isEqualTo(1250);
	}

	@Test
	void 총_거리가_50km_이하_일_때_요금을_반환한다() {
		assertThat(Fare.calculate(50)).isEqualTo(2050);
	}

	@Test
	void 총_거리가_51km_일_때_요금을_반환한다() {
		assertThat(Fare.calculate(51)).isEqualTo(2150);
	}

	@Test
	void 총_거리가_50km_초과_일_때_요금을_반환한다() {
		assertThat(Fare.calculate(100)).isEqualTo(2750);
	}

	@Test
	void 추가_요금이_있는_노선을_이용하면_추가_요금이_반환된다() {
	    //given
		SubwayMap subwayMap = new SubwayMap(List.of(이호선));

	    //when
		Path path = subwayMap.findPath(교대역, 강남역, DISTANCE);

		//then
		assertThat(path.calculateFare()).isEqualTo(Fare.BASIC_FARE + 이호선_추가요금);
	}

	@Test
	void 추가_요금이_있는_노선들을_이용하면_추가_요금중_최대값이_반환된다() {
	    //given
		SubwayMap subwayMap = new SubwayMap(List.of(이호선, 삼호선, 신분당선));

	    //when
		Path path = subwayMap.findPath(교대역, 양재역, DISTANCE);

		//then
		assertThat(path.calculateFare()).isEqualTo(Fare.BASIC_FARE + 신분당선_추가요금);
	}

	private Station createStation(long id, String name) {
		Station station = new Station(name);
		ReflectionTestUtils.setField(station, "id", id);

		return station;
	}
}