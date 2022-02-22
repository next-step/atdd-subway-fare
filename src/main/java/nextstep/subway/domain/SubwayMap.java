package nextstep.subway.domain;

import static nextstep.subway.utils.StringDateTimeConverter.convertDateTimeToString;
import static nextstep.subway.utils.StringDateTimeConverter.convertLineTimeToDateTime;
import static nextstep.subway.utils.StringDateTimeConverter.convertStringToDateTime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class SubwayMap {
    private List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target, PathType pathType, String startTimeString) {
        List<GraphPath<Station, SectionEdge>> paths = findAllKShortestPaths(source, target, pathType);

        ShortestPaths shortestPaths = findShortest(paths, startTimeString);

        if (isSearchingShortestDistancePath(pathType)) {
            return Path.of(new Sections(shortestPaths.getShortestDistanceSections())
                , shortestPaths.getShortestDistance(), shortestPaths.getShortestDistanceArrivalTime());
        }

        return Path.of(new Sections(shortestPaths.getShortestDurationSections())
            , shortestPaths.getShortestDistance(), shortestPaths.getShortestDurationArrivalTime());
    }

    private boolean isSearchingShortestDistancePath(PathType pathType) {
        return pathType.equals(PathType.DISTANCE);
    }

    protected List<GraphPath<Station, SectionEdge>> findAllKShortestPaths(Station source, Station target, PathType pathType) {
        return new KShortestPaths(createGraph(pathType), 100)
            .getPaths(source, target);
    }

    public ShortestPaths findShortest(List<GraphPath<Station, SectionEdge>> paths, String startTimeString) {
        LocalDateTime startTime = convertStringToDateTime(startTimeString);

        Path shortestDistancePath = findShortestDistancePath(paths, startTime);
        Path shortestDurationPath = findShortestDurationPath(paths, startTime);

        return new ShortestPaths(shortestDurationPath, shortestDistancePath);
    }

    private Path findShortestDurationPath(List<GraphPath<Station, SectionEdge>> paths, LocalDateTime startTime) {
        LocalDateTime fastestArrivalTime = LocalDateTime.MAX;
        List<Section> shortestSectionList = null;
        for (GraphPath<Station, SectionEdge> path : paths) {
            List<Section> sectionList = sectionsFromGraphPath(path);

            LocalDateTime currentTime = findArrivalTime(sectionList, startTime);

            if (currentTime.isBefore(fastestArrivalTime)) {
                shortestSectionList = sectionList;
                fastestArrivalTime = currentTime;
            }
        }

        return Path.of(
            new Sections(shortestSectionList)
            , totalDistanceOf(shortestSectionList)
            , convertDateTimeToString(fastestArrivalTime)
        );
    }

    private Path findShortestDistancePath(List<GraphPath<Station, SectionEdge>> paths, LocalDateTime startTime) {
        int shortestFareDistance = Integer.MAX_VALUE;
        List<Section> sectionList;
        LocalDateTime currentTime;
        GraphPath<Station, SectionEdge> shortestPath = null;

        int distance;
        for (GraphPath<Station, SectionEdge> path : paths) {
            distance = totalDistanceOf(sectionsFromGraphPath(path));

            if (distance <= shortestFareDistance) {
                shortestFareDistance = distance;

                shortestPath = path;
            }
        }

        sectionList = sectionsFromGraphPath(shortestPath);
        currentTime = findArrivalTime(sectionList, startTime);

        return Path.of(
            new Sections(sectionList), shortestFareDistance, convertDateTimeToString(currentTime));
    }

    private int totalDistanceOf(List<Section> sectionList) {
        return sectionList.stream()
            .mapToInt(Section::getDistance)
            .sum();
    }

    protected LocalDateTime findArrivalTime(List<Section> sectionList, LocalDateTime startTime) {
        LocalDateTime currentTime = startTime;

        Section prevSection = null;
        for (Section section : sectionList) {
            if (prevSection == null || !prevSection.getLine().equals(section.getLine())) {
                LocalDateTime trainTime = findTrainTime(section, currentTime);
                currentTime = trainTime;
            }

            currentTime = currentTime.plusMinutes(section.getDuration());

            prevSection = section;
        }

        return currentTime;
    }

    protected LocalDateTime findTrainTime(Section section, LocalDateTime currentTime) {
        Station targetStation = section.getUpStation();
        Line line = section.getLine();

        PathDirection pathDirection = findPathDirection(section);
        int durationTimeToStation = findDurationTimeToStation(line, targetStation, pathDirection);
        LocalDateTime trainTime = findFirstArrivedTrainTime(currentTime, line, durationTimeToStation);

        return trainTime.plusMinutes(durationTimeToStation);
    }

    private LocalDateTime findFirstArrivedTrainTime(LocalDateTime currentTime, Line line, int durationTimeToStation) {
        LocalDateTime startTime = convertLineTimeToDateTime(currentTime, line.getStartTime());
        int intervalTime = line.getIntervalTime();

        LocalDateTime trainTime = startTime;
        while (!currentTime.isBefore(trainTime.plusMinutes(durationTimeToStation))
            && !currentTime.isEqual(trainTime.plusMinutes(durationTimeToStation))) {
            trainTime = trainTime.plusMinutes(intervalTime);
        }

        return trainTime;
    }

    private int findDurationTimeToStation(Line line, Station targetStation, PathDirection pathDirection) {
        List<Section> sectionList = line.getSections();

        if (pathDirection.equals(PathDirection.UP)) {
            return calculateFromTimeDownToUp(sectionList, targetStation);
        }

        return calculateTimeFromTopToDown(sectionList, targetStation);
    }

    private int calculateFromTimeDownToUp(List<Section> sectionList, Station targetStation) {
        int durationMinuteSum = 0;

        for (int i = sectionList.size()-1; i >= 0; --i) {
            if (sectionList.get(i).getDownStation().equals(targetStation)) {
                break;
            }
            durationMinuteSum += sectionList.get(i).getDuration();
        }

        return durationMinuteSum;
    }

    private int calculateTimeFromTopToDown(List<Section> sectionList, Station targetStation) {
        int durationMinuteSum = 0;

        for (Section sect : sectionList) {
            if (sect.getUpStation().equals(targetStation)) {
                break;
            }
            durationMinuteSum += sect.getDuration();
        }

        return durationMinuteSum;
    }

    protected PathDirection findPathDirection(Section section) {
        Line line = section.getLine();
        List<Station> stations = line.getStations();

        Station currentStation = section.getUpStation();
        Station nextStation = section.getDownStation();

        int i = 0;
        if (stations.get(i).equals(currentStation)) {
            return PathDirection.DOWN;
        }

        while (++i < stations.size()) {
            if (currentStation.equals(stations.get(i))) {
                if (nextStation.equals(stations.get(i-1))) {
                    return PathDirection.UP;
                }
                break;
            }
        }

        return PathDirection.DOWN;
    }

    private List<Section> sectionsFromGraphPath(GraphPath<Station, SectionEdge> graphPath) {
        return graphPath.getEdgeList().stream()
            .map(SectionEdge::getSection)
            .collect(Collectors.toList());
    }

    private SimpleDirectedWeightedGraph<Station, SectionEdge> createGraph(PathType pathType) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph
            = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        addVertex(graph);
        addWeights(graph, pathType);

        return graph;
    }

    private void addWeights(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, PathType pathType) {
        addEdgeDistanceWeight(graph);
        addOppositeEdgeDistanceWeight(graph);
    }


    private void addVertex(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }

    private void addEdgeDistanceWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDistance());
                });
    }

    private void addOppositeEdgeDistanceWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(
                    it.getLine(),
                    it.getDownStation(),
                    it.getUpStation(),
                    it.getDistance(),
                    it.getDuration()
                ))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDistance());
                });
    }

    private void addEdgeDurationWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDuration());
                });
    }

    private void addOppositeEdgeDurationWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(
                    it.getLine(),
                    it.getDownStation(),
                    it.getUpStation(),
                    it.getDistance(),
                    it.getDuration()
                ))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDuration());
                });
    }
}
