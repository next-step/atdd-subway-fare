package nextstep.subway.path.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.entity.Line;
import nextstep.subway.line.domain.entity.LineSection;
import nextstep.subway.path.application.dto.PathResponse;
import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PathService {

    private final PathFinder shortestPathFinder;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathResponse findShortestPath(Long sourceId, Long targetId, PathType pathType,
        Optional<Integer> age) {
        Station source = stationRepository.findByIdOrThrow(sourceId);
        Station target = stationRepository.findByIdOrThrow(targetId);
        List<Line> lines = lineRepository.findAll();

        List<LineSection> shortestPath = shortestPathFinder.find(lines, source, target, pathType);
        return createPathResponse(shortestPath, age);
    }

    public void existsPath(Long sourceId, Long targetId) {
        Station source = stationRepository.findByIdOrThrow(sourceId);
        Station target = stationRepository.findByIdOrThrow(targetId);
        List<Line> lines = lineRepository.findAll();

        shortestPathFinder.find(lines, source, target, PathType.DURATION);
    }

    private PathResponse createPathResponse(List<LineSection> shortestPath, Optional<Integer> age) {
        List<StationResponse> stations = extractStationResponses(shortestPath);
        long totalDistance = calculateTotalDistance(shortestPath);
        long totalDuration = calculateTotalDuration(shortestPath);
        long fare = FareCalculator.calculateFare(totalDistance, shortestPath, age);
        return new PathResponse(stations, totalDistance, totalDuration, fare);
    }

    private List<StationResponse> extractStationResponses(List<LineSection> sections) {
        List<StationResponse> responses = sections.stream()
            .map(section -> StationResponse.from(section.getUpStation()))
            .collect(Collectors.toList());

        if (!sections.isEmpty()) {
            responses.add(StationResponse.from(sections.get(sections.size() - 1).getDownStation()));
        }

        return responses;
    }

    private long calculateTotalDistance(List<LineSection> sections) {
        return sections.stream().mapToLong(LineSection::getDistance).sum();
    }

    private long calculateTotalDuration(List<LineSection> sections) {
        return sections.stream().mapToLong(LineSection::getDuration).sum();
    }
}
