package nextstep.subway.unit;

import nextstep.subway.domain.FareCalculator;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.SubwayMapByDistance;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.Section;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FareCalculatorTest {
    private SubwayMap subwayMap;
    private FareCalculator fareCalculator;

    @BeforeEach
    void setup() {

        Line 노선1 = new Line("노선1", "파란색", 1L, 2L, 10, 5);
        Line 노선2 = new Line("노선2", "빨간색", 3L, 4L, 20, 5);

        subwayMap = new SubwayMapByDistance(List.of(
                new Section(노선1, 1L, 2L, 9, 5),
                new Section(노선1, 2L, 3L, 7, 10),
                new Section(노선2, 3L, 4L, 20, 5),
                new Section(노선2, 4L, 5L, 14, 5),
                new Section(노선2, 5L, 6L, 9, 9)
        ));
    }

    /**
     * 10km까지 - 기본운임 1250원
     */
    @Test
    void calculateFare10Km() {
        Path path = subwayMap.getShortesPath(1L, 2L);
        fareCalculator = new FareCalculator(path);
        assertThat(fareCalculator.getFare()).isEqualTo(1250);
    }

    /**
     * 10km초과 ∼ 50km까지 (5km마다 100원)
     * 16km = 10km(1250원) + 5km(100원) + 1km(100원) - 1450원
     */
    @Test
    void calculateFareOver10Km() {
        Path path = subwayMap.getShortesPath(1L, 3L);
        fareCalculator = new FareCalculator(path);
        assertThat(fareCalculator.getFare()).isEqualTo(1450);
    }

    /**
     * 10km초과∼50km까지(5km마다 100원)
     * 50km = 10km(1250원) + 5km * 8 (800원) - 2050원
     */
    @Test
    void calculateFare50Km() {
        Path path = subwayMap.getShortesPath(1L, 5L);
        fareCalculator = new FareCalculator(path);
        assertThat(fareCalculator.getFare()).isEqualTo(2050);
    }

    /**
     * 50km초과 시 (8km마다 100원)
     * 59km = 10km(1250원) + 5km * 8 (800원) + 8km (100원) + 1km (100원) - 2250원
     */
    @Test
    void calculateFareOver50Km() {
        Path path = subwayMap.getShortesPath(1L, 6L);
        fareCalculator = new FareCalculator(path);
        assertThat(fareCalculator.getFare()).isEqualTo(2250);
    }
}
