package nextstep.subway.unit.path;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.domain.Line;
import nextstep.subway.path.Path;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

public class PathTest {

	private Station 교대역;
	private Station 강남역;
	private Station 역삼역;
	private Line 이호선;
	private Path path;

	private Station Z_Station;
	private Station Y_Station;
	private Station X_Station;
	private Line ZW_Line;
	private Path Z_Path;

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

		Z_Station = new Station("Z");
		Y_Station = new Station("Y");
		X_Station = new Station("X");

		ZW_Line = new Line("ZW", "black", 1000);
		ZW_Line.addSection(Z_Station, Y_Station, 5, 7);
		ZW_Line.addSection(Y_Station, X_Station, 5, 7);

		Z_Path = new Path(new Sections(ZW_Line.getSections()));
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

	@DisplayName("지하철 이용 요금")
	@Test
	void extractFareTest() {
		assertThat(path.extractFare()).isEqualTo(1350);
	}

	@DisplayName("지하철 추가 이용 요금")
	@Test
	void addLineFareTest() {
		assertThat(Z_Path.extractFare()).isEqualTo(2250);
	}

	@DisplayName("지하철 어린이 이용 요금")
	@Test
	void kidsDiscountFareTest() {
		assertThat(path.discountFare(10)).isEqualTo(500);
	}

	@DisplayName("지하철 청소년 이용 요금")
	@Test
	void youthDiscountFareTest() {
		assertThat(path.discountFare(18)).isEqualTo(800);
	}

	@DisplayName("지하철 성인 이용 요금")
	@Test
	void adultDiscountFareTest() {
		assertThat(path.discountFare(20)).isEqualTo(1350);
	}
}
