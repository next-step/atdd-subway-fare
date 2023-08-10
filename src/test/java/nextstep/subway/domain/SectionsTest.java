package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @DisplayName("새로운 구간을 추가한다")
    @Test
    void insertSection() {
        Sections sections = new Sections(new ArrayList<>(List.of(
            new Section(null, new Station("강남역"), new Station("판교역"), 10, 15)
        )));

        sections.add(new Section(null, new Station("판교역"), new Station("양재역"), 2, 3));

        assertThat(sections.getSections())
            .extracting(Section::getDistance)
            .containsExactly(10, 2);

        assertThat(sections.getSections())
            .extracting(Section::getDuration)
            .containsExactly(15, 3);
    }

}