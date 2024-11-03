package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.subway.domain.fare.Fare;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.utils.SubwayFixture.*;
import static nextstep.subway.utils.SubwayFixture.createSection;
import static org.assertj.core.api.Assertions.*;

@DisplayName("요금 조회기능 단위 테스트")
public class FareTest {
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 석남역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;
    private List<Section> 구간_목록;

    @BeforeEach
    void setUp() {
        교대역 = createStation(1L, "교대역");
        강남역 = createStation(2L, "강남역");
        양재역 = createStation(3L, "양재역");
        남부터미널역 = createStation(4L, "남부터미널역");
        석남역 = createStation(5L, "석남역");

        이호선 = createLine("2호선", "green", 500L, createSection(교대역, 강남역, 10, 8));
        신분당선 = createLine("신분당선", "green", 880L, createSection(강남역, 양재역, 10, 10));
        삼호선 = createLine("3호선", "green", 250L, createSection(교대역, 남부터미널역, 2, 4));
        삼호선.addSection(createSection(남부터미널역, 양재역, 3, 10));

        구간_목록 = List.of(이호선, 신분당선, 삼호선).stream()
                .map(Line::getSections)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @DisplayName("거리 기반 정책 요금 계산")
    @Nested
    class distanceFareTest {
        @DisplayName("10km 이내는 기본 운임만 부과된다.")
        @Test
        void distanceFareCase1() {
            Fare fare = new Fare();
            fare.calculateDistanceBasedFare(9);
            assertThat(fare.getFare()).isEqualTo(1250L);
        }

        @DisplayName("10km 초과 ~ 50km까지 5km마다 100원이 추가 부과된다.")
        @Test
        void distanceFareCase2() {
            Fare fare = new Fare();
            fare.calculateDistanceBasedFare(15);
            assertThat(fare.getFare()).isEqualTo(1350L);

            fare = new Fare();
            fare.calculateDistanceBasedFare(35);
            assertThat(fare.getFare()).isEqualTo(1750L);

            fare = new Fare();
            fare.calculateDistanceBasedFare(50);
            assertThat(fare.getFare()).isEqualTo(2050L);
        }

        @DisplayName("50Km 초과 시 8km마다 100이 추가 부과된다.")
        @Test
        void distanceFareCase3() {
            Fare fare = new Fare();
            fare.calculateDistanceBasedFare(50);
            assertThat(fare.getFare()).isEqualTo(2050L);

            fare = new Fare();
            fare.calculateDistanceBasedFare(55);
            assertThat(fare.getFare()).isEqualTo(2150L);

            fare = new Fare();
            fare.calculateDistanceBasedFare(58);
            assertThat(fare.getFare()).isEqualTo(2150L);

            fare = new Fare();
            fare.calculateDistanceBasedFare(59);
            assertThat(fare.getFare()).isEqualTo(2250L);
        }

        @DisplayName("경로 조회 시 추가 요금이 있는 노선이 포함되는 경우 가장 높은 추가 요금을 가진 노선의 요금만 반영된다.")
        @Test
        void additionalFare() {
            Fare fare = new Fare();
            List<Line> lines = List.of(이호선, 신분당선, 삼호선);
            fare.calculateDistanceBasedFare(35, lines);
            assertThat(fare.getFare()).isEqualTo(2130);
        }

        @DisplayName("경로 조회 시 청소년 로그인 사용자의 경우 350원을 공제한 금액의 20% 할인이 적용된다.")
        @Test
        void teenagerDiscount() {
            Fare fare = new Fare();
            List<Line> lines = List.of(이호선, 신분당선, 삼호선);
            Member member = new Member("testemail@email.com", "testpassword", 13);
            fare.calculateDistanceBasedFare(35, lines, member);
            assertThat(fare.getFare()).isEqualTo(1424);
        }

        @DisplayName("경로 조회 시 청소년 로그인 사용자의 경우 350원을 공제한 금액의 50% 할인이 적용된다.")
        @Test
        void childrenDiscount() {
            Fare fare = new Fare();
            List<Line> lines = List.of(이호선, 신분당선, 삼호선);
            Member member = new Member("testemail@email.com", "testpassword", 12);
            fare.calculateDistanceBasedFare(35, lines, member);
            assertThat(fare.getFare()).isEqualTo(890);
        }
    }
}
