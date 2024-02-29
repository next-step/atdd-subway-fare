package nextstep.path;

import lombok.RequiredArgsConstructor;
import nextstep.auth.principal.LoginMember;
import nextstep.exception.InvalidInputException;
import nextstep.line.LineRepository;
import nextstep.station.StationRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

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
        stationRepository.findById(sourceId).orElseThrow(EntityNotFoundException::new);
        stationRepository.findById(targetId).orElseThrow(EntityNotFoundException::new);

        SearchType searchType = SearchType.from(type);
        Path pathInfo = searchType.findPath(pathFinder, Long.toString(sourceId), Long.toString(targetId));

        return pathInfo.toResponse(stationRepository, lineRepository, loginMember);
    }
}
