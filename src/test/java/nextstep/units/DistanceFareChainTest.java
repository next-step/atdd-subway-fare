package nextstep.units;

import nextstep.path.fare.distance.BaseDistanceFareChain;
import nextstep.path.fare.distance.LongDistanceFareChain;
import nextstep.path.fare.distance.MediumDistanceFareChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistanceFareChainTest {
    private BaseDistanceFareChain baseFareChain;
    private MediumDistanceFareChain mediumFareChain;
    private LongDistanceFareChain longFareChain;

    @BeforeEach
    public void setUp() {
        baseFareChain = new BaseDistanceFareChain();
        mediumFareChain = new MediumDistanceFareChain();
        longFareChain = new LongDistanceFareChain();

        baseFareChain.addNext(mediumFareChain);
        mediumFareChain.addNext(longFareChain);
    }

    @Test
    public void testBaseFare() {
        int distance = 9;
        int fare = baseFareChain.calculate(distance);

        assertEquals(1250, fare);
    }

    /**
     * 10 + 11 => (10 - 1) / 5 + 1 = 1550
     */
    @Test
    public void testMediumFare() {
        int distance = 21;
        int fare = baseFareChain.calculate(distance);

        assertEquals(1550, fare);
    }

    /**
     * 10 + 50 => (50 - 1) / 8 + 1 = 1950
     */
    @Test
    public void testLongFare() {
        int distance = 60;
        int fare = baseFareChain.calculate(distance);

        assertEquals(1950, fare);
    }

}
