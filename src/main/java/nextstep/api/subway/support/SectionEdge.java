package nextstep.api.subway.support;

import org.jgrapht.graph.DefaultWeightedEdge;

import lombok.Getter;
import nextstep.api.subway.domain.line.Section;

@Getter
public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;

    private SectionEdge(final Section section) {
        this.section = section;
    }

    public static SectionEdge of(final Section section) {
        return new SectionEdge(section);
    }
}
