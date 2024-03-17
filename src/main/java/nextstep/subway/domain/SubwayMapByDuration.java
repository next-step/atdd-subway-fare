package nextstep.subway.domain;

import nextstep.subway.domain.entity.Section;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SubwayMapByDuration implements PathFinder{
	private final SubwayMap subwayMap;

	public SubwayMapByDuration(List<Section> sections) {
		this.subwayMap = new SubwayMap();

		sections.forEach(this::addSection);
	}
	@Override
	public Path getShortestPath(Long source, Long target) {
		return subwayMap.getShortestPath(source, target);
	}

	private void addSection(Section section) {
		WeightedMultigraph weightedMultigraph = subwayMap.getWeightedMultigraph();
		weightedMultigraph.addVertex(section.getDownStationId());
		weightedMultigraph.addVertex(section.getUpStationId());

		SectionWeightedEdge edge = new SectionWeightedEdge(section);
		weightedMultigraph.addEdge(section.getDownStationId(), section.getUpStationId(), edge);

		weightedMultigraph.setEdgeWeight(edge, section.getDuration());
	}
}
