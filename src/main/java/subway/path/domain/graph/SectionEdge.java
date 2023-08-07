package subway.path.domain.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jgrapht.graph.DefaultWeightedEdge;
import subway.line.domain.Section;


@AllArgsConstructor
public class SectionEdge extends DefaultWeightedEdge {

    @Getter
    private final Section section;
}
