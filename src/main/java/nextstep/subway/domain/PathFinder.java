package nextstep.subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;

public abstract class PathFinder {
    private final FareCalculator fareCalculator;

    protected PathFinder(FareCalculator fareCalculator) {
        this.fareCalculator = fareCalculator;
    }

    public PathResponse findPath(PathRequest pathRequest, List<Line> lines) {
        validateRequest(pathRequest, lines);

        PathResponse pathResponse = getPath(pathRequest, lines);

        pathResponse.updateFare(fareCalculator.calculateFare(pathResponse.getDistance(), pathResponse.getLines()));
        return pathResponse;
    }

    protected void validateRequest(PathRequest request, List<Line> lines) {
        if (request.getSource().equals(request.getTarget())) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }

        final List<Station> stations = getAllStationsInLines(lines);

        if (stations.stream().noneMatch(it -> it.getId().equals(request.getSource()))) {
            throw new IllegalArgumentException("출발역이 존재하지 않습니다.");
        }

        if(stations.stream().noneMatch(it -> it.getId().equals(request.getTarget()))) {
            throw new IllegalArgumentException("도착역이 존재하지 않습니다.");
        }
    }

    protected abstract PathResponse getPath(PathRequest pathRequest, List<Line> lines);

    private static List<Station> getAllStationsInLines(List<Line> lines) {
        return lines.stream()
            .flatMap(it -> it.getStations().stream())
            .collect(Collectors.toList());
    }
}
