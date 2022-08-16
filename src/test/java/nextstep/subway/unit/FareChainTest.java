package nextstep.subway.unit;

import nextstep.subway.domain.service.chain.FareChain;
import nextstep.subway.domain.service.chain.FareChainCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FareChainTest {

    @Autowired
    @Qualifier("basicFareCalculator")
    private FareChain basicChain;

    @Autowired
    @Qualifier("tenToFiftyKiloFareCalculator")
    private FareChain tenToFiftyChain;

    @Autowired
    @Qualifier("overFiftyKiloFareCalculator")
    private FareChain overToFiftyChain;

    private FareChainCalculator fareChainCalculator;

    @BeforeEach
    void setUp() {
        fareChainCalculator = new FareChainCalculator(basicChain, tenToFiftyChain, overToFiftyChain);
    }

    @Test
    void basicCalculator() {
        final int oneDistance = 1;
        final int tenDistance = 10;
        final int baseFare = 1250;

        int 거리가_1_일때_요금 = fareChainCalculator.operate(oneDistance);
        int 거리가_10_일때_요금 = fareChainCalculator.operate(tenDistance);

        assertThat(거리가_1_일때_요금).isEqualTo(baseFare);
        assertThat(거리가_10_일때_요금).isEqualTo(baseFare);
    }


    @Test
    void tenToFiftyCalculator() {
        final int fiftyDistance = 50;
        final int elevenDistance = 11;
        final int fiftyFare = 2050;
        final int elevenFare = 1350;

        int 거리가_50_일때_요금 = fareChainCalculator.operate(fiftyDistance);
        int 거리가_11_일때_요금 = fareChainCalculator.operate(elevenDistance);

        assertThat(거리가_50_일때_요금).isEqualTo(fiftyFare);
        assertThat(거리가_11_일때_요금).isEqualTo(elevenFare);
    }

    @Test
    void overToFiftyCalculator() {
        final int distance = 51;
        final int fiftyOneFare = 2150;

        int 거리가_51_일때_요금 = fareChainCalculator.operate(distance);

        assertThat(거리가_51_일때_요금).isEqualTo(fiftyOneFare);
    }


}
