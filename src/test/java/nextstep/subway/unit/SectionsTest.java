package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

@DisplayName("Sections 테스트")
public class SectionsTest {
    @DisplayName("Station 순서에 맞춰 모든 소요 시간을 반환한다.")
    @Test
    void durations() {
        // Given
        Station 첫번째_역 = new Station();
        Station 두번째_역 = new Station();
        Station 세번째_역 = new Station();
        Station 네번째_역 = new Station();
        Sections sections = new Sections(Arrays.asList(
            new Section(null, 첫번째_역, 두번째_역, 1, 1),
            new Section(null, 두번째_역, 세번째_역, 2, 2),
            new Section(null, 세번째_역, 네번째_역, 3, 3)
        ));

        // When, Then
        assertThat(sections.durations()).containsExactly(1, 2, 3);
    }
}
