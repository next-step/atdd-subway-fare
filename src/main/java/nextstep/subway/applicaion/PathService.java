package nextstep.subway.applicaion;

import java.util.List;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.JGraphTPathFinderImpl;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.PathFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineRepository lineRepository;
    private final PathFinder pathFinder;

    public PathService(LineRepository lineRepository, PathFinder pathFinder) {
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
    }

    public PathResponse getPath(PathRequest request) {
        final List<Line> lines = lineRepository.findAll();

        return pathFinder.findPath(request, lines);
    }
}
