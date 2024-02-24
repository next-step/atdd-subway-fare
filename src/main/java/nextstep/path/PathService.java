package nextstep.path;

import lombok.RequiredArgsConstructor;
import nextstep.exception.InvalidInputException;
import nextstep.station.StationRepository;
import nextstep.station.StationResponse;
import org.jgrapht.alg.util.Pair;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PathService {
    private final PathFinder pathFinder;
    private final StationRepository stationRepository;

    public PathResponse showShortestPath(Long sourceId, Long targetId, String type) {
        if (sourceId.equals(targetId)) {
            throw new InvalidInputException("출발역과 도착역이 동일합니다.");
        }
        stationRepository.findById(sourceId).orElseThrow(EntityNotFoundException::new);
        stationRepository.findById(targetId).orElseThrow(EntityNotFoundException::new);

        SearchType searchType = SearchType.from(type);
        PathInfo pathInfo = searchType.findPath(pathFinder, Long.toString(sourceId), Long.toString(targetId));

        List<StationResponse> stations = pathInfo.getStationIds().stream()
                .map(Long::parseLong)
                .map(stationRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(StationResponse::from)
                .collect(Collectors.toList());
        int distance = pathInfo.getDistance();
        int duration = pathInfo.getDuration();

        return new PathResponse(stations, distance, duration);
    }
}
