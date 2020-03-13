package atdd.path.application.dto;

import atdd.exception.ErrorType;
import atdd.exception.SubwayException;
import atdd.path.domain.Edge;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class MinTimePathAssembler {
    private List<MinTimePathLine> minTimePathLines;
    private List<Station> pathStations;
    private List<Line> lines;
    private LocalDateTime startDateTime;

    public MinTimePathAssembler(List<Line> lines, List<Station> pathStations, LocalDateTime startDateTime) {
        this.pathStations = pathStations;
        this.minTimePathLines = makeMinTimePathLines(lines, pathStations);
        this.lines = lines;
        this.startDateTime = startDateTime;
    }

    public MinTimePathResponseView assemble() {
        LocalDateTime localDateTime = LocalDateTime.now();

        return MinTimePathResponseView.builder()
                .startStationId(pathStations.get(0).getId())
                .endStationId(pathStations.get(pathStations.size() - 1).getId())
                .stations(StationSimpleResponseView.listOf(pathStations))
                .lines(LineSimpleResponseView.listOf(getLine()))
                .distance(getDistance())
                .departAt(localDateTime)
                .arriveBy(calculateArriveBy(startDateTime)).build();
    }

    private List<MinTimePathLine> makeMinTimePathLines(List<Line> lines, List<Station> pathStations) {
        List<MinTimePathLine> minTimePathLines = new ArrayList<>();

        for (int i = 0; i < pathStations.size() - 1; i++) {
            for (Line line : lines) {
                Optional<Edge> edgeOptional = findEdge(line.getEdges(), pathStations.get(i).getId(), pathStations.get(i + 1).getId());

                if (!edgeOptional.isPresent()) {
                    continue;
                }

                Optional<MinTimePathLine> minTimePathLine = findMinTimePathLine(minTimePathLines, line.getId());

                if (!minTimePathLine.isPresent()) {
                    minTimePathLines.add(new MinTimePathLine(line, new ArrayList(Arrays.asList(edgeOptional.get()))));
                    break;
                }

                minTimePathLine.get().addEdge(edgeOptional.get());

                break;
            }
        }

        return minTimePathLines;
    }

    private Optional<Edge> findEdge(List<Edge> edges, long sourceId, long endId) {
        return edges.stream()
                .filter(it -> it.isSourceStation(sourceId) && it.isTargetStation(endId))
                .findFirst();
    }

    private Optional<MinTimePathLine> findMinTimePathLine(List<MinTimePathLine> minTimePathLines, long lineId) {
        return minTimePathLines.stream()
                .filter(it -> it.getLine().getId() == lineId)
                .findFirst();
    }

    private List<Line> getLine() {
        return minTimePathLines.stream()
                .map(it -> it.getLine())
                .collect(Collectors.toList());
    }

    private int getDistance() {
        return minTimePathLines.stream()
                .flatMap(it -> it.getEdges().stream())
                .collect(Collectors.summingInt(Edge::getDistance));
    }

    private LocalDateTime calculateArriveBy(LocalDateTime startDateTime) {
        LocalDateTime arriveDateTime = startDateTime;

        for (MinTimePathLine minTimePathLine : minTimePathLines) {
            Edge edge = minTimePathLine.getEdges().get(0);
            List<LocalTime> timeTables = findTimeTables(minTimePathLine.getLine().getId(), edge.getSourceStation().getId(), edge.getTargetStation().getId());

            boolean endSubway = true;

            for (LocalTime timeTable : timeTables) {
                if (timeTable.compareTo(arriveDateTime.toLocalTime()) == 0 || timeTable.isAfter(arriveDateTime.toLocalTime())) {
                    arriveDateTime = LocalDateTime.of(arriveDateTime.toLocalDate(), timeTable);
                    endSubway = false;

                    break;
                }
            }

            // 막차
            if (endSubway) {
                if(timeTables.get(0).isBefore(arriveDateTime.toLocalTime())) {
                    arriveDateTime = arriveDateTime.plusMinutes(1);
                }
                arriveDateTime = LocalDateTime.of(arriveDateTime.toLocalDate().plusDays(1), timeTables.get(0));
            }

            int elapsedTime = minTimePathLine.getEdges().stream().collect(Collectors.summingInt(Edge::getElapsedTime));
            arriveDateTime = arriveDateTime.plusMinutes(elapsedTime);
        }

        return arriveDateTime;
    }

    private List<LocalTime> findTimeTables(long lineId, long stationId, long nextStationId) {
        Line line = lines.stream()
                .filter(it -> it.getId() == lineId)
                .findFirst().get();

        List<Station> stations = line.getStations();

        int index = -1;
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).getId() == stationId) {
                index = i;
            }
        }

        if (index == 0 || stations.get(index + 1).getId() == nextStationId) {
            return line.getUpTimetablesOf(stationId).stream()
                    .map(it -> LocalTime.parse(it))
                    .collect(Collectors.toList());
        }

        if (index == stations.size() - 1 || stations.get(index - 1).getId() == nextStationId) {
            return line.getDownTimetablesOf(stationId).stream()
                    .map(it -> LocalTime.parse(it))
                    .collect(Collectors.toList());
        }

        throw new SubwayException(ErrorType.NOT_FOUND_TIME_TABLES);
    }

    @Getter
    private class MinTimePathLine {
        private Line line;
        private List<Edge> edges;

        public MinTimePathLine(Line line, List<Edge> edges) {
            this.line = line;
            this.edges = edges;
        }

        public void addEdge(Edge edge) {
            edges.add(edge);
        }
    }
}
