package nextstep.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private Section section;
    private int sectionWeight;
    public static SectionEdge of(Section section, PathWeight pathWeight) {
        return new SectionEdge(section, pathWeight);
    }

    public SectionEdge(Section section, PathWeight pathWeight) {
        this.section = section;
        this.sectionWeight = pathWeight == PathWeight.DISTANCE ? section.getDistance() : section.getDuration();
    }

    public Section getSection() {
        return section;
    }

    public int getEdgeWeight(){
        return sectionWeight;
    }

}
