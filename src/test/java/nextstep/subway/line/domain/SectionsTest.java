package nextstep.subway.line.domain;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SectionsTest {

    @Spy
    private Sections sections;

    @ParameterizedTest
    @CsvSource(value = {"7:1250", "45:1950", "50:2050", "66:2250"},  delimiter = ':')
    void getTotalFare(int distance, int resultFare) {
        // given
        when(sections.getTotalDistance()).thenReturn(distance);

        // when
        int totalFare = sections.getTotalFare();

        // then
        assertThat(totalFare).isEqualTo(resultFare);
    }
}
