package nextstep.subway.domain.strategy;

import nextstep.exception.ApplicationException;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.ShortestPathType;

import java.util.List;
import java.util.Objects;

public class ShortestPathFactory {

    public ShortestPathStrategy generateStrategy(ShortestPathType shortestPathType, List<Section> sections, PathType pathType) {
        if (sections.isEmpty()) {
            throw new ApplicationException("지하철 구간이 존재하지 않습니다.");
        }

        if (Objects.requireNonNull(shortestPathType) == ShortestPathType.DIJKSTRA) {
            return new Dijkstra(sections, pathType);
        }
        throw new ApplicationException("존재하지 않는 알고리즘 최단 전략 입니다.");

    }
}
