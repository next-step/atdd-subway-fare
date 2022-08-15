package nextstep.subway.unit;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.fare.DefaultFareCalculator;
import nextstep.subway.domain.fare.FareCalculatorChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultFareCalculatorTest {

    private Path path;

    @BeforeEach
    void setUp() {
        path = new Path(new Sections(Collections.emptyList()));
    }

    @Test
    void defaultFare() {
        FareCalculatorChain defaultFareCalculator = new DefaultFareCalculator();
        assertThat(defaultFareCalculator.calculate(path, 0)).isEqualTo(1250);
    }
}
