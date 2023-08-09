package nextstep.subway.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class PathTest {

    @Test
    void getAdditionalFare() {

        // given
        Line line1 = spy(Line.class);
        Line line2 = spy(Line.class);
        Line line3 = spy(Line.class);
        Sections sections = spy(Sections.class);

        when(line1.getAdditionalFare()).thenReturn(100);
        when(line2.getAdditionalFare()).thenReturn(200);
        when(line3.getAdditionalFare()).thenReturn(300);
        when(sections.isInLine(any())).thenReturn(true);

        // when
        Path path = new Path(List.of(line1, line2, line3), sections);
        long additionalFare = path.getAdditionalFareByLine();

        // then
        assertThat(additionalFare).isEqualTo(300);

    }
}