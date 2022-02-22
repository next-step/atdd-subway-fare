package nextstep.subway.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class SubwayMap {
    private List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target, PathType pathType, String time) {
        List<GraphPath<Station, SectionEdge>> paths = findAllKShortestPaths(source, target, pathType);

        ShortestPaths shortestPaths = findShortest(paths, time);

        if (pathType.equals(PathType.DISTANCE)) {
            return Path.of(new Sections(shortestPaths.getShortestDistanceSections())
                , shortestPaths.getShortestDistance(), shortestPaths.getShortestDistanceArrivalTime());
        }

        return Path.of(new Sections(shortestPaths.getShortestDurationSections())
                , shortestPaths.getShortestDistance(), shortestPaths.getShortestDurationArrivalTime());
    }

    protected List<GraphPath<Station, SectionEdge>> findAllKShortestPaths(Station source, Station target, PathType pathType) {
        return new KShortestPaths(createGraph(pathType), 100)
            .getPaths(source, target);
    }

    public ShortestPaths findShortest(List<GraphPath<Station, SectionEdge>> paths, String startTimeString) {
        LocalDateTime startTime = convertStringToDateTime(startTimeString);

        Path shortestDistancePath = null;
        Path shortestDurationPath = null;

        // 1. 전체 경로가 나왔다.
        // 2. 경로들 중 최단거리 경로를 찾는다.
        //  2.1. weight 가 가장 작은 값인 경로,
        //  2.1. weight 의 합은 fareDistance
        int shortestFareDistance = Integer.MAX_VALUE;
        for (GraphPath<Station, SectionEdge> path : paths) {
            List<Section> sectionList = makeSectionListFromGraphPath(path);
            int distance = sectionList.stream()
                .mapToInt(Section::getDistance)
                .sum();

            if (distance <= shortestFareDistance) {
                shortestFareDistance = distance;

                List<Section> sections = makeSectionListFromGraphPath(path);

                LocalDateTime currentTime = findArrivalTime(sectionList, startTime);

                shortestDistancePath = Path.of(
                    new Sections(sections), shortestFareDistance, convertDateTimeToString(currentTime));
            }
        }

        // 3. 경로들 중 가장빠른도착시간 경로를 찾는다.
        //  3.*. 방향을 찾는다.
        //  3.0. 최초 구간 시작의 출발가능 시간을 구한다.
        //  3.1. 구간의 시작과 끝의 노선이 바뀌지 않았으면
        //   3.1.1. 기존 시간에 그냥 더한다.
        //  3.2. 구간의 시작과 끝의 노선이 다르면
        //   3.2.1. 구간의 시작에서 끝으로 가는 가장 빠른 정차시간을 찾는다.
        //    3.2.1.1. 해당 노선의 구간 시작~끝 인 방향으로, 노선의 시작시간과 시간간격으로 찾는다.
        //    3.2.1.2. 차가 없으면 그 다음날로 넘어간다.
        //   3.2.2.
        LocalDateTime fastestArrivalTime = LocalDateTime.MAX;
        List<Section> shortestSections = null;

        for (GraphPath<Station, SectionEdge> path : paths) {
            List<Section> sectionList = makeSectionListFromGraphPath(path);

//            LocalDateTime currentTime = startTime;
            LocalDateTime currentTime = findArrivalTime(sectionList, startTime);

//            Section prevSection = null;
//            for (Section section : sectionList) {
//                if (prevSection == null || !prevSection.getLine().equals(section.getLine())) {
//                    LocalDateTime trainTime = findTrainTime(section, currentTime);
//                    currentTime = trainTime;
//                }
//
//                currentTime = currentTime.plusMinutes(section.getDuration());
//
//                prevSection = section;
//            }

            if (currentTime.isBefore(fastestArrivalTime)) {
                shortestSections = sectionList;
                fastestArrivalTime = currentTime;
            }
        }

        shortestDurationPath = Path.of(
            new Sections(shortestSections), shortestFareDistance, convertDateTimeToString(fastestArrivalTime));

        return new ShortestPaths(shortestDurationPath, shortestDistancePath, shortestFareDistance);
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
        PathDirection pathDirection = findPathDirection(section);

        Station targetStation = section.getUpStation();
        Line line = section.getLine();

        List<Section> sectionList = line.getSections();
        int durationMinuteSum = 0;
        if (pathDirection.equals(PathDirection.UP)) {
            for (int i = sectionList.size()-1; i >= 0; --i) {
                if (sectionList.get(i).getDownStation().equals(targetStation)) {
                    break;
                }
                durationMinuteSum += sectionList.get(i).getDuration();
            }
        } else {
            for (Section sect : sectionList) {
                if (sect.getUpStation().equals(targetStation)) {
                    break;
                }
                durationMinuteSum += sect.getDuration();
            }
        }

        LocalDateTime startTime = convertLineTimeToDateTime(currentTime, line.getStartTime());
        LocalDateTime endTime = convertLineTimeToDateTime(currentTime, line.getEndTime());
        int intervalTime = line.getIntervalTime();

        // 첫차 시간 + (간격 X n) + 종점에서 정차역까지의 소요시간  이 조회 기준 시간보다 커질 때 까지 n을 증가
        LocalDateTime trainTime = startTime;
        while (!currentTime.isBefore(trainTime.plusMinutes(durationMinuteSum))
            && !currentTime.isEqual(trainTime.plusMinutes(durationMinuteSum))) {
            trainTime = trainTime.plusMinutes(intervalTime);
        }

        return trainTime.plusMinutes(durationMinuteSum);
    }

    protected static LocalDateTime convertLineTimeToDateTime(LocalDateTime findTime, String time) {
        return LocalDateTime.of(findTime.getYear(), findTime.getMonth(), findTime.getDayOfMonth()
            , Integer.parseInt(time.substring(0,2)), Integer.parseInt(time.substring(2)));
    }

    protected static LocalDateTime convertStringToDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        return LocalDateTime.parse(dateString, formatter);
    }

    protected static String convertDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
    }

    enum PathDirection {
        UP, DOWN
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

    //////////////////////////////////////////////////////////////////////////////////////

//    public Path findPath(Station source, Station target, PathType pathType, String time) {
//        GraphPath<Station, SectionEdge> result = findMinDistancePath(source, target);
//        int fareDistance = result.getEdgeList().stream()
//            .mapToInt(value -> value.getSection().getDistance())
//            .sum();
//
//        if (pathType.equals(PathType.DURATION)) {
//            result = findMinDurationPath(source, target);
//        }
//
//        List<Section> sections = makeSectionListFromGraphPath(result);
//
//        return Path.of(new Sections(sections), fareDistance);
//    }

    private GraphPath<Station, SectionEdge> findMinDistancePath(Station source, Station target) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath
            = new DijkstraShortestPath<>(createGraph(PathType.DISTANCE));
        return dijkstraShortestPath.getPath(source, target);
    }

    private GraphPath<Station, SectionEdge> findMinDurationPath(Station source, Station target) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath
            = new DijkstraShortestPath<>(createGraph(PathType.DURATION));
        return dijkstraShortestPath.getPath(source, target);
    }

    private List<Section> makeSectionListFromGraphPath(GraphPath<Station, SectionEdge> graphPath) {
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
//        if (pathType.equals(PathType.DISTANCE))
        {
            addEdgeDistanceWeight(graph);
            addOppositeEdgeDistanceWeight(graph);
//            return;
        }

//        addEdgeDurationWeight(graph);
//        addOppositeEdgeDurationWeight(graph);
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
