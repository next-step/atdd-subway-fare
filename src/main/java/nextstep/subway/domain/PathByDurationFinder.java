package nextstep.subway.domain;

import nextstep.subway.domain.entity.Section;

import java.util.List;

public class PathByDurationFinder extends PathFinder{
	public PathByDurationFinder(List<Section> sections) {
		super(sections);
	}

	@Override
	protected void addPath(Section section) {
		weightedMultigraph.addVertex(section.getDownStationId());
		weightedMultigraph.addVertex(section.getUpStationId());

		weightedMultigraph.setEdgeWeight(weightedMultigraph.addEdge(section.getDownStationId(), section.getUpStationId()), section.getDuration());
	}
}
