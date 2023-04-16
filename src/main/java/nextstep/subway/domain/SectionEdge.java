package nextstep.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Objects;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;

    public SectionEdge(Section section) {
        this.section = section;
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section);
    }

    public Section getSection() {
        return section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionEdge that = (SectionEdge) o;
        return Objects.equals(getSection(), that.getSection());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSection());
    }
}
