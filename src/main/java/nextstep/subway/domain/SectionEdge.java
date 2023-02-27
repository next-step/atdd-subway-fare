package nextstep.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;
    private final Line line;

    public SectionEdge(Section section, Line line) {
        this.section = section;
        this.line = line;
    }

    public static SectionEdge of(Section section, Line line) {
        return new SectionEdge(section, line);
    }

    public Line getLine() {
        return line;
    }

    public Section getSection() {
        return section;
    }
}
