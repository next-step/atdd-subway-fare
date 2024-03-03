package nextstep.path;

import lombok.RequiredArgsConstructor;
import nextstep.auth.principal.LoginMember;
import nextstep.exception.InvalidInputException;
import nextstep.line.LineRepository;
import nextstep.line.Lines;
import nextstep.station.Station;
import nextstep.station.StationRepository;
import nextstep.station.StationResponse;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PathService {
    private final PathFinder pathFinder;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathResponse showShortestPath(Long sourceId, Long targetId, String type, LoginMember loginMember) {
        if (sourceId.equals(targetId)) {
            throw new InvalidInputException("출발역과 도착역이 동일합니다.");
        }
        Station source = stationRepository.findById(sourceId).orElseThrow(EntityNotFoundException::new);
        Station target = stationRepository.findById(targetId).orElseThrow(EntityNotFoundException::new);

        SearchType searchType = SearchType.from(type);
        Path path = searchType.findPath(pathFinder, source, target);

        Lines lines = Lines.from(lineRepository.findAll());

        return new PathResponse(
            path.getStations().stream()
                    .map(StationResponse::from)
                    .collect(Collectors.toList()),
            path.getDistance(),
            path.getDuration(),
            path.getFare(lines, loginMember)
        );
    }


}
