package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.domain.fixture.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    @Test
    @DisplayName("모든 구간의 소요시간을 구한다.")
    void totalDuration() {
        Section section1 = Section.create(null, GANGNAM, YEOKSAM, 10, 2);
        Section section2 = Section.create(null, YEOKSAM, SEOLLEUNG, 15, 3);
        Sections sections = new Sections(List.of(section1, section2));

        assertThat(sections.totalDuration()).isEqualTo(5);
    }
}