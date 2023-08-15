package nextstep.service;

import nextstep.domain.member.AbstractMember;
import nextstep.domain.member.Member;
import nextstep.domain.member.NullMember;
import nextstep.domain.subway.Line;
import nextstep.domain.subway.Path;
import nextstep.domain.subway.Station;
import nextstep.domain.subway.PathType;
import nextstep.dto.PathResponse;
import nextstep.repository.LineRepository;
import nextstep.util.PathFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final int NON_LOGIN_AGE = 30;
    private StationService stationService;
    private LineRepository lineRepository;

    public PathService(StationService stationService, LineRepository lineRepository){
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public PathResponse getPath(Long sourceId, Long targetId, PathType type, AbstractMember member){

        Station sourceStation = stationService.findStation(sourceId);
        Station targetStation = stationService.findStation(targetId);

        List<Line> lineList = lineRepository.findAll();

        PathFinder pathFinder = new PathFinder(lineList , type);
        Path path = pathFinder.findPath(sourceStation, targetStation);

        int age = !member.isNull() ? member.getAge() : NON_LOGIN_AGE;

        return PathResponse.createPathResponse(path,age);

    }

    public void validatePath(Long sourceId,Long targetId,PathType type){
        try {
            getPath(sourceId,targetId, type,new NullMember());
        } catch (Exception e) {
            throw new IllegalArgumentException("존재하지 않는 경로입니다.");
        }
    }
}
