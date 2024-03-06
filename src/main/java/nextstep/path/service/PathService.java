package nextstep.path.service;

import nextstep.auth.application.UserDetail;
import nextstep.line.domain.Line;
import nextstep.line.persistance.LineRepository;
import nextstep.path.DistanceFareFactory;
import nextstep.path.domain.*;
import nextstep.path.domain.dto.PathsDto;
import nextstep.path.ui.PathType;
import nextstep.path.ui.PathsResponse;
import nextstep.station.domain.Station;
import nextstep.station.exception.StationNotFoundException;
import nextstep.station.persistance.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;



@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;


    public PathService(
            StationRepository stationRepository,
            LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathsResponse searchPath(long source, long target, PathType pathType, UserDetail userDetail) {
        try {
            PathFinder pathFinder = createPathFinder(lineRepository.findAll(), pathType);
            PathsDto pathsDto = pathFinder.findPath(getStation(source), getStation(target));
            List<SectionEdge> sectionEdges = pathsDto.getSectionEdges();
            List<LineFare> lineFares = sectionEdges.stream().map(SectionEdge::getLineFare).collect(Collectors.toList());

            Fare fare = new Fare(lineFares, pathsDto.getDistance());
            fare.calculateFare(DistanceFareFactory.createDistanceFare(), DiscountConditionFactory.createDiscountCondition(userDetail.getAge()));

            return PathsResponse.of(pathsDto, fare.getFare());
        } catch (IllegalArgumentException e) {
            CannotFindPathException ex = new CannotFindPathException("경로 탐색이 불가합니다");
            ex.initCause(e);
            throw ex;
        }
    }


    public boolean isConnectedPath(Station source, Station target, PathType pathType) {
        PathFinder pathFinder = createPathFinder(lineRepository.findAll(), pathType);
        return pathFinder.isConnected(getStation(source.getId()), getStation(target.getId()));
    }


    private Station getStation(long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(Long.toString(stationId)));
    }


    private PathFinder createPathFinder(List<Line> lineList, PathType pathType) {
        if (PathType.DISTANCE == pathType) {
            return new DistancePathFinder(lineList);
        }
        return new DurationPathFinder(lineList);
    }
}
