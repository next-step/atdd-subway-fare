package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.SubwayMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.domain.PathType.DISTANCE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Path 관련 테스트를 진행한다.")
class PathTest extends StationData {

	public static final int DEFALUT_AGE = 20;
	private static final int 신분당선_추가요금 = 900;
	private static final int 이호선_추가요금 = 450;
	public static final int 삼호선_추가요금 = 500;

	@BeforeEach
	@Override
	void setUp() {
		super.setUp();

		신분당선 = new Line("신분당선", "red", 신분당선_추가요금);
		이호선 = new Line("2호선", "red", 이호선_추가요금);
		삼호선 = new Line("3호선", "red", 삼호선_추가요금);

		신분당선.addSection(강남역, 양재역, 3, 2);
		이호선.addSection(교대역, 강남역, 3, 2);
		삼호선.addSection(교대역, 남부터미널역, 5, 3);
		삼호선.addSection(남부터미널역, 양재역, 5, 3);
	}

	@Test
	void 추가_요금이_있는_노선을_이용하면_추가_요금이_반환된다() {
	    //given
		SubwayMap subwayMap = new SubwayMap(List.of(이호선));

	    //when
		Path path = subwayMap.findPath(교대역, 강남역, DISTANCE);

		//then
		assertThat(path.calculateFare(DEFALUT_AGE)).isEqualTo(Fare.BASIC_FARE + 이호선_추가요금);
	}

	@Test
	void 추가_요금이_있는_노선들을_이용하면_추가_요금중_최대값이_반환된다() {
	    //given
		SubwayMap subwayMap = new SubwayMap(List.of(이호선, 삼호선, 신분당선));

	    //when
		Path path = subwayMap.findPath(교대역, 양재역, DISTANCE);

		//then
		assertThat(path.calculateFare(DEFALUT_AGE)).isEqualTo(Fare.BASIC_FARE + 신분당선_추가요금);
	}
}