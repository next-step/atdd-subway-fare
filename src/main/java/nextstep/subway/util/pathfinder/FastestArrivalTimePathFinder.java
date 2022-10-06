package nextstep.subway.util.pathfinder;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.Multigraph;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FastestArrivalTimePathFinder {
    public static PathResponse find(List<Line> lines, LocalDateTime startTime,
                                    Station source, Station target, int fare) {
        Multigraph<Station, SectionEdge> graph = new Multigraph<>(SectionEdge.class);

        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.setEdgeWeight(sectionEdge, it.getDuration());
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.addEdge(it.getDownStation(), it.getUpStation(), sectionEdge);
                });

        // 다익스트라 최단 경로 찾기
        List<GraphPath<Station, SectionEdge>> paths = new KShortestPaths<>(graph, 1000).getPaths(source, target);

        GraphPath<Station, SectionEdge> fastestPath = paths.get(0);
        LocalDateTime fastestArriveTime = LocalDateTime.MAX;

        for (GraphPath<Station, SectionEdge> path : paths) {
            LocalDateTime arrivalTime = calculateArriveTime(startTime, source, path);
            if (arrivalTime.isBefore(fastestArriveTime)) {
                fastestArriveTime = arrivalTime;
                fastestPath = path;
            }
        }

        Sections sections = new Sections(fastestPath.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList()));
        return PathResponse.of(new Path(sections), fastestPath.getStartVertex(), fare, fastestArriveTime);
    }

    private static LocalDateTime calculateArriveTime(LocalDateTime start, Station source, GraphPath<Station, SectionEdge> path) {
        List<Section> sections = path.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());

        boolean isGoingUp = isGoingUp(sections);
        LocalDateTime currentTime = start;
        Station currentStation = source;
        Line currentLine = null;

        for (Section section : sections) {
            currentTime = getCurrentTime(currentLine, section, currentTime, currentStation, isGoingUp);
            currentLine = section.getLine();
            currentStation = getNextStation(section, isGoingUp);
        }
        return currentTime;
    }

    private static boolean isGoingUp(List<Section> sections) {
        Section firstSection = sections.get(0);
        Section secondSection = sections.get(1);
        return secondSection.isSameDownStation(firstSection.getUpStation());
    }

    private static LocalDateTime getCurrentTime(Line currentLine, Section section, LocalDateTime currentTime,
                                                Station currentStation, boolean isGoingUp) {
        if (currentLine == null || isDifferent(currentLine, section.getLine())) {
            LocalDateTime startTime = section.getLine().getStopTime(currentTime, currentStation, isGoingUp);
            return startTime.plusMinutes(section.getDuration());
        }
        return currentTime.plusMinutes(section.getDuration());
    }

    private static boolean isDifferent(Line line, Line currentLine) {
        return !line.equals(currentLine);
    }

    private static Station getNextStation(Section section, boolean isGoingUp) {
        if (isGoingUp) {
            return section.getUpStation();
        }
        return section.getDownStation();
    }
}
