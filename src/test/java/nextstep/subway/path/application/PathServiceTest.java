package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@SpringBootTest
@Transactional
class PathServiceTest {

	@Autowired
	private PathService pathService;

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private LineRepository lineRepository;

	@Autowired
	private MemberRepository memberRepository;

	private Station 강남역;
	private Station 역삼역;
	private Station 남부터미널역;
	private Station 양재역;
	private Station 교대역;
	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;

	@BeforeEach
	void setUp() {
		강남역 = stationRepository.save(new Station("강남역"));
		역삼역 = stationRepository.save(new Station("역삼역"));
		남부터미널역 = stationRepository.save(new Station("남부터미널역"));
		양재역 = stationRepository.save(new Station("양재역"));
		교대역 = stationRepository.save(new Station("교대역"));

		신분당선 = lineRepository.save(new Line("신분당선", "red", 500));
		신분당선.addSection(강남역, 양재역, 10, 10);
		이호선 = lineRepository.save(new Line("이호선", "green", 0));
		이호선.addSection(교대역, 강남역, 10, 10);
		삼호선 = lineRepository.save(new Line("삼호선", "yellow", 900));
		삼호선.addSection(교대역, 남부터미널역, 2, 10);
		삼호선.addSection(남부터미널역, 양재역, 3, 10);
	}

	@DisplayName("최단 경로 찾기")
	@Test
	void findPath() {
		final Member member = memberRepository.save(new Member("test@test.com", "123", 20));
		// when
		PathResponse pathResponse = pathService.findPath(LoginMember.of(member), 양재역.getId(), 교대역.getId(), PathType.DISTANCE);

		// then
		assertThat(pathResponse.getDistance()).isEqualTo(5);
		assertThat(pathResponse.getDuration()).isEqualTo(20);
		assertThat(pathResponse.getFare()).isEqualTo(2150);
	}
}