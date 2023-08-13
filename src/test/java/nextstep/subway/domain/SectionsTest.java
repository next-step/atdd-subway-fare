package nextstep.subway.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class SectionsTest {

    @Test
    void isInLine_true() {

        // given
        Section section1 = new Section();
        Section section2 = new Section();

        Line line1 = spy(Line.class);

        when(line1.getSections()).thenReturn(List.of(section1));

        // when
        Sections sections = new Sections(List.of(section1, section2));
        boolean result = sections.isInLine(line1);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void isInLine() {

        // given
        Section section1 = new Section();
        Section section2 = new Section();

        Line line1 = spy(Line.class);

        when(line1.getSections()).thenReturn(List.of());

        // when
        Sections sections = new Sections(List.of(section1, section2));
        boolean result = sections.isInLine(line1);

        // then
        assertThat(result).isFalse();
    }
}