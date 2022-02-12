package nextstep.subway.domain;

import java.util.List;


public class SubwayMap {
    private final List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target, PathType pathType) {
        // 그래프 만들기
        SubwayMapGraph subwayMapGraph = SubwayMapGraph.createGraph(lines, pathType);

        // 다익스트라 최단 경로 찾기
        List<Section> sections = subwayMapGraph.getShortestPathSections(source, target);

        return new Path(new Sections(sections));
    }
}
