package nextstep.subway.unit;

import nextstep.member.domain.LoginMember;
import nextstep.member.domain.RoleType;
import nextstep.subway.applicaion.dto.PathType;
import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayMapTest {

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 선정릉역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 구호선;

    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");
        선정릉역 = createStation(5L, "선정릉역");

        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("2호선", "green");
        삼호선 = new Line("3호선", "orange");
        구호선 = new Line("9호선", "brown", 500);

        신분당선.addSection(강남역, 양재역, 20, 4);
        이호선.addSection(교대역, 강남역, 15, 3);
        삼호선.addSection(교대역, 남부터미널역, 8, 3);
        삼호선.addSection(남부터미널역, 양재역, 20, 5);
        구호선.addSection(양재역, 선정릉역, 10, 3);
    }

    @Test
    void findPathDistance() {
        // given
        List<Line> lines = List.of(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, PathType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(List.of(교대역, 남부터미널역, 양재역));
        assertThat(path.getFare()).isEqualTo(1650);
    }

    @Test
    void findPathDistanceOppositely() {
        // given
        List<Line> lines = List.of(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(양재역, 교대역, PathType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(List.of(양재역, 남부터미널역, 교대역));
        assertThat(path.getFare()).isEqualTo(1650);
    }

    @Test
    void findPathDuration() {
        // given
        List<Line> lines = List.of(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 양재역, PathType.DURATION);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(List.of(교대역, 강남역, 양재역));
        assertThat(path.getFare()).isEqualTo(1650);
    }

    @Test
    void findPathDurationOppositely() {
        // given
        List<Line> lines = List.of(신분당선, 이호선, 삼호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(양재역, 교대역, PathType.DURATION);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(List.of(양재역, 강남역, 교대역));
        assertThat(path.getFare()).isEqualTo(1650);
    }

    @Test
    @DisplayName("추가 요금이 있는 노선 이용")
    void addFareLine() {
        // given
        List<Line> lines = List.of(신분당선, 이호선, 삼호선, 구호선);
        SubwayMap subwayMap = new SubwayMap(lines);

        // when
        Path path = subwayMap.findPath(교대역, 선정릉역, PathType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(List.of(교대역, 남부터미널역, 양재역, 선정릉역));
        assertThat(path.getFare()).isEqualTo(2350);
    }

    @Test
    @DisplayName("비로그인 요금 할인")
    void anonymousDiscount() {
        // given
        final DiscountPolicy discountPolicy = new AgeDiscountPolicy(new LoginMember(-1L, 0, List.of(RoleType.ROLE_ANONYMOUS.name())));
        List<Line> lines = List.of(신분당선, 이호선, 삼호선, 구호선);
        SubwayMap subwayMap = new SubwayMap(lines, discountPolicy);

        // when
        Path path = subwayMap.findPath(교대역, 선정릉역, PathType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(List.of(교대역, 남부터미널역, 양재역, 선정릉역));
        assertThat(path.getFare()).isEqualTo(2350);
    }

    @Test
    @DisplayName("요금 할인 없음")
    void noneDiscount() {
        // given
        final DiscountPolicy discountPolicy = new AgeDiscountPolicy(new LoginMember(-1L, 20, List.of(RoleType.ROLE_MEMBER.name())));
        List<Line> lines = List.of(신분당선, 이호선, 삼호선, 구호선);
        SubwayMap subwayMap = new SubwayMap(lines, discountPolicy);

        // when
        Path path = subwayMap.findPath(교대역, 선정릉역, PathType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(List.of(교대역, 남부터미널역, 양재역, 선정릉역));
        assertThat(path.getFare()).isEqualTo(2350);
    }
    @Test
    @DisplayName("청소년 요금 할인")
    void teenagerDiscount() {
        // given
        final DiscountPolicy discountPolicy = new AgeDiscountPolicy(new LoginMember(-1L, 13, List.of(RoleType.ROLE_MEMBER.name())));
        List<Line> lines = List.of(신분당선, 이호선, 삼호선, 구호선);
        SubwayMap subwayMap = new SubwayMap(lines, discountPolicy);

        // when
        Path path = subwayMap.findPath(교대역, 선정릉역, PathType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(List.of(교대역, 남부터미널역, 양재역, 선정릉역));
        assertThat(path.getFare()).isEqualTo(1950);
    }

    @Test
    @DisplayName("어린이 요금 할인")
    void childDiscount() {
        // given
        final DiscountPolicy discountPolicy = new AgeDiscountPolicy(new LoginMember(-1L, 6, List.of(RoleType.ROLE_MEMBER.name())));
        List<Line> lines = List.of(신분당선, 이호선, 삼호선, 구호선);
        SubwayMap subwayMap = new SubwayMap(lines, discountPolicy);

        // when
        Path path = subwayMap.findPath(교대역, 선정릉역, PathType.DISTANCE);

        // then
        assertThat(path.getStations()).containsExactlyElementsOf(List.of(교대역, 남부터미널역, 양재역, 선정릉역));
        assertThat(path.getFare()).isEqualTo(1350);
    }
    private Station createStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }
}
