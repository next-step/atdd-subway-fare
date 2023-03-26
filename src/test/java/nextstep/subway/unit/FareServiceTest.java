package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import nextstep.member.domain.Guest;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.FareService;
import nextstep.subway.domain.Fare.Fare;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.ErrorMessage;
import nextstep.subway.exception.NegativeDistanceException;

@SpringBootTest
@Transactional
class FareServiceTest {
	private static final int DEFAULT_DURATION = 0;
	private static final String DEFAULT_PASSWORD = "defaultPassword";
	private final LoginMember 게스트 = Guest.getInstance();
	@Autowired
	private FareService fareService;
	@Autowired
	private MemberRepository memberRepository;

	@DisplayName("경로 10KM 이내일 경우, 요금을 계산한다.")
	@CsvSource({"1,1_250", "9,1_250", "10,1_250"})
	@ParameterizedTest
	void calculateFare_WITH_IN_10_KM(int distance, int cost) {
		// Given
		Sections sections = getSectionsFixtureStepByDistance(distance);

		// When
		Fare fare = fareService.calculateTotalFare(게스트, sections);

		// Then
		assertThat(fare.getValue()).isEqualTo(cost);
	}

	@DisplayName("경로 10KM 초과 50KM 이내일 경우, 요금을 계산한다.")
	@CsvSource({"11, 1_350", "16, 1_450", "21, 1_550", "26, 1_650", "31, 1_750", "36, 1_850", "41, 1_950", "46, 2_050",
		"49, 2_050", "50, 2_050"})
	@ParameterizedTest
	void calculateFare_EXCEEDING_10_KM_AND_LESS_THAN_50_KM(int distance, int cost) {
		// Given
		Sections sections = getSectionsFixtureStepByDistance(distance);

		// When
		Fare fare = fareService.calculateTotalFare(게스트, sections);

		// Then
		assertThat(fare.getValue()).isEqualTo(cost);
	}

	@DisplayName("경로 50KM 초과일 경우, 요금을 계산한다.")
	@CsvSource({"51, 2_150", "57, 2_150", "58, 2_250", "59, 2_250"})
	@ParameterizedTest
	void calculateFare_EXCEEDING_50_KM(int distance, int cost) {
		// Given
		Sections sections = getSectionsFixtureStepByDistance(distance);

		// When
		Fare fare = fareService.calculateTotalFare(게스트, sections);

		// Then
		assertThat(fare.getValue()).isEqualTo(cost);
	}

	@DisplayName("거리가 양수가 아니면 예외를 던진다.")
	@ValueSource(ints = {0, -1})
	@ParameterizedTest
	void WHEN_DISTANCE_IS_NEGATIVE_THEN_THROW_EXCEPTION(int distance) {
		// Given
		Sections sections = getSectionsFixtureStepByDistance(distance);

		// When & Then
		assertThatThrownBy(() -> fareService.calculateTotalFare(게스트, sections))
			.isInstanceOf(NegativeDistanceException.class)
			.hasMessage(ErrorMessage.SHOULD_BE_POSITIVE.getMessage());
	}

	@DisplayName("노선의 요금이 있을 경우, 요금을 계산한다.")
	@CsvSource({"51, 800, 2_950", "57, 500, 2_650", "58, 300, 2_550", "59, 0, 2_250"})
	@ParameterizedTest
	void calculateFare_LINE_FARE_IS_EXISTED(int distance, int lineFare, int cost) {
		// Given
		Sections sections = getSectionsFixtureStepByDistanceAndLineFare(distance, lineFare);

		// When
		Fare fare = fareService.calculateTotalFare(게스트, sections);

		// Then
		assertThat(fare.getValue()).isEqualTo(cost);
	}

	@DisplayName("사용자의 경로 요청일 경우, 나이 정책을 적용한 요금을 계산한다.")
	@CsvSource({"51, 800, 6, 1_650", "57, 500, 12, 1_500", "58, 300, 13, 2_110", "59, 0, 18, 1_870"})
	@ParameterizedTest
	void calculateFare_APPLY_AGE_POLICY(int distance, int lineFare, int age, int cost) {
		// Given
		Sections sections = getSectionsFixtureStepByDistanceAndLineFare(distance, lineFare);
		LoginMember user = getLoginMemberFixtureStepByAge(age);

		// When
		Fare fare = fareService.calculateTotalFare(user, sections);

		// Then
		assertThat(fare.getValue()).isEqualTo(cost);
	}

	Sections getSectionsFixtureStepByDistance(int distance) {
		Station 강남역 = new Station("강남역");
		Station 역삼역 = new Station("역삼역");
		Line line = new Line("1호선", "blue", 0);

		line.addSection(강남역, 역삼역, distance, DEFAULT_DURATION);

		return new Sections(line.getSections());
	}

	Sections getSectionsFixtureStepByDistanceAndLineFare(int distance, int lineFare) {
		Station 강남역 = new Station("강남역");
		Station 역삼역 = new Station("역삼역");
		Line line = new Line("1호선", "blue", lineFare);

		line.addSection(강남역, 역삼역, distance, DEFAULT_DURATION);

		return new Sections(line.getSections());
	}

	LoginMember getLoginMemberFixtureStepByAge(int age) {
		Member member = memberRepository.save(new Member(generateNewEmail(), DEFAULT_PASSWORD, age));
		return new LoginMember(member.getId(), member.getRoles());
	}

	public String generateNewEmail() {
		return UUID.randomUUID().toString() + "@email.com";
	}
}
