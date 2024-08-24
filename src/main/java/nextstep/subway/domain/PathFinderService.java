package nextstep.subway.domain;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.LoginMember;
import nextstep.common.exception.PathNotFoundException;
import nextstep.common.exception.StationNotFoundException;
import nextstep.subway.infrastructure.SectionRepository;
import nextstep.subway.infrastructure.StationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PathFinderService {
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public Path findPath(Long sourceId, Long targetId, PathType pathType, int age) {
        List<Section> allSections = sectionRepository.findAll();
        List<Station> allStations = stationRepository.findAll();

        Station sourceStation = stationRepository.findById(sourceId)
                .orElseThrow(() -> new StationNotFoundException(sourceId));
        Station targetStation = stationRepository.findById(targetId)
                .orElseThrow(() -> new StationNotFoundException(targetId));

        PathFinder pathFinder = PathFinderFactory.createPathFinder(allSections, allStations, pathType);
        Path shortestPath = pathFinder.getShortestPath(allSections, sourceStation, targetStation, pathType, age);

        if (!shortestPath.isValid()) {
            throw new PathNotFoundException(sourceStation.getId(), targetStation.getId());
        }

        return shortestPath;
    }
}
