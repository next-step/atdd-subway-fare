package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.fixture.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {

    @Test
    @DisplayName("반대방향으로 구간을 생성하면 상행역과 하행역의 방향이 반대로 변경이 된다.")
    void reverseOf() {
        Section section = Section.create(null, GANGNAM, YEOKSAM, 10, 3);

        Section reverseSection = Section.reverseOf(section);

        assertThat(reverseSection).isEqualTo(Section.create(null, YEOKSAM, GANGNAM, 10, 3));
    }

    @Test
    @DisplayName("두 구간을 합치면 길이와 소요시간이 합쳐지며 공통 역은 사라진다.")
    void combineSection() {
        Section upSection = Section.create(null, GANGNAM, YEOKSAM, 10, 3);
        Section downSection = Section.create(null, YEOKSAM, SEOLLEUNG, 8, 2);

        Section combineSection = Section.combineSection(upSection, downSection);

        assertThat(combineSection).isEqualTo(Section.create(null, GANGNAM, SEOLLEUNG, 18, 5));
    }
}