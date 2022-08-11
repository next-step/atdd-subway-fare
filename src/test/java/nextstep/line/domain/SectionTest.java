package nextstep.line.domain;

import nextstep.line.domain.exception.IllegalSectionOperationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionTest {

    @Test
    void combine() {
        Section section = new Section(1L, 2L, 5, 5);
        Section anotherSection = new Section(2L, 3L, 10, 10);

        Section combinedSection = section.combine(anotherSection);

        assertThat(combinedSection.getUpStationId()).isEqualTo(1L);
        assertThat(combinedSection.getDownStationId()).isEqualTo(3L);
        assertThat(combinedSection.getDistance()).isEqualTo(15);
        assertThat(combinedSection.getDuration()).isEqualTo(15);
    }

    @Test
    void combine_Exception() {
        Section section = new Section(1L, 2L, 5, 10);
        Section anotherSection = new Section(3L, 4L, 10, 10);

        assertThatThrownBy(() -> section.combine(anotherSection))
                .isInstanceOf(IllegalSectionOperationException.class)
                .hasMessage(IllegalSectionOperationException.CANT_COMBINE);
    }

    @Test
    void subtractSameUpStation() {
        Section section = new Section(1L, 3L, 15, 15);
        Section anotherSection = new Section(2L, 3L, 10, 10);

        Section subtractedSection = section.subtract(anotherSection);

        assertThat(subtractedSection.getUpStationId()).isEqualTo(1L);
        assertThat(subtractedSection.getDownStationId()).isEqualTo(2L);
        assertThat(subtractedSection.getDistance()).isEqualTo(5);
        assertThat(subtractedSection.getDuration()).isEqualTo(5);
    }

    @Test
    void subtractSameDownStation() {
        Section section = new Section(1L, 3L, 15, 15);
        Section anotherSection = new Section(1L, 2L, 10, 10);

        Section subtractedSection = section.subtract(anotherSection);

        assertThat(subtractedSection.getUpStationId()).isEqualTo(2L);
        assertThat(subtractedSection.getDownStationId()).isEqualTo(3L);
        assertThat(subtractedSection.getDistance()).isEqualTo(5);
        assertThat(subtractedSection.getDuration()).isEqualTo(5);
    }

    @Test
    void subtract_Distance_Exception() {
        Section section = new Section(1L, 3L, 10, 10);
        Section anotherSection = new Section(1L, 2L, 10, 5);

        assertThatThrownBy(() -> section.subtract(anotherSection))
                .isInstanceOf(IllegalSectionOperationException.class)
                .hasMessage(IllegalSectionOperationException.INVALID_DISTANCE);
    }

    @Test
    void subtract_Duration_Exception() {
        Section section = new Section(1L, 3L, 10, 10);
        Section anotherSection = new Section(1L, 2L, 5, 10);

        assertThatThrownBy(() -> section.subtract(anotherSection))
                .isInstanceOf(IllegalSectionOperationException.class)
                .hasMessage(IllegalSectionOperationException.INVALID_DURATION);
    }

    @Test
    void subtract_CantSubtract_Exception() {
        Section section = new Section(1L, 3L, 10, 10);
        Section anotherSection = new Section(2L, 4L, 5, 5);

        assertThatThrownBy(() -> section.subtract(anotherSection))
                .isInstanceOf(IllegalSectionOperationException.class)
                .hasMessage(IllegalSectionOperationException.CANT_SUBTRACT);
    }

}
