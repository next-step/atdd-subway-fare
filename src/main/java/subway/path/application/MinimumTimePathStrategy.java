package subway.path.application;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.line.domain.Section;
import subway.station.domain.Station;

import java.util.List;

@Component
public class MinimumTimePathStrategy implements PathStrategy { // TODO
    @Override
    public Double getWeightOfPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                  Station sourceStation,
                                  Station targetStation) {
        // MinimumTimePathFinder에서의 getWeightOfMinimumTimePath() 메서드의 구현을 여기에 넣습니다.
    }

    @Override
    public Long getTotalWeightInPath(List<Station> stationsInPath, List<Section> sections) {
        // MinimumTimePathFinder에서의 getDistanceInMinimumTimePath() 메서드의 구현을 여기에 넣습니다.
    }

    @Override
    public void setEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section, DefaultWeightedEdge edge) {
        // edge의 weight를 섹션의 duration으로 설정
        graph.setEdgeWeight(edge, section.getDuration());
    }
}
