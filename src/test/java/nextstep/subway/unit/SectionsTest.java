package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SectionsTest {

    private final Station a = new Station("a");
    private final Station b = new Station("b");
    private final Station c = new Station("c");
    private final Station d = new Station("d");

    private final Line line = new Line("line", "bg");
    private final Section section = new Section(line, a, b, 5, 5);
    
    private final Sections sections = new Sections();
    
    @BeforeEach
    void init() {
        sections.add(section);
    }

    @Test
    void addSectionInterStationWhenHasSameUpwardStation() {
        Section newSection = new Section(line, a, c, 4, 4);
        sections.add(newSection);

        List<Section> addedSection = sections.getSections();
        assertThat(addedSection).hasSize(2);
        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(a, c, b);
    }

    @Test
    void addSectionInterStationWhenHasSameDownwardStation() {
        Section newSection = new Section(line, d, b, 4, 4);

        sections.add(newSection);

        List<Section> addedSection = sections.getSections();
        assertThat(addedSection).hasSize(2);
        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(a, d, b);
    }

    @Test
    void addSectionWhenLastDownwardStation() {
        Section newSection = new Section(line, b, c, 4, 4);

        sections.add(newSection);

        List<Section> addedSection = sections.getSections();
        assertThat(addedSection).hasSize(2);
        assertThat(addedSection.get(0).getDistance()).isEqualTo(5);
        assertThat(addedSection.get(1).getDistance()).isEqualTo(4);
    }

    @Test
    void addSectionWhenLastUpwardStation() {
        Section newSection = new Section(line, c, a, 4, 4);

        sections.add(newSection);

        List<Section> addedSection = sections.getSections();
        assertThat(addedSection).hasSize(2);
        assertThat(addedSection.get(0).getDistance()).isEqualTo(5);
        assertThat(addedSection.get(1).getDistance()).isEqualTo(4);
    }

    @Test
    void removeSection() {
        Section newSection = new Section(line, b, c, 4, 4);
        sections.add(newSection);

        sections.delete(b);

        List<Section> sectionList = sections.getSections();
        assertThat(sectionList).hasSize(1);
        Section leftSection = sectionList.get(0);
        assertThat(leftSection.getDistance()).isEqualTo(9);
        assertThat(leftSection.getUpStation()).isEqualTo(a);
        assertThat(leftSection.getDownStation()).isEqualTo(c);
    }

    @Test
    void removeSectionUpLastStation() {
        Section newSection = new Section(line, b, c, 4, 4);
        sections.add(newSection);

        sections.delete(a);

        List<Section> sectionList = sections.getSections();
        assertThat(sectionList).hasSize(1);
        Section leftSection = sectionList.get(0);
        assertThat(leftSection.getDistance()).isEqualTo(4);
        assertThat(leftSection.getUpStation()).isEqualTo(b);
        assertThat(leftSection.getDownStation()).isEqualTo(c);
    }

    @Test
    void removeSectionDownLastStation() {
        Section newSection = new Section(line, b, c, 4, 4);
        sections.add(newSection);

        sections.delete(c);

        List<Section> sectionList = sections.getSections();
        assertThat(sectionList).hasSize(1);
        Section leftSection = sectionList.get(0);
        assertThat(leftSection.getDistance()).isEqualTo(5);
        assertThat(leftSection.getUpStation()).isEqualTo(a);
        assertThat(leftSection.getDownStation()).isEqualTo(b);
    }

    @Test
    void getStationsByOrder() {
        Section newSection = new Section(line, c, a, 4, 4);
        sections.add(newSection);
        Section newSection1 = new Section(line, a, d, 1, 1);
        sections.add(newSection1);

        List<Station> stations = sections.getStations();
        assertThat(stations).containsExactly(c, a, d, b);
    }
}
