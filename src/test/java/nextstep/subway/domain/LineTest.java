package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    @DisplayName("노선에 구간을 추가한다")
    @Test
    void addSection() {
        // given
        Station sinsaStation = new Station("신사역");
        Station gwanggyoStation = new Station("광교역");
        Line line = new Line();

        // when
        line.addSection(sinsaStation, gwanggyoStation, 15, 42 * 60);

        // then
        Section section = line.getSections().get(0);
        Section exceptedSection = new Section(line, sinsaStation, gwanggyoStation, 15, 42 * 60);
        assertThat(section).usingRecursiveComparison().isEqualTo(section);
    }
}
