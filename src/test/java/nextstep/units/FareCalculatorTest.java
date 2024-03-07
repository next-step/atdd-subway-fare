package nextstep.units;

import nextstep.line.Line;
import nextstep.member.domain.AgeRange;
import nextstep.path.fare.FareCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FareCalculatorTest {
    private Map<String, Line> stubLines = new HashMap<>();

    @BeforeEach
    public void setUp() {
        stubLines.put("1호선", new Line(1L, "blue", "1호선", 0, null, null, null, 0));
        stubLines.put("2호선", new Line(2L, "green", "2호선", 300, null, null, null, 0));
        stubLines.put("3호선", new Line(3L, "brown", "3호선", 600, null, null, null, 0));
        stubLines.put("4호선", new Line(4L, "sky", "4호선", 900, null, null, null, 0));
    }

    /**
     * distance fare: 10 + 2 => (MediumFare 적용) 1350
     * Age fare: 0
     * extra fare: 0
     * total fare: 1350
     */
    @Test
    public void testMediumFareForAdultOnSingleLine() {
        int distance = 12;
        Set<Line> lines = Set.of(stubLines.get("1호선"));
        AgeRange ageRange = AgeRange.ADULT;

        FareCalculator fareCalculator = new FareCalculator(distance, lines, ageRange);

        int totalFare = fareCalculator.calculate();

        assertEquals(1350, totalFare);
    }

    /**
     * distance fare: 10 + 2 => (MediumFare 적용) 1350
     * Age fare: 0
     * extra fare: +300(2호선)
     * total fare: 1350 + 300 = 1650
     */
    @Test
    public void testMediumFareForAdultOnMultipleLine() {
        int distance = 12;
        Set<Line> lines = Set.of(stubLines.get("1호선"), stubLines.get("2호선"));
        AgeRange ageRange = AgeRange.ADULT;

        FareCalculator fareCalculator = new FareCalculator(distance, lines, ageRange);

        int totalFare = fareCalculator.calculate();

        assertEquals(1650, totalFare);
    }

    /**
     * distance fare: 10 + 2 => (MediumFare 적용) 1350
     * Age fare: (Teenager 적용) 350원 공제의 20% 할인
     * extra fare: +300(2호선)
     * total fare: (1650원 - 350원) × (1 - 0.20)
     */
    @Test
    public void testMediumFareForTeenagerOnMultipleLines() {
        int distance = 12;
        Set<Line> lines = Set.of(stubLines.get("1호선"), stubLines.get("2호선"));
        AgeRange ageRange = AgeRange.TEENAGER;

        FareCalculator fareCalculator = new FareCalculator(distance, lines, ageRange);

        int totalFare = fareCalculator.calculate();

        assertEquals(1040, totalFare);
    }

    /**
     * distance fare: 10 + 2 => (MediumFare 적용) 1350
     * Age fare: (Children 적용) 350원 공제의 50% 할인
     * extra fare: +300(2호선)
     * total fare: (1650원 - 350원) × (1 - 0.50) = 650
     */
    @Test
    public void testMediumFareForChildOnMultipleLines() {
        int distance = 12;
        Set<Line> lines = Set.of(stubLines.get("1호선"), stubLines.get("2호선"));
        AgeRange ageRange = AgeRange.CHILDREN;

        FareCalculator fareCalculator = new FareCalculator(distance, lines, ageRange);

        int totalFare = fareCalculator.calculate();

        assertEquals(650, totalFare);
    }

    /**
     * distance fare: 10 + 2 => (MediumFare 적용) 1350
     * Age fare: (Children 적용) 350원 공제의 50% 할인
     * extra fare: +600(3호선)
     * total fare: (1950원 - 350원) × (1 - 0.50) = 800
     */
    @Test
    public void testMediumFareForChildOnMultipleLines2() {
        int distance = 12;
        Set<Line> lines = Set.of(stubLines.get("1호선"), stubLines.get("2호선"), stubLines.get("3호선"));
        AgeRange ageRange = AgeRange.CHILDREN;

        FareCalculator fareCalculator = new FareCalculator(distance, lines, ageRange);

        int totalFare = fareCalculator.calculate();

        assertEquals(800, totalFare);
    }

    /**
     * distance fare: 10 + 10 => (MediumFare 적용) 1450
     * Age fare: (Children 적용) 350원 공제의 50% 할인
     * extra fare: +600(3호선)
     * total fare: (2050원 - 350원) × (1 - 0.50) = 850
     */
    @Test
    public void testMediumFareForChildOnMultipleLines3() {
        int distance = 20;
        Set<Line> lines = Set.of(stubLines.get("1호선"), stubLines.get("2호선"), stubLines.get("3호선"));
        AgeRange ageRange = AgeRange.CHILDREN;

        FareCalculator fareCalculator = new FareCalculator(distance, lines, ageRange);

        int totalFare = fareCalculator.calculate();

        assertEquals(850, totalFare);
    }

    /**
     * distance fare: 10 + 50 => (LongFare 적용) 1950
     * Age fare: (Children 적용) 350원 공제의 50% 할인
     * extra fare: +2550(3호선)
     * total fare: (2550원 - 350원) × (1 - 0.50) = 1100
     */
    @Test
    public void testLongFareForChildOnMultipleLines() {
        int distance = 60;
        Set<Line> lines = Set.of(stubLines.get("1호선"), stubLines.get("2호선"), stubLines.get("3호선"));
        AgeRange ageRange = AgeRange.CHILDREN;

        FareCalculator fareCalculator = new FareCalculator(distance, lines, ageRange);

        int totalFare = fareCalculator.calculate();

        assertEquals(1100, totalFare);
    }
}
