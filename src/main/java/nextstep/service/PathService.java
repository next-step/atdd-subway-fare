package nextstep.service;

import nextstep.domain.Line;
import nextstep.domain.Path;
import nextstep.domain.Station;
import nextstep.dto.PathResponse;
import nextstep.repository.LineRepository;
import nextstep.util.PathFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {

    private StationService stationService;
    private LineRepository lineRepository;

    public PathService(StationService stationService, LineRepository lineRepository){
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public PathResponse getPath(Long sourceId,Long targetId,String type){

        Station sourceStation = stationService.findStation(sourceId);
        Station targetStation = stationService.findStation(targetId);

        List<Line> lineList = lineRepository.findAll();

        PathFinder pathFinder = new PathFinder(lineList , type);
        Path path = pathFinder.findPath(sourceStation, targetStation);

        return PathResponse.createPathResponse(path);

    }

    public void validatePath(Long sourceId,Long targetId,String type){
        try {
            getPath(sourceId,targetId, type);
        } catch (Exception e) {
            throw new IllegalArgumentException("존재하지 않는 경로입니다.");
        }
    }
}
