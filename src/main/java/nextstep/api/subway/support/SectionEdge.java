package nextstep.api.subway.support;

import org.jgrapht.graph.DefaultWeightedEdge;

import lombok.Getter;
import nextstep.api.subway.domain.line.Line;
import nextstep.api.subway.domain.line.Section;

@Getter
public class SectionEdge extends DefaultWeightedEdge {
    private final Line line;
    private final Section section;

    private SectionEdge(final Line line, final Section section) {
        this.line = line;
        this.section = section;
    }

    public static SectionEdge from(final Line line, final Section section) {
        return new SectionEdge(line, section);
    }
}
