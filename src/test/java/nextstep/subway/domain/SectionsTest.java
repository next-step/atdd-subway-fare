package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SectionsTest {

    @DisplayName("구간의 총 소요시간을 구한다")
    @ParameterizedTest
    @CsvSource(value = {
            "5,6,7,18",
            "5,7,10,22"
    })
    void totalDuration(int durationFirst, int durationSecond, int durationThird, int expected) {
        // given
        Section firstSection = new Section(null, null, null, 0, durationFirst);
        Section secondSection = new Section(null, null, null, 0, durationSecond);
        Section thirdSection = new Section(null, null, null, 0, durationThird);
        Sections sections = new Sections(List.of(firstSection, secondSection, thirdSection));

        // when
        int duration = sections.totalDuration();

        // then
        assertThat(duration).isEqualTo(expected);
    }
}
