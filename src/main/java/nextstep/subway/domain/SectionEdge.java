package nextstep.subway.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jgrapht.graph.DefaultWeightedEdge;

@Getter
@RequiredArgsConstructor
public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;

    public static SectionEdge of(Section section) {
        return new SectionEdge(section);
    }
}
