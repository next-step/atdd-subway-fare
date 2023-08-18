package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.section.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private final Line line;
    private final Section section;

    public static SectionEdge of(Line line, Section section) {
        return new SectionEdge(line, section);
    }

    public SectionEdge(Line line, Section section) {
        this.line = line;
        this.section = section;
    }

    public Line getLine() {
        return line;
    }

    public Section getSection() {
        return section;
    }
}
