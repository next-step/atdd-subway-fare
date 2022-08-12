package nextstep.line.domain;

import nextstep.line.domain.exception.IllegalSectionOperationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {

    @Test
    void addDuplicateSection() {
        Sections sections = new Sections();
        sections.add(new Section(1L, 2L, 5, 5));

        assertThatThrownBy(() -> sections.add(new Section(2L, 1L, 5, 5)))
                .isInstanceOf(IllegalSectionOperationException.class)
                .hasMessage(IllegalSectionOperationException.ALREADY_INCLUDED);
    }

    @Test
    void addSectionInMiddle() {
        Sections sections = new Sections();
        sections.add(new Section(1L, 3L, 10, 10));

        sections.add(new Section(2L, 3L, 4, 5));

        assertThat(sections.getStations()).containsExactly(1L, 2L, 3L);
        assertThat(sections.getSections()).extracting("distance").containsExactlyInAnyOrder(6, 4);
        assertThat(sections.getSections()).extracting("duration").containsExactlyInAnyOrder(5, 5);
    }

    @Test
    void deleteMiddleSection() {
        Sections sections = new Sections();
        sections.add(new Section(1L, 2L, 5, 5));
        sections.add(new Section(2L, 3L, 5, 5));

        sections.delete(2L);

        assertThat(sections.getStations()).containsExactly(1L, 3L);
        assertThat(sections.getSections()).extracting("distance").containsExactlyInAnyOrder(10);
        assertThat(sections.getSections()).extracting("duration").containsExactlyInAnyOrder(10);
    }

    @Test
    void getStations() {
        Sections sections = new Sections();
        sections.add(new Section(1L, 4L, 90, 90)); // 1 > 4
        sections.add(new Section(1L, 2L, 20, 20)); // 1 > 2 > 4
        sections.add(new Section(2L, 3L, 20, 20)); // 1 > 2 > 3 > 4
        sections.add(new Section(4L, 5L, 20, 20)); // 1 > 2 > 3 > 4 > 5

        assertThat(sections.getStations()).containsExactly(1L, 2L, 3L, 4L, 5L);
    }

    @Test
    void totalDistanceAndDuration() {
        Sections sections = new Sections();
        sections.add(new Section(1L, 4L, 30, 30)); // 1 > 4 (30)
        sections.add(new Section(1L, 3L, 20, 20)); // 1 > 3 > 4 (30)
        sections.add(new Section(2L, 3L, 5, 5));   // 1 > 2 > 3 > 4 (30)
        sections.add(new Section(4L, 5L, 10, 10)); // 1 > 2 > 3 > 4 > 5 (40)

        assertThat(sections.totalDistance()).isEqualTo(40);
        assertThat(sections.totalDuration()).isEqualTo(40);
    }

}
