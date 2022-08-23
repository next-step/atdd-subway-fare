package nextstep.subway.domain.service;

import lombok.AllArgsConstructor;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SubwayMap extends SubwayPathFinder {
    private final List<Line> lines;

    public Path findPath(Station source, Station target, PathType type) {
        setGraph(type);
        List<SectionEdge> edgeList = getEdgeList(source, target);
        Sections sections = new Sections(convertToSections(edgeList));
        return new Path(sections);
    }

    private List<Section> convertToSections(List<SectionEdge> edgeList) {
        return edgeList.stream()
                     .map(SectionEdge::getSection)
                     .collect(Collectors.toList());
    }

    @Override
    protected void addVertex() {
        lines.stream()
             .flatMap(it -> it.getStations().stream())
             .distinct()
             .collect(Collectors.toList())
             .forEach(graph::addVertex);
    }

    @Override
    protected void setEdgeWeight(PathType type) {
        lines.stream()
             .flatMap(it -> it.getSections().stream())
             .forEach(it -> {
                 SectionEdge sectionEdge = SectionEdge.of(it);
                 graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                 graph.setEdgeWeight(sectionEdge, type.getValue(it));
             });
    }

    @Override
    protected void setOppositeEdgesWeight(PathType type) {
        lines.stream()
             .flatMap(it -> it.getSections().stream())
             .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
             .forEach(it -> {
                 SectionEdge sectionEdge = SectionEdge.of(it);
                 graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                 graph.setEdgeWeight(sectionEdge, type.getValue(it));
             });
    }

}
