package nextstep.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;
    private final SectionCondition condition;

    public static SectionEdge of(Section section) {
        return new SectionEdge(section);
    }

    public static SectionEdge of(Section section, SectionCondition condition) {
        return new SectionEdge(section, condition);
    }

    public SectionEdge(Section section) {
        this(section, SectionCondition.DISTANCE);
    }

    public SectionEdge(Section section, SectionCondition condition) {
        this.section = section;
        this.condition = condition;
    }

    public Section getSection() {
        return section;
    }

    public double getWeight() {
        if (condition == SectionCondition.DURATION) {
            return section.getDuration();
        }

        return section.getDistance();
    }
}
