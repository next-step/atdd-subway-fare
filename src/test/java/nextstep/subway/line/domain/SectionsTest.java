package nextstep.subway.line.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SectionsTest {

    @Spy
    private Sections sections;

    @Test
    void getTotalFare() {
        // given
        when(sections.getTotalDistance()).thenReturn(45);

        // when
        int totalFare = sections.getTotalFare();

        // then
        assertThat(totalFare).isEqualTo(1950);
    }
}
