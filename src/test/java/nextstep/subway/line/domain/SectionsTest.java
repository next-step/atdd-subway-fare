package nextstep.subway.line.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {

    @Test
    void getTotalFareTest() {
        Sections sections = new Sections();
        assertThat(sections.getTotalFare(58)).isEqualTo(2150);
        assertThat(sections.getTotalFare(10)).isEqualTo(1250);
        assertThat(sections.getTotalFare(17)).isEqualTo(1450);
    }
}
