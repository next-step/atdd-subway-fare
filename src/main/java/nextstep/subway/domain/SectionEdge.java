package nextstep.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;
    private final Line line;

    private SectionEdge(Section section, Line line) {
        this.section = section;
        this.line = line;
    }

    public static SectionEdge from(Section section, Line line) {
        return new SectionEdge(section, line);
    }

    public Line getLine() {
        return line;
    }

    public Section getSection() {
        return section;
    }
}
