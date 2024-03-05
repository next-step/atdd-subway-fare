package nextstep.subway.domain.strategy;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

class SectionProxy extends DefaultWeightedEdge {
    private final Section section;
    private final double weight;

    public SectionProxy(Section section, double weight) {
        this.section = section;
        this.weight = weight;
    }

    public Station getSourceVertex() {
        return section.upStation();
    }

    public Station getTargetVertex() {
        return section.downStation();
    }

    public Section toSection() {
        return section;
    }

    @Override
    public double getWeight() {
        return weight;
    }

}
