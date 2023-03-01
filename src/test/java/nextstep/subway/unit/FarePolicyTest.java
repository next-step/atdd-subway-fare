package nextstep.subway.unit;

import nextstep.member.domain.LoginMember;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.fare.FarePolicy;
import nextstep.subway.utils.ApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Transactional
public class FarePolicyTest extends ApplicationTest {
    private static final int CHEAP_FARE = 900;
    private static final int EXPENSIVE_FARE = 901;

    private static final String CHILD_EMAIL = "child6@email.com";
    private static final String TEENAGER_EMAIL = "teenager13@email.com";
    private static final String ADULT_EMAIL = "adult19@email.com";

    @Autowired
    private FarePolicy farePolicy;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    private Path path;

    private Line lineA;
    private Line lineB;

    private Station stationA;
    private Station stationB;
    private Station stationC;



    @BeforeEach
    public void setUp() {
        super.setUp();

        lineA = lineRepository.save(new Line("lineA", "lineA-color", CHEAP_FARE));
        lineB = lineRepository.save(new Line("lineB", "lineB-color", EXPENSIVE_FARE));

        stationA = stationRepository.save(new Station("stationA"));
        stationB = stationRepository.save(new Station("stationB"));
        stationC = stationRepository.save(new Station("stationC"));

        lineA.addSection(stationA, stationB, 5, 5);
        lineB.addSection(stationB, stationC, 10, 10);

        path = new SubwayMap(lineRepository.findAll()).findPath(stationA, stationC, PathType.DISTANCE);
    }

    @DisplayName("로그인 한 유저는 거리, 노선, 연령 할인 정책을 통해 요금이 책정된다.")
    @MethodSource("fareProvider")
    @ParameterizedTest(name = "사용자 유형: {2}, 요금: {1}")
    void getFare(String email, int expected, String ageType) {
        final Long id = memberRepository.findByEmail(email).get().getId();
        final LoginMember loginMember = new LoginMember(id, Collections.emptyList());

        final int fare = farePolicy.getFare(path, loginMember);

        assertThat(fare).isEqualTo(expected);
    }

    private static Stream<Arguments> fareProvider() {
        return Stream.of(
                Arguments.of(CHILD_EMAIL, 950, "어린이"),
                Arguments.of(TEENAGER_EMAIL, 1520, "청소년"),
                Arguments.of(ADULT_EMAIL, 2251, "성인")
        );
    }
}
