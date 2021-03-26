package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineFareService {
    private final LineService lineService;

    public LineFareService(LineService lineService) {
        this.lineService = lineService;
    }

    public int getAdditionalFareOf(PathResult pathResult) {
        List<Line> allLine = lineService.findLines();
        return pathResult.filterLineHasSection(allLine)
                         .stream()
                         .map(Line::getAdditionalFare)
                         .reduce(0, Math::max);
    }
}
