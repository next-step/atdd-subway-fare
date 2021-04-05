package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.Test;


import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LineFarePolicyTest {
    private LineFarePolicy lineFarePolicy;
    private static final int BASIC_FEE = 1250;

    @Test
    void calculateFareByPolicyTest_WithLines() {
        Line line1 = new Line("1호선", "blue", 500);
        Line line2 = new Line("2호선", "green", 100);
        Line line3 = new Line("3호선", "red", 0);
        lineFarePolicy = new LineFarePolicy(Arrays.asList(line1, line2, line3));

        assertThat(lineFarePolicy.calculateFareByPolicy(BASIC_FEE)).isEqualTo(1750);
    }
}
