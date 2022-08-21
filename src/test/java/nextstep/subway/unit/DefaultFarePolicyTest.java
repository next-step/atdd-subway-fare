package nextstep.subway.unit;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.fare.DefaultFarePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultFarePolicyTest {

    private Path path;

    @BeforeEach
    void setUp() {
        path = new Path(new Sections(Collections.emptyList()));
    }

    @Test
    void defaultFare() {
        DefaultFarePolicy policy = new DefaultFarePolicy();
        assertThat(policy.fare(path)).isEqualTo(1250);
    }
}
