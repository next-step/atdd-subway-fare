package nextstep.subway.unit;

import nextstep.auth.domain.UserDetail;
import nextstep.subway.domain.*;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.Section;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FareCalculatorTest {
    private PathFinder subwayMap;
    private FareCalculator fareCalculator;

    @BeforeEach
    void setup() {
        Line 노선1 = new Line("노선1", "파란색", 1L, 2L, 10, 5);
        Line 노선2 = new Line("노선2", "빨간색", 3L, 4L, 20, 5);
        Line 노선3 = new Line("노선3", "노란색", 6L, 7L, 20, 5, 900);
        Line 노선4 = new Line("노선4", "주황색", 7L, 8L, 5, 5, 400);

        subwayMap = new SubwayMapByDistance(List.of(
                new Section(노선1, 1L, 2L, 9, 5),
                new Section(노선1, 2L, 3L, 7, 10),
                new Section(노선2, 3L, 4L, 20, 5),
                new Section(노선2, 4L, 5L, 14, 5),
                new Section(노선2, 5L, 6L, 9, 9),
                new Section(노선3, 6L, 7L, 20, 5),
                new Section(노선4, 7L, 8L, 5, 5)
        ));
    }

    /**
     * 10km까지 - 기본운임 1250원
     */
    @Test
    void calculateFare10Km() {
        Path path = subwayMap.getShortestPath(1L, 2L);
        fareCalculator = new FareCalculator(path);
        assertThat(fareCalculator.getFare()).isEqualTo(1250);
    }

    /**
     * 10km초과 ∼ 50km까지 (5km마다 100원)
     * 16km = 10km(1250원) + 5km(100원) + 1km(100원) - 1450원
     */
    @Test
    void calculateFareOver10Km() {
        Path path = subwayMap.getShortestPath(1L, 3L);
        fareCalculator = new FareCalculator(path);
        assertThat(fareCalculator.getFare()).isEqualTo(1450);
    }

    /**
     * 10km초과∼50km까지(5km마다 100원)
     * 50km = 10km(1250원) + 5km * 8 (800원) - 2050원
     */
    @Test
    void calculateFare50Km() {
        Path path = subwayMap.getShortestPath(1L, 5L);
        fareCalculator = new FareCalculator(path);
        assertThat(fareCalculator.getFare()).isEqualTo(2050);
    }

    /**
     * 50km초과 시 (8km마다 100원)
     * 59km = 10km(1250원) + 5km * 8 (800원) + 8km (100원) + 1km (100원) - 2250원
     */
    @Test
    void calculateFareOver50Km() {
        Path path = subwayMap.getShortestPath(1L, 6L);
        fareCalculator = new FareCalculator(path);
        assertThat(fareCalculator.getFare()).isEqualTo(2250);
    }

    /**
     * 역5 ---노선2(9)--- 역6 ---노선3(20)--- 역7
     * 거리 요금 정책 : 29km = 10km(1250원) + 5km(100원) * 3 + 4km(100원) - 1650원
     * 노선 요금 정책 : 노선3 추가 요금 900원
     * 1650원 + 900원 -> 2550원
     */
    @Test
    @DisplayName("추가 요금이 있는 노선이 포함된 요금 계산")
    void whenLineHasAdditionalFeeThenCalculateFare() {
        Path path = subwayMap.getShortestPath(5L, 7L);
        fareCalculator = new FareCalculator(path);
        assertThat(fareCalculator.getFare()).isEqualTo(2550);
    }

    /**
     * 역6 ---노선3(20)--- 역7 ---노선4(5)--- 역8
     * 거리 요금 정책 : 25km = 10km(1250원) + 5km(100원) * 3 - 1550원
     * 노선 요금 정책 : 노선3 추가 요금 900원 / 노선4 추가 요금 -> 900원
     * 1550원 + 900원 -> 2550원
     */
    @Test
    @DisplayName("추가 요금이 있는 노선이 포함된 요금 계산 시, 가장 높은 금액의 추가 요금을 적용한다.")
    void whenLineHasAdditionalFeeThenCalculateFareWithMaxFee() {
        Path path = subwayMap.getShortestPath(6L, 8L);
        fareCalculator = new FareCalculator(path);
        assertThat(fareCalculator.getFare()).isEqualTo(2450);
    }

    @Test
    @DisplayName("사용자가 어린이면 운임에서 350원을 공제한 금액의 50%할인이 적용된다.")
    void whenUserIsChildThenFareIsDiscounted() {
        Path path = subwayMap.getShortestPath(6L, 8L);
        UserDetail userDetail = new UserDetail("email@email.com", "password", 12);
        fareCalculator = new FareCalculator(path, userDetail);
        assertThat(fareCalculator.getFare()).isEqualTo(1050);
    }

    @Test
    @DisplayName("사용자가 청소년이면 운임에서 350원을 공제한 금액의 20%할인이 적용된다.")
    void whenUserIsTeenagerThenFareIsDiscounted() {
        Path path = subwayMap.getShortestPath(6L, 8L);
        UserDetail userDetail = new UserDetail("email@email.com", "password", 18);
        fareCalculator = new FareCalculator(path, userDetail);
        assertThat(fareCalculator.getFare()).isEqualTo(1680);
    }
}
