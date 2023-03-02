package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.fare.FarePolicyManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static nextstep.subway.domain.fare.Fare.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;


public class FareTest {

    private FarePolicyManager fareManager;

    @BeforeEach
    void setUp() {
        fareManager = new FarePolicyManager();
    }

    @DisplayName("10Km 이내 거리는 기본 요금을 받는다.")
    @ValueSource(ints = {1, 10})
    @ParameterizedTest(name = "거리: {0}Km")
    void defaultFare(int distance) {
        Path path = getFixturePathWithDistance(distance);
        int cost = fareManager.calculate(path, getMemberWithAge(DEFAULT_FARE));

        assertThat(cost).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("10Km 초과 시 추가운임이 부과된다.")
    @CsvSource({"12,1350", "16,1450", "30,1_650"})
    @ParameterizedTest(name = "거리: {0}Km")
    void extra10KmFare(int distance, int expected) {
        Path path = getFixturePathWithDistance(distance);
        int cost = fareManager.calculate(path, getMemberWithAge(expected));

        assertThat(cost).isEqualTo(expected);
    }

    @DisplayName("50Km 초과 시 추가운임이 부과된다.")
    @CsvSource({"66,2250", "75,2450"})
    @ParameterizedTest(name = "거리: {0}Km")
    void extra50KmFare(int distance, int expected) {
        Path path = getFixturePathWithDistance(distance);
        int cost = fareManager.calculate(path, getMemberWithAge(expected));

        assertThat(cost).isEqualTo(expected);
    }

    @DisplayName("추가 요금이 있는 노선인 경우 노선 추가 요금이 부과된다.")
    @Test
    void additionalFare() {
        Line 이호선 = createLine("2호선", 500);
        Line 삼호선 = createLine("3호선", 1000);
        Station 역삼역 = createStation("역삼역");
        Station 강남역 = createStation("강남역");
        Station 교대역 = createStation("교대역");
        Station 남부터미널역 = createStation("남부터미널역");
        Section 역삼역_강남역 = createSection(역삼역, 강남역, 10);
        Section 강남역_교대역 = createSection(강남역, 교대역, 10);
        Section 교대역_남부터미널역 = createSection(교대역, 남부터미널역, 10);
        Sections sections = new Sections(List.of(역삼역_강남역, 강남역_교대역, 교대역_남부터미널역));
        이호선.addSection(역삼역_강남역);
        이호선.addSection(강남역_교대역);
        삼호선.addSection(교대역_남부터미널역);

        Path path = new Path(sections);
        int cost = fareManager.calculate(path, Member.guest());

        assertThat(cost).isEqualTo(1_650 + 1_000);
    }

    @DisplayName("나이별 할인 정책을 적용할 수 있다.")
    @MethodSource
    @ParameterizedTest(name = "기본 요금(" + DEFAULT_FARE + "원)에서 {0}세 할인을 받으면 {1}원")
    void applyDiscount(int age, int expected) {
        Path path = getFixturePathWithDistance(10);

        int cost = fareManager.calculate(path, getMemberWithAge(age));

        assertThat(cost).isEqualTo(expected);
    }

    static Stream<Arguments> applyDiscount() {
        return Stream.of(
                arguments(6, 800),
                arguments(12, 800),
                arguments(18, 1_070),
                arguments(19, 1_250)
        );
    }

    private Path getFixturePathWithDistance(int distance) {
        Line 이호선 = createLine("2호선", 0);
        Station 역삼역 = createStation("역삼역");
        Station 강남역 = createStation("강남역");
        Section 역삼역_강남역 = createSection(역삼역, 강남역, distance);
        이호선.addSection(역삼역_강남역);

        Path path = new Path(new Sections(List.of(역삼역_강남역)));
        return path;
    }

    private Member getMemberWithAge(int age) {
        return new Member("test", "test", age);
    }

    private Station createStation(String name) {
        return new Station(name);
    }

    private Section createSection(Station upStation, Station downStation, int distance) {
        return Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }

    private Line createLine(String name, int additionalFare) {
        return new Line(name, "", additionalFare);
    }
}
