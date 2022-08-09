package nextstep.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;

import nextstep.subway.domain.Section;

public class SectionEdge extends DefaultWeightedEdge {
	private Section section;

	public static SectionEdge of(Section section) {
		return new SectionEdge(section);
	}

	public SectionEdge(Section section) {
		this.section = section;
	}

	public Section getSection() {
		return section;
	}
}
