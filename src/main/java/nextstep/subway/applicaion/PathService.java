package nextstep.subway.applicaion;

import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.dto.FareRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.FareAgeEnum;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.ui.exception.PathException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.subway.ui.exception.ExceptionMessage.SAME_STATION;

@Transactional
@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationService stationService;
    private final FarePolicyHandler farePolicyHandler;

    public PathService(LineRepository lineRepository, StationService stationService, FarePolicyHandler farePolicyHandler) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.farePolicyHandler = farePolicyHandler;
    }

    public PathResponse findPath(Long source, Long target, PathType type, LoginMember loginMember) {
        if (source.equals(target)) {
            throw new PathException(SAME_STATION.getMsg());
        }
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);

        PathFinder pathFinder = new PathFinder(lines, type);
        Path path = pathFinder.shortsPath(sourceStation, targetStation);
        return new PathResponse(path, farePolicyHandler.execute(new FareRequest(loginMember.getAge()), path));
    }
}
